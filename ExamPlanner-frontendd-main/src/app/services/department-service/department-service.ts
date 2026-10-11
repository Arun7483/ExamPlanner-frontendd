import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface Department {
  departmentId: string;
  departmentCode: string;
  name: string;
  headOfDepartment: string;
  totalSemesters: number;
}

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private baseUrl = 'http://localhost:8090/api/departments';

  // Signal to store departments list
  private departmentsSignal = signal<Department[]>([]);
  public departments = computed(() => this.departmentsSignal());

  // Loading state signal
  public isLoading = signal(false);

  // Error state signal
  public error = signal<string | null>(null);

  constructor(private http: HttpClient) { }

  getDepartments(): Observable<Department[]> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.get<Department[]>(this.baseUrl).pipe(
      tap((data) => {
        this.departmentsSignal.set(data);
        this.isLoading.set(false);
      }),
      catchError((err) => {
        this.isLoading.set(false);
        return this.handleError(err);
      })
    );
  }

  createDepartment(dept: Department): Observable<Department> {
    this.isLoading.set(true);
    this.error.set(null);
    // Ensure totalSemesters is included in payload
    const payload = {
      ...dept,
      totalSemesters: dept.totalSemesters || 8
    };
    return this.http.post<Department>(`${this.baseUrl}/add`, payload).pipe(
      tap((newDept) => {
        const current = this.departmentsSignal();
        this.departmentsSignal.set([...current, newDept]);
        this.isLoading.set(false);
      }),
      catchError((err) => {
        this.isLoading.set(false);
        return this.handleError(err);
      })
    );
  }

  updateDepartment(id: string | number, dept: Department): Observable<Department> {
    this.isLoading.set(true);
    this.error.set(null);
    // Ensure totalSemesters is included in payload
    const payload = {
      ...dept,
      totalSemesters: dept.totalSemesters || 8
    };
    return this.http.put<Department>(`${this.baseUrl}/${id}`, payload).pipe(
      tap((updatedDept) => {
        const current = this.departmentsSignal();
        const updated = current.map(d => d.departmentId === id ? updatedDept : d);
        this.departmentsSignal.set(updated);
        this.isLoading.set(false);
      }),
      catchError((err) => {
        this.isLoading.set(false);
        return this.handleError(err);
      })
    );
  }

  deleteDepartment(id: string | number): Observable<void> {
    this.isLoading.set(true);
    this.error.set(null);
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        const current = this.departmentsSignal();
        this.departmentsSignal.set(current.filter(d => d.departmentId !== id));
        this.isLoading.set(false);
      }),
      catchError((err) => {
        this.isLoading.set(false);
        return this.handleError(err);
      })
    );
  }

  getDepartmentByCode(deptCode: string): Department | undefined {
    return this.departmentsSignal().find(d => d.departmentCode === deptCode);
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Logging Error:', error);
    const msg = error.error || 'Server Error';
    this.error.set(msg);
    return throwError(() => new Error(msg));
  }
}