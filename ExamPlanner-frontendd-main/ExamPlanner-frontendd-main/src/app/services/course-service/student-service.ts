import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { API_BASE_URL } from '../../config/api.config';

export interface Department {
  departmentId: string;   
  departmentCode?: string; 
  name: string;
  totalSemesters?: number;
}

export interface StudentDTO {
  studentID: string;
  studentName: string;
  studentEmail: string;
  departmentCode: string;
  currentSemester: number;
}

export interface PageStudentDTO {
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  size: number;
  content: StudentDTO[];
  number: number;
  numberOfElements: number;
  empty: boolean;
}

@Injectable({ providedIn: 'root' })
export class StudentService {
  private baseUrl = `${API_BASE_URL}/api/students`;
  private deptUrl = `${API_BASE_URL}/api/departments`;
  // Signals
  private studentsSignal = signal<StudentDTO[]>([]);
  public students = computed(() => this.studentsSignal());
  
  private totalElementsSignal = signal<number>(0);
  public totalElements = computed(() => this.totalElementsSignal());
  
  public isLoading = signal(false);
  public error = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  // Get paginated students
  getStudents(page: number = 1, size: number = 5): Observable<PageStudentDTO> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.get<PageStudentDTO>(`${this.baseUrl}?page=${page}&size=${size}`).pipe(
      tap((data) => {
        this.studentsSignal.set(data.content);
        this.totalElementsSignal.set(data.totalElements);
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  getDepartments(): Observable<Department[]> {
      return this.http.get<Department[]>(this.deptUrl);
    }

  // Get students by department and semester
  getStudentsByDeptAndSemester(deptCode: string, semester: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${deptCode}/${semester}`);
  }

  // Create new student
  createStudent(student: StudentDTO): Observable<StudentDTO> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.post<StudentDTO>(`${this.baseUrl}/add`, student).pipe(
      tap((newStudent) => {
        const current = this.studentsSignal();
        this.studentsSignal.set([...current, newStudent]);
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  // Update existing student
  updateStudent(student: StudentDTO): Observable<StudentDTO> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.put<StudentDTO>(`${this.baseUrl}/update`, student).pipe(
      tap((updatedStudent) => {
        const current = this.studentsSignal();
        const updated = current.map(s => s.studentID === updatedStudent.studentID ? updatedStudent : s);
        this.studentsSignal.set(updated);
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  // Delete student
  deleteStudent(id: string): Observable<any> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.delete(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        const current = this.studentsSignal();
        this.studentsSignal.set(current.filter(s => s.studentID !== id));
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Student Service Error:', error);
    const msg = error.error?.message || 'Server Error';
    this.error.set(msg);
    this.isLoading.set(false);
    return throwError(() => new Error(msg));
  }
}
