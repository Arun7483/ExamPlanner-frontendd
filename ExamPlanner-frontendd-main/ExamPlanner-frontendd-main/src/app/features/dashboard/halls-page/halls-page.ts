// halls-page.ts
import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToolbarModule } from 'primeng/toolbar';
import { HallService, HallAllocation } from '../../../services/course-service/hall-service';

@Component({
  selector: 'app-halls-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    TableModule,
    ToolbarModule
  ],
  templateUrl: './halls-page.html',
  styleUrls: ['./halls-page.css'],
})
export class HallsPage {
  HallPageForm!: FormGroup;

  // Local table source (bound to the template)
  halls = signal<HallAllocation[]>([]);

  // Dialog state
  showAddDialog = false;

  // Edit state
  editingHallId: string | null = null;

  // Availability options to avoid free text
  availabilityOptions: Array<HallAllocation['availability']> = ['YES', 'NO'];

  constructor(private fb: FormBuilder, private hallService: HallService) {}

  ngOnInit(): void {
    this.HallPageForm = this.fb.group({
      // If your hallId can include letters (e.g., H101), update pattern:
      // hallId: ['', [Validators.required, Validators.pattern('^[A-Za-z0-9-]+$')]],
      hallId: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      capacity: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      floor: ['', [Validators.required]],
      availability: ['', [Validators.required]] // 'YES' | 'NO'
    });

    this.loadHalls();
  }

  loadHalls(): void {
    this.hallService.getHalls().subscribe({
      next: (data) => {
        // Normalize types (capacity might come as string from API)
        this.halls.set(
          data.map(h => ({
            hallId: String(h.hallId),
            capacity: Number(h.capacity),
            floor: String(h.floor),
            availability: (h.availability === 'YES' ? 'YES' : 'NO')
          }))
        );
      },
      error: (err) => console.error('Load halls error:', err)
    });
  }

  hasError(control: string, type: string): boolean {
    const ctrl = this.HallPageForm.get(control);
    return !!(ctrl && ctrl.hasError(type) && (ctrl.touched || ctrl.dirty));
  }

  openAddDialog(): void {
    this.editingHallId = null;
    this.HallPageForm.reset();
    this.showAddDialog = true;
  }

  saveHall(): void {
    if (!this.HallPageForm.valid) {
      this.HallPageForm.markAllAsTouched();
      return;
    }

    const form = this.HallPageForm.getRawValue();
    const payload: HallAllocation = {
      hallId: String(form.hallId),
      capacity: Number(form.capacity),
      floor: String(form.floor),
      availability: form.availability as 'YES' | 'NO'
    };

    if (this.editingHallId === null) {
      // Create
      this.hallService.createHall(payload).subscribe({
        next: () => {
          this.loadHalls();           // refresh list after create
          this.showAddDialog = false; // close dialog
        },
        error: (err) => console.error('Create hall error:', err)
      });
    } else {
      // Update
      this.hallService.updateHall(this.editingHallId, payload).subscribe({
        next: () => {
          this.loadHalls();           // refresh list after update
          this.showAddDialog = false; // close dialog
        },
        error: (err) => console.error('Update hall error:', err)
      });
    }
  }

  editHall(hall: HallAllocation): void {
    this.editingHallId = hall.hallId;

    this.HallPageForm.patchValue({
      hallId: hall.hallId,
      capacity: hall.capacity,
      floor: hall.floor,
      availability: hall.availability
    });

    this.showAddDialog = true;
  }

  deleteHall(hall: HallAllocation): void {
    if (confirm(`Delete Hall ${hall.hallId}?`)) {
      this.hallService.deleteHall(hall.hallId).subscribe({
        next: () => this.loadHalls(),
        error: (err) => console.error('Delete hall error:', err)
      });
    }
  }
}