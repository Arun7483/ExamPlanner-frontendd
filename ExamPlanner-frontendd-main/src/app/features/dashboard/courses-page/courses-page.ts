import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { signal, Signal, computed } from '@angular/core';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DataTableComponent } from '../../../shared/data-table/data-table';
import { Course, CourseDTO, CourseService, Department } from '../../../services/course-service/course-service';

@Component({
  selector: 'app-course',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TableModule, DialogModule, ButtonModule, InputTextModule, DataTableComponent],
  templateUrl: './courses-page.html',
  styleUrl: './courses-page.css'
})
export class CourseComponents implements OnInit {
  tableColumns = [
    { label: 'COURSE ID', key: 'id' },
    { label: 'COURSE NAME', key: 'courseName' },
    { label: 'CREDITS', key: 'credits' },
    { label: 'DEPARTMENT', key: 'departmentCode', },
    { label: 'SEMESTER', key: 'semester' }
  ];

  // Signals
  courses: Signal<Course[]>;
  departments = signal<Department[]>([]);
  displayModal = signal(false);
  isEditing = signal(false);
  errorMessage = signal('');
  selectedDepartment = signal<Department | null>(null);

  // Computed signal for semester options based on selected department
  semesterOptions = computed(() => {
    const dept = this.selectedDepartment();
    if (!dept || !dept.totalSemesters) return [];
    return Array.from({ length: dept.totalSemesters }, (_, i) => i + 1);
  });

  courseForm!: FormGroup;

  constructor(
    private courseService: CourseService,
    private fb: FormBuilder
  ) {
    this.courses = this.courseService.courses;
    this.initializeForm();
  }

  private initializeForm(): void {
    this.courseForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      id: ['', [Validators.required, Validators.minLength(4)]],
      credits: ['', [Validators.required]],
      department: ['', [Validators.required]],
      semester: [{value: '',disabled: true}, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadCourses();
    this.loadDepartments();
    
  }

  loadCourses(): void {
    this.courseService.getCourses().subscribe({
      next: () => {
        this.errorMessage.set('');
      },
      error: () => this.errorMessage.set('Failed to load courses')
    });
  }

  loadDepartments(): void {
    this.courseService.getDepartments().subscribe({
      next: (data: Department[]) => {
        this.departments.set(data);
      },
      error: () => this.errorMessage.set('Failed to load departments')
    });
  }

  onDepartmentChange(): void {
    const deptCode = this.courseForm.get('department')?.value;
    const selected = this.departments().find(d => d.departmentCode === deptCode);
    this.selectedDepartment.set(selected || null);

    const semesterControl = this.courseForm.get('semester');
    
    if(!selected || !selected.totalSemesters) {
      semesterControl?.disable();
    }else{
      semesterControl?.enable();
    }

    // Reset semester when department changes
    this.courseForm.get('semester')?.reset();
  }

  saveCourse(): void {
    this.errorMessage.set('');
    
    if (this.courseForm.invalid) {
      this.errorMessage.set('Please fill all required fields correctly.');
      return;
    }

    const formValue = this.courseForm.getRawValue();
    const payload: CourseDTO = {
      id: formValue.id,
      courseName: formValue.name,
      credits: formValue.credits,
      departmentCode: formValue.department,
      semester: Number(formValue.semester)
    };

    const request = this.isEditing()
      ? this.courseService.updateCourse(payload)
      : this.courseService.createCourse(payload);

    request.subscribe({
      next: () => this.handleActionSuccess(),
      error: (err) => {
        this.errorMessage.set('Save failed: ' + (err.error?.message || 'Check connection'));
      }
    });
  }

  deleteCourse(course: Course): void {
    if (course.id && confirm(`Delete ${course.name || course.id}?`)) {
      this.courseService.deleteCourse(course.id).subscribe({
        next: () => {
          this.loadCourses();
          this.errorMessage.set('');
        },
        error: (err) => this.errorMessage.set('Delete failed: ' + (err.error?.message || 'Check connection'))
      });
    }
  }

  private handleActionSuccess(): void {
    this.loadCourses();
    this.displayModal.set(false);
    this.errorMessage.set('');
  }

  showAddDialog(): void {
    this.isEditing.set(false);
    this.errorMessage.set('');
    this.selectedDepartment.set(null);
    this.courseForm.reset();
    this.courseForm.get('id')?.enable();
    this.displayModal.set(true);
  }

  editCourse(course: Course): void {
    this.isEditing.set(true);
    console.log(this.courseService.courses())
    
    // Find and set the selected department
    const deptCode = course.departmentCode || course.department?.departmentCode;
    const selected = this.departments().find(d => d.departmentCode === deptCode);
    this.selectedDepartment.set(selected || null);

    this.courseForm.patchValue({
      id: course.id,
      name: course.name,
      credits: course.credits,
      department: deptCode || '',
      semester: course.semester
    });
    this.courseForm.get('id')?.disable();
    this.displayModal.set(true);
  }

  closeDialog(): void {
    this.displayModal.set(false);
    this.errorMessage.set('');
    this.selectedDepartment.set(null);
  }
}
