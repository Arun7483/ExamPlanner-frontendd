import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, finalize } from 'rxjs/operators';
import { apiConfig } from '../config/api.config';



export interface HallAllocation {
  hallId: string;          
  capacity: number;
  floor: string;
  availability: 'YES' | 'NO';
}

export interface Department {
  departmentId: number;
  departmentCode: string;
  name: string;
  headOfDepartment: string;
  totalSemesters: number;
}

export interface Course {
  id: number;             
  name: string;
  credits: string;
  department: Department;
  semester: number;
}

export interface ExamScheduleDTO {
  departmentCode: string;
  semester: number;
  examDate: string;        // YYYY-MM-DD
  startTime: string;     
  courseID: number;        
  duration: number;        
}

export interface StudentConflictRequest {
  departmentCode: string;
  semester: number;
  examDate: string;        
  startTime: string;       
  duration: number;        
}

export interface ExamHallAssignment {
  hall: HallAllocation;
  invigilator: {
    id: number;
    name?: string;         
  };
}

export interface ExamSchedule {
  id: number;
  course: Course;
  hallAssignments: ExamHallAssignment[]; 
  examDate: string;
  startTime: string;
  endTime: string;
  durationMinutes: number;
  departmentCode: string;
  semester: number;
}

export interface AssignPair {
  hallId: number;
  invigilatorId: number;
}

export interface AssignHallsDTO {
  examId: number;
  assignments: AssignPair[];
}


@Injectable({
  providedIn: 'root',
})
export class ExamScheduleService {
  private baseUrl = `${apiConfig.apiBaseUrl}/api/schedules`;

  private schedulesSignal = signal<ExamSchedule[]>([]);
  public schedules = computed(() => this.schedulesSignal());

  public isLoading = signal(false);
  public error = signal<string | null>(null);

  constructor(private http: HttpClient) {}


  getAllSchedules(): Observable<ExamSchedule[]> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.get<ExamSchedule[]>(`${this.baseUrl}/all`).pipe(
      tap((data) => this.schedulesSignal.set(data)),
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

  createSchedule(schedule: ExamScheduleDTO): Observable<ExamSchedule> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.post<ExamSchedule>(`${this.baseUrl}/new`, schedule).pipe(
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }


  checkStudentConflict(conflict: StudentConflictRequest): Observable<boolean> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.post<boolean>(`${this.baseUrl}/student_conflict`, conflict).pipe(
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }


  getAvailableHalls(date: string, startTime: string, duration: number): Observable<HallAllocation[]> {
    return this.http.get<HallAllocation[]>(
      `${this.baseUrl}/available-halls`,
      { params: { date, startTime, duration: String(duration) } }
    ).pipe(
      catchError((err) => this.handleError(err))
    );
  }


  assignResources(dto: AssignHallsDTO): Observable<ExamSchedule> {
    return this.http.put<ExamSchedule>(`${this.baseUrl}/assign-resources`, dto).pipe(
      catchError((err) => this.handleError(err))
    );
  }

  deleteSchedule(id: number): Observable<void> {
    this.isLoading.set(true);
    this.error.set(null);

    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      catchError((err) => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    );
  }

 
  private handleError(error: HttpErrorResponse) {
    console.error('Exam Schedule Service Error:', error);

    const msg =
      (typeof error.error === 'string' && error.error) ||
      error.error?.message ||
      error.message ||
      'Server Error';

    this.error.set(msg);
    return throwError(() => new Error(msg));
  }
}