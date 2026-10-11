import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { signal, Signal } from '@angular/core';
import { DataTableComponent } from '../../../shared/data-table/data-table';
import { Department, DepartmentService } from '../../../services/department-service/department-service';

@Component({
  selector: 'app-departments-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DataTableComponent],
  templateUrl: './departments-page.html',
  styleUrl: './departments-page.css'
})
export class DepartmentsPage implements OnInit {
  showModal = signal(false);
  isEditing = signal(false);
  errorMessage = signal<string>('');
  departments: Signal<Department[]>;

  deptForm!: FormGroup;

  tableColumns = [
    { label: 'ID', key: 'departmentId' },
    { label: 'DEPARTMENT NAME', key: 'name' },
    { label: 'CODE', key: 'departmentCode' },
    { label: 'HOD', key: 'headOfDepartment' },
    { label: 'SEMESTERS', key: 'totalSemesters', isBadge: true }
  ];

  constructor(private departmentService: DepartmentService, private fb: FormBuilder) {
    // Subscribe to department signal
    this.departments = this.departmentService.departments;
    this.initializeForm();
  }

  ngOnInit(): void {
    this.fetchDepartments();
  }

  // Initialize reactive form with validators
  private initializeForm(): void {
    this.deptForm = this.fb.group({
      departmentId: ['', [Validators.required, Validators.minLength(2)]],
      departmentCode: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(5)]],
      name: ['', [Validators.required, Validators.minLength(3)]],
      headOfDepartment: ['', [Validators.required, Validators.minLength(3)]],
      totalSemesters: [8, [Validators.required, Validators.min(1), Validators.max(8)]]
    });
  }

  fetchDepartments(): void {
    this.departmentService.getDepartments().subscribe({
      next: () => {
        this.errorMessage.set('');
      },
      error: (err: any) => this.errorMessage.set("Backend connection failed.")
    });
  }

  saveDepartment(): void {
    this.errorMessage.set('');

    if (this.deptForm.invalid) {
      this.errorMessage.set('Please fill all required fields correctly.');
      return;
    }

    const deptToSave: Department = {
      departmentId: this.deptForm.get('departmentId')?.value,
      departmentCode: this.deptForm.get('departmentCode')?.value,
      name: this.deptForm.get('name')?.value,
      headOfDepartment: this.deptForm.get('headOfDepartment')?.value,
      totalSemesters: Number(this.deptForm.get('totalSemesters')?.value) || 8
    };

    if (this.isEditing()) {
      console.log('Updating department:', deptToSave);
      this.departmentService.updateDepartment(deptToSave.departmentId, deptToSave).subscribe({
        next: () => this.handleActionSuccess(),
        error: (err: any) => this.errorMessage.set(err.message)
      });
    } else {
      console.log('Creating department:', deptToSave);
      this.departmentService.createDepartment(deptToSave).subscribe({
        next: () => this.handleActionSuccess(),
        error: (err: any) => this.errorMessage.set(err.message)
      });
    }
  }

  handleDelete(dept: Department): void {
    if (dept.departmentId && confirm(`Delete ${dept.name}?`)) {
      this.departmentService.deleteDepartment(dept.departmentId).subscribe({
        next: () => {
          this.errorMessage.set('');
        },
        error: (err: any) => this.errorMessage.set("Delete failed: " + err.message)
      });
    }
  }

  private handleActionSuccess(): void {
    this.closeModal();
    this.errorMessage.set('');
  }

  openAddModal(): void {
    this.isEditing.set(false);
    this.deptForm.reset({ totalSemesters: 8 });
    this.deptForm.get('departmentId')?.enable();
    this.showModal.set(true);
  }

  handleEdit(dept: Department): void {
    this.deptForm.patchValue({
      departmentId: dept.departmentId,
      departmentCode: dept.departmentCode,
      name: dept.name,
      headOfDepartment: dept.headOfDepartment,
      totalSemesters: dept.totalSemesters
    });
    this.deptForm.get('departmentId')?.disable();
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.errorMessage.set('');
    this.deptForm.reset({ totalSemesters: 8 });
  }
}