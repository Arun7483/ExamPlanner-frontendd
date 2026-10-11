// student-page.ts
import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToolbarModule } from 'primeng/toolbar';
import { PageStudentDTO, StudentDTO, StudentService, Department } from '../../../services/course-service/student-service';

@Component({ 
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TableModule, DialogModule, ButtonModule, InputTextModule, ToolbarModule],
  templateUrl: './students-page.html',
  styleUrls: ['./students-page.css']
})
export class StudentComponent {
  // Table data (local view-model)
  students = signal<any[]>([]);

  // UI state
  displayModal = signal(false);
  isEditing = signal(false);
  editingStudentId: string | null = null;

  // Departments loaded from backend
  departments = signal<Department[]>([]);
  selectedDepartment = signal<Department | null>(null);

  // error/info
  errorMessage = signal('');

  // columns (not strictly needed by PrimeNG if you template)
  tableColumns = [
    { field: 'id', header: 'ID' },
    { field: 'name', header: 'STUDENT NAME' },
    { field: 'email', header: 'EMAIL ADDRESS' },
    { field: 'department', header: 'DEPARTMENT' }
  ];

  studentForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService   // ✅ inject service
  ) {}

  ngOnInit(): void {
    this.studentForm = this.fb.group({
      studentName: ['', [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z0-9 ]*$')]],
      studentID: ['', [Validators.required, Validators.minLength(4), Validators.pattern('^[A-Z0-9]*$')]],
      studentEmail: ['', [Validators.required, Validators.email]],
      departmentCode: ['', [Validators.required]] // stores departmentCode
    });

    this.loadStudents();      // optional: backend or local
    this.loadDepartments();   // ✅ backend
  }

  // --- Loaders ---

  loadStudents() {
    // Option A: Keep your local table empty (as you had)
    // this.students.set([]);

    // Option B (recommended): Fetch from backend and map DTO -> view model used by your table
    this.studentService.getStudents(1, 10).subscribe({
      next: (page: PageStudentDTO) => {
        const viewRows = page.content.map(this.mapDtoToView);
        this.students.set(viewRows);
        this.errorMessage.set('');
      },
      error: () => this.errorMessage.set('Failed to load students')
    });
  }

  loadDepartments(): void {
    this.studentService.getDepartments().subscribe({
      next: (data: Department[]) => {
        this.departments.set(data);
        this.errorMessage.set('');
      },
      error: () => this.errorMessage.set('Failed to load departments')
    });
  }

  // --- Form helpers ---

  hasError(control: string, type: string): boolean {
    const ctrl = this.studentForm.get(control);
    return !!(ctrl && ctrl.hasError(type) && (ctrl.touched || ctrl.dirty));
  }

  onDepartmentChange(): void {
    const deptCode = this.studentForm.get('department')?.value as string;
    const selected = this.departments().find(d => (d.departmentCode ?? d.departmentId) === deptCode);
    this.selectedDepartment.set(selected || null);
  }

  // --- CRUD (local table or backend) ---

  showAddDialog() {
    this.isEditing.set(false);
    this.editingStudentId = null;
    this.studentForm.reset();
    this.selectedDepartment.set(null);
    this.displayModal.set(true);
  }

  saveStudent() {
    if (!this.studentForm.valid) {
      this.studentForm.markAllAsTouched();
      return;
    }

    const form = this.studentForm.value;
    // const currentStudents = [...this.students()];
    // if (this.editingStudentId === null) {
    //   currentStudents.push(form);
    // } else {
    //   const index = currentStudents.findIndex(s => s.id === this.editingStudentId);
    //   if (index !== -1) currentStudents[index] = form;
    // }
    // this.students.set(currentStudents);
    // this.displayModal.set(false);

    
    const dto = this.mapFormToDto(form);
    const request$ = this.editingStudentId === null
      ? this.studentService.createStudent(dto)
      : this.studentService.updateStudent(dto);

    request$.subscribe({
      next: (saved: StudentDTO) => {
        
        this.loadStudents();
        this.displayModal.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Save failed: ' + (err?.message || 'Check connection'));
      }
    });
  }

  editStudent(student: any) {
    this.isEditing.set(true);
    this.editingStudentId = student.id;

    this.studentForm.patchValue({
      name: student.name,
      id: student.id,
      email: student.email,
      department: student.department
    });

    const selected = this.departments().find(d => (d.departmentCode ?? d.departmentId) === student.department);
    this.selectedDepartment.set(selected || null);

    this.displayModal.set(true);
  }

  deleteStudent(student: any) {
    if (confirm(`Delete Student ${student.id}?`)) {
      
      this.studentService.deleteStudent(student.id).subscribe({
        next: () => this.loadStudents(),
        error: (err) => this.errorMessage.set('Delete failed: ' + (err?.message || 'Check connection'))
      });
    }
  }

  

  private mapDtoToView = (dto: StudentDTO) => ({
    id: dto.studentID,
    name: dto.studentName,
    email: dto.studentEmail,
    department: dto.departmentCode
  });

  private mapFormToDto(formValue: any): StudentDTO {
    return {
      studentID: formValue.studentID,
      studentName: formValue.studentName,
      studentEmail: formValue.studentEmail,
      departmentCode: formValue.departmentCode,
      currentSemester: 1
    };
  }
}