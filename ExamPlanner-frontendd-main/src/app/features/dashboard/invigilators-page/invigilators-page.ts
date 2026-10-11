import { Component, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  InvigilatorService,
  Invigilator,
  InvigilatorRequestDTO,
  Department
} from '../../../services/course-service/invigilator-service';

@Component({
  selector: 'app-invigilators-page',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule,
    CardModule,
    ToolbarModule,
    DialogModule,
    InputTextModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './invigilators-page.html',
  styleUrls: ['./invigilators-page.css'],
})
export class InvigilatorsPage implements OnInit {
  InvigilatorForm!: FormGroup;
  Invigilators = signal<Invigilator[]>([]);
  showAddDialog = false;
  editingInvigilatorId: string | null = null;

  departments = signal<Department[]>([]);
  errorMessage = signal<string>('');

  constructor(private fb: FormBuilder, private InvigilatorService: InvigilatorService) {}

  ngOnInit(): void {
    this.InvigilatorForm = this.fb.group({
      invigilatorId: ['', [Validators.required, Validators.pattern('^[0-9]+$')]], 
      name: ['', [Validators.required]],
      department: ['', [Validators.required]], 
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]] 
    });

    this.loadInvigilators();
    this.loadDepartments(); 
  }

  loadInvigilators() {
    this.InvigilatorService.getInvigilators().subscribe({
      next: (response: any) => {
        this.Invigilators.set(response?.content ?? response ?? []);
        this.errorMessage.set('');
      },
      error: () => this.errorMessage.set('Failed to load invigilators')
    });
  }

  loadDepartments() {
    this.InvigilatorService.getDepartments().subscribe({
      next: (data: Department[]) => {
        this.departments.set(data || []);
        this.errorMessage.set('');
      },
      error: () => this.errorMessage.set('Failed to load departments')
    });
  }

  hasError(control: string, type: string): boolean {
    const ctrl = this.InvigilatorForm.get(control);
    return !!(ctrl && ctrl.hasError(type) && (ctrl.touched || ctrl.dirty));
  }

  openAddDialog() {
    this.editingInvigilatorId = null;
    this.InvigilatorForm.reset();
    this.showAddDialog = true;
  }

  saveInvigilator() {
    if (!this.InvigilatorForm.valid) {
      this.InvigilatorForm.markAllAsTouched();
      return;
    }

    const form = this.InvigilatorForm.getRawValue();
    const payload: InvigilatorRequestDTO = {
      invigilatorId: String(form.invigilatorId),
      name: form.name,
      departmentCode: form.department, 
      email: form.email,
      phone: form.phone 
    };

    if (this.editingInvigilatorId === null) {
      this.InvigilatorService.createInvigilator(payload).subscribe({
        next: () => {
          this.loadInvigilators();
          this.showAddDialog = false;
        },
        error: (err: any) => console.log(err)
      });
    } else {
      this.InvigilatorService.updateInvigilator(payload).subscribe({
        next: () => {
          this.loadInvigilators();
          this.showAddDialog = false;
        },
        error: (err: any) => console.log(err)
      });
    }
  }

  editInvigilator(invigilator: any) {
    this.editingInvigilatorId = invigilator.invigilatorId;

    this.InvigilatorForm.patchValue({
      invigilatorId: invigilator.invigilatorId,
      name: invigilator.name,
      department: invigilator.department?.departmentCode ?? invigilator.departmentCode ?? '',
      email: invigilator.email,
      phone: invigilator.phone || ''
    });

    this.showAddDialog = true;
  }

  deleteInvigilator(invigilator: any) {
    if (confirm(`Delete Invigilator ${invigilator.invigilatorId}?`)) {
      this.InvigilatorService.deleteInvigilator(invigilator.invigilatorId).subscribe({
        next: () => this.loadInvigilators(),
        error: (err: any) => console.log(err)
      });
    }
  }
}
