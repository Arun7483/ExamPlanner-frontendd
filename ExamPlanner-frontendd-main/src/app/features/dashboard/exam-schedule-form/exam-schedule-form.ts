import { CommonModule } from '@angular/common';
import { Component, OnInit, signal, computed, Signal, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { StepperModule } from 'primeng/stepper'; 
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { ProgressBarModule } from 'primeng/progressbar';
import { Router } from '@angular/router';
import { DepartmentService, Department } from '../../../services/department-service/department-service';
import { CourseService, Course } from '../../../services/course-service/course-service';
import { ExamScheduleService, StudentConflictRequest, ExamScheduleDTO} from '../../../services/exam-schedule-service';
import { StudentService } from '../../../services/course-service/student-service';


@Component({
  selector: 'app-exam-schedule-form',
  imports: [ButtonModule, StepperModule, CommonModule, DatePickerModule, SelectModule, ProgressBarModule, ReactiveFormsModule],
  templateUrl: './exam-schedule-form.html',
  styleUrl: './exam-schedule-form.css',
})
export class ExamScheduleForm implements OnInit {
  activeStep = signal(1);
  semesters = Array.from({ length: 8 }, (_, i) => i + 1);
  departments = signal<Department[]>([]);
  courses = signal<Course[]>([]);
  selectedCourseId = signal(null);
  totalStudents = signal(0);
  halls = [
    { id: 1, name: 'Hall A', capacity: 100 },
    { id: 2, name: 'Hall B', capacity: 80 },
    { id: 3, name: 'Hall C', capacity: 120 },
    { id: 4, name: 'Hall D', capacity: 60 },
    { id: 5, name: 'Hall E', capacity: 90 },
    { id: 6, name: 'Hall F', capacity: 110 },
  ];
  selectedHalls: number[] = [];
  invigilators = [
    { id: 101, name: 'Dr. Smith', department: 'CS' },
    { id: 102, name: 'Prof. Jones', department: 'Math' },
    { id: 103, name: 'Mr. Wilson', department: 'Physics' },
  ];
  assignments = [];
  duration = [
    { label: '1 Hour', value: 60 },
    { label: '2 Hours', value: 120 },
    { label: '3 Hours', value: 180 },
  ];
  
  conflictMessage = signal<string>('');

  form: FormGroup;

  constructor(
    private router: Router,
    private departmentService: DepartmentService,
    private studentService: StudentService,
    private courseService: CourseService,
    private examScheduleService: ExamScheduleService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      department: ['', Validators.required],
      semester: ['', Validators.required],
      course: ['', Validators.required],
      examDate: ['', Validators.required],
      startTime: ['', Validators.required],
      duration: ['', Validators.required],
      halls: [[]],
      invigilators: [[]]
    });
  }

  ngOnInit(): void {
    this.departmentService.getDepartments().subscribe({
      next: (depts) => this.departments.set(depts),
      error: () => this.departments.set([])
    });
  }

  onCourseSelected(): void {
    const deptCode = this.form.get('department')?.value;
    const semester = this.form.get('semester')?.value;
    this.selectedCourseId.set(this.form.get('course')?.value);;
    if(deptCode && semester && this.selectedCourseId()) {
      this.studentService.getStudentsByDeptAndSemester(deptCode,semester).subscribe({
        next: (value) => {
          this.totalStudents.set(value);
          console.log(value)
        },
        error: () => console.log("error")
      })
    }
  }

  navigate(step: number) {
    this.activeStep.set(step);
  }

  onDepartmentOrSemesterChange(): void {
    const deptCode = this.form.get('department')?.value;
    const semester = this.form.get('semester')?.value;
    if (deptCode && semester) {
      this.courseService.getCoursesByDeptAndSemester(deptCode, semester).subscribe({
        next: (dtos) => {
          this.courses.set(dtos.map(dto => ({
            id: dto.id,
            name: dto.courseName,
            credits: dto.credits,
            semester: dto.semester,
            departmentCode: dto.departmentCode

          })));
        },
        error: () => this.courses.set([])
      });
    } else {
      this.courses.set([]);
    }
    this.form.get('course')?.reset();
  }

  isHallSelected(hallId: number): boolean {
    return this.selectedHalls.includes(hallId);
  }

  toggleHallSelection(hallId: number): void {
    const index = this.selectedHalls.indexOf(hallId);
    if (index > -1) {
      this.selectedHalls.splice(index, 1);
    } else {
      this.selectedHalls.push(hallId);
    }
    this.form.get('halls')?.setValue(this.selectedHalls);
  }

  get selectedHallsCapacity(): number {
    return this.selectedHalls.reduce((total, hallID) => {
      const hall = this.halls.find((h) => h.id === hallID);
      return total + (hall ? hall.capacity : 0);
    }, 0);
  }

  get examCapacityPercentage(): number {
    if (this.totalStudents() === 0) return 0;
    const percentage = (this.selectedHallsCapacity / this.totalStudents()) * 100;
    return Math.min(percentage, 100);
  }

  get capacityStatus(): string {
    const covered = this.selectedHallsCapacity;
    const remaining = this.totalStudents() - covered;
    if (remaining <= 0) {
      return `Capacity: ${covered} / ${this.totalStudents} (${remaining === 0 ? 'Perfect' : 'Excess'})`;
    } else {
      return `Capacity: ${covered} / ${this.totalStudents} (${remaining} students remaining)`;
    }
  }

  // Called on "Next" after date/time selection
  checkStudentConflicts(step: number): void {
    const deptCode = this.form.get('department')?.value;
    const semester = this.form.get('semester')?.value;
    const examDate = this.form.get('examDate')?.value;
    const startTime = this.form.get('startTime')?.value;
    const duration = this.form.get('duration')?.value;
    
    if (deptCode && semester && examDate && startTime && duration) {
      const dateObj = new Date(examDate);
      const formattedDate = dateObj.toLocaleDateString('en-CA');
      const timeObj = new Date(startTime);
      const formattedTime = timeObj.toTimeString().split(' ')[0];
      console.log({
     deptCode: deptCode,
      semester: semester,
      examDate: formattedDate,
      startTime: formattedTime,
      duration: duration.value
    })
      const req: StudentConflictRequest = {
        departmentCode: deptCode,
        semester: semester,
        examDate: formattedDate,
        startTime: formattedTime,
        duration: duration.value
      };
      const dto: ExamScheduleDTO = {
        departmentCode: deptCode,
        semester: semester,
        examDate: formattedDate,
        startTime: formattedTime,
        courseID: this.selectedCourseId()!,
        duration: duration.value,
      }
      this.examScheduleService.checkStudentConflict(req).subscribe({
        next: (conflicts) => {
          console.log("success:"+conflicts)
          this.conflictMessage.set(conflicts ? 'Conflicts Found: There is already an exam scheduled at same time!' : 'No Conflicts.');
          if(!conflicts){
            this.examScheduleService.createSchedule(dto).subscribe({
              next: (response) => {
                console.log("Exam Schedule saved!")
                console.log(response)
                this.activeStep.set(3);
              },
              error: (err) =>{
                console.log(err)
                this.conflictMessage.set('Failed to save schedule.Please try again.')
              },
            })
          }
        },
        error: () => {
          this.conflictMessage.set('Error checking conflicts.');
        }
      });
    }
  }

  ScheduleExam() {
    this.router.navigate(['/dashboard/schedules']);
  }
}
