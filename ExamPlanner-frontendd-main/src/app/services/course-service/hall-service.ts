import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { apiConfig } from '../../config/api.config';

export interface HallAllocation {
  hallId: string;
  capacity: number;
  floor: string;
  availability: 'YES' | 'NO';
}

// Narrowing helper to ensure literal type
function toYesNo(value: unknown): HallAllocation['availability'] {
  return String(value).toUpperCase() === 'YES' ? 'YES' : 'NO';
}

@Injectable({ providedIn: 'root' })
export class HallService {
  private baseUrl = `${apiConfig.apiBaseUrl}/api/halls`;

  // Signals cache
  private hallsSignal = signal<HallAllocation[]>([]);
  public halls = computed(() => this.hallsSignal());

  public isLoading = signal(false);
  public error = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  /** GET /api/halls */
  getHalls(): Observable<HallAllocation[]> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.get<HallAllocation[]>(this.baseUrl).pipe(
      tap((data) => {
        const normalized: HallAllocation[] = data.map((h) => ({
          hallId: String(h.hallId),
          capacity: Number(h.capacity),
          floor: String(h.floor),
          availability: toYesNo(h.availability) // <-- proper narrowing
        }));
        this.hallsSignal.set(normalized);
        this.isLoading.set(false);
      }),
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }

  /** POST /api/halls  (Create) */
  createHall(hall: HallAllocation): Observable<HallAllocation> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.post<HallAllocation>(`${this.baseUrl}`, hall).pipe(
      tap((newHall) => {
        const current = this.hallsSignal();
        const merged: HallAllocation = {
          hallId: String(newHall.hallId),
          capacity: Number(newHall.capacity),
          floor: String(newHall.floor),
          availability: toYesNo(newHall.availability) // <-- proper narrowing
        };
        this.hallsSignal.set([...current, merged]);
        this.isLoading.set(false);
      }),
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }

  /** PUT /api/halls/{id}  (Update) */
  updateHall(id: string, hall: HallAllocation): Observable<HallAllocation> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.put<HallAllocation>(`${this.baseUrl}/${id}`, hall).pipe(
      tap((updatedHall) => {
        const current = this.hallsSignal();
        const updated = current.map((h) =>
          h.hallId === id
            ? {
                hallId: String(updatedHall.hallId),
                capacity: Number(updatedHall.capacity),
                floor: String(updatedHall.floor),
                availability: toYesNo(updatedHall.availability) // <-- proper narrowing
              }
            : h
        );
        this.hallsSignal.set(updated);
        this.isLoading.set(false);
      }),
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }

  /** DELETE /api/halls/{id} */
  deleteHall(id: string): Observable<any> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.delete(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        const current = this.hallsSignal();
        this.hallsSignal.set(current.filter((h) => h.hallId !== id));
        this.isLoading.set(false);
      }),
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }

  /** Centralized error handling */
  private handleError(error: HttpErrorResponse) {
    console.error('Hall Service Error:', error);
    const msg =
      (error?.error && (error.error.message || error.error.error)) ||
      error.message ||
      'Server Error';
    this.error.set(msg);
    this.isLoading.set(false);
    return throwError(() => new Error(msg));
  }
}