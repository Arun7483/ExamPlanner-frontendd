import { HttpClient } from '@angular/common/http';
import { Injectable, signal, computed } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { API_BASE_URL } from '../../config/api.config';

export interface Department {
  departmentId: string;   
  departmentCode?: string; 
  name: string;
  totalSemesters?: number;
}

export interface CourseDTO {
  id: string;
  courseName: string;
  credits: string;
  departmentCode: string;
  semester: number;
}

export interface Course {
  id: string;
  name: string;
  credits: string;
  semester?: number | string;
  departmentCode?: string; 
  department?: Department;
}

@Injectable({ providedIn: 'root' })
export class CourseService {
  private baseUrl = `${API_BASE_URL}/api/courses`;
  private deptUrl = `${API_BASE_URL}/api/departments`;

  // Signals
  private coursesSignal = signal<Course[]>([]);
  public courses = computed(() => this.coursesSignal());
  public isLoading = signal(false);
  public error = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  getCourses(): Observable<Course[]> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.get<Course[]>(this.baseUrl).pipe(
      tap((data) => {
        this.coursesSignal.set(data);
        console.log(data);
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  } 

  getDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(this.deptUrl);
  }

  createCourse(course: CourseDTO): Observable<Course> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.post<Course>(`${this.baseUrl}/add`, course).pipe(
      tap((newCourse) => {
        const current = this.coursesSignal();
        this.coursesSignal.set([...current, newCourse]);
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  updateCourse(course: CourseDTO): Observable<Course> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.put<Course>(`${this.baseUrl}/update`, course).pipe(
      tap((updatedCourse) => {
        const current = this.coursesSignal();
        const updated = current.map(c => c.id === updatedCourse.id ? updatedCourse : c);
        this.coursesSignal.set(updated);
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  deleteCourse(id: string): Observable<any> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.delete(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        const current = this.coursesSignal();
        this.coursesSignal.set(current.filter(c => c.id !== id));
        this.isLoading.set(false);
      }),
      catchError((err) => this.handleError(err))
    );
  }

  getCoursesByDeptAndSemester(deptCode: string, semester: number): Observable<CourseDTO[]> {
    return this.http.get<CourseDTO[]>(`${this.baseUrl}/${deptCode}/${semester}`);
  }

  private handleError(err: any) {
    console.error('Error:', err);
    const msg = err.error?.message || 'An error occurred';
    this.error.set(msg);
    this.isLoading.set(false);
    return throwError(() => new Error(msg));
  }
}