import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, finalize } from 'rxjs/operators';
import { API_BASE_URL } from '../../config/api.config';

export interface InvigilatorRequestDTO {
  invigilatorId: string;
  name: string;
  email: string;
  departmentCode: string;
  phone?: string;
}

export interface Invigilator {
  invigilatorId: string;
  name: string;
  email: string;
  department: {
    departmentId: string;
    departmentCode: string;
    name: string;
    headOfDepartment: string;
    totalSemesters: number;
  };
  user: {
    id: number;
    name: string;
    email: string;
    role: string;
  };
}

export interface PageInvigilator {
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  size: number;
  content: Invigilator[];
  number: number;
  numberOfElements: number;
  empty: boolean;
}

export interface ServiceError {
  status: number;
  message: string;
  raw?: unknown;
}

export interface Department {
  departmentId: string;
  departmentCode: string;
  name: string;
  headOfDepartment?: string;
  totalSemesters?: number;
}

@Injectable({ providedIn: 'root' })
export class InvigilatorService {
  private readonly baseUrl = `${API_BASE_URL}/api/invigilators`;
  private readonly deptUrl = `${API_BASE_URL}/api/departments`;

  // Optional: keep departments in a signal cache
  private readonly departmentsSignal = signal<Department[]>([]);
  public readonly departments = computed(() => this.departmentsSignal());

  private readonly invigilatorsSignal = signal<Invigilator[]>([]);
  public readonly invigilators = computed(() => this.invigilatorsSignal());

  private readonly totalElementsSignal = signal<number>(0);
  public readonly totalElements = computed(() => this.totalElementsSignal());

  public readonly isLoading = signal<boolean>(false);
  public readonly error = signal<string | null>(null);

  constructor(private http: HttpClient) {}


  getDepartments(): Observable<Department[]> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.get<Department[]>(this.deptUrl).pipe(
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

  getInvigilators(page: number = 1, size: number = 5): Observable<PageInvigilator> {
    
    this.isLoading.set(true);
    this.error.set(null);

    const params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size));

    return this.http.get<PageInvigilator>(`${this.baseUrl}`, { params }).pipe(
      tap((data) => {
        this.invigilatorsSignal.set(data.content ?? []);
        this.totalElementsSignal.set(data.totalElements ?? 0);
      }),
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

  createInvigilator(invigilator: InvigilatorRequestDTO): Observable<Invigilator> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.post<Invigilator>(`${this.baseUrl}/add`, invigilator).pipe(
      tap((created) => {
        const current = this.invigilatorsSignal();
        this.invigilatorsSignal.set([...current, created]);
        this.totalElementsSignal.set(this.totalElements() + 1);
      }),
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }


  updateInvigilator(invigilator: InvigilatorRequestDTO): Observable<Invigilator> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.put<Invigilator>(`${this.baseUrl}/update`, invigilator).pipe(
      tap((updated) => {
        const current = this.invigilatorsSignal();
        const next = current.map((i) => (i.invigilatorId === updated.invigilatorId ? updated : i));
        this.invigilatorsSignal.set(next);
      }),
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

  deleteInvigilator(id: string): Observable<void> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.delete<void>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
      tap(() => {
        const current = this.invigilatorsSignal();
        const next = current.filter((i) => i.invigilatorId !== id);
        this.invigilatorsSignal.set(next);
        this.totalElementsSignal.set(Math.max(0, this.totalElements() - 1));
      }),
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

  getInvigilatorById(id: string): Observable<Invigilator> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.get<Invigilator>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Invigilator Service Error:', error);

    const message = this.extractErrorMessage(error);
    this.error.set(message);

    const serviceError: ServiceError = {
      status: error.status ?? -1,
      message,
      raw: error,
    };
    return throwError(() => serviceError);
  }

  private extractErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Cannot reach the server. Please ensure the API is running and CORS is configured.';
    }
    const body = error.error;

    if (typeof body === 'string' && body.trim().length > 0) {
      return body.split('\n')[0].trim();
    }
    if (typeof body === 'object' && body !== null) {
      const candidate =
        (body as any).message ??
        (body as any).error ??
        (body as any).detail ??
        (body as any).title;
      if (typeof candidate === 'string' && candidate.trim().length > 0) {
        return candidate;
      }
    }

    if (error.status === 409) return 'Conflict: An invigilator with the same ID or email may already exist.';
    if (error.status === 400) return 'Bad Request: Please verify the form data and try again.';
    if (error.status === 404) return 'Not Found: The requested resource does not exist.';
    if (error.status === 500) return 'Server Error: Something went wrong on the server.';

    const statusText = error.statusText || 'Error';
    return `HTTP ${error.status}: ${statusText}`;
  }
}