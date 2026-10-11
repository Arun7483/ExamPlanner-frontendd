import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SessionStorageService } from '../auth/signup/admin-signup-page/service/session-storage'; 
interface Student{
  id:string;
  name:string;
  present:boolean;
  is_practice:boolean;
  status:'Present'|'Absent';
  reason:string;
}
interface Exam{
  id:number;
  course:string;
  time:string;
  venue:string;
}

@Component({
  selector: 'app-invigilator-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, DialogModule, DatePipe],
  templateUrl: './invigilator-dashboard.html',
  styleUrls: ['./invigilator-dashboard.css']
})
export class InvigilatorDashboard implements OnInit {
  today: Date = new Date();
 profileData: any;
 activeView: string = 'dashboard';
 
 students:Student[]=[
  {
    id:'1',
    name:'Student One',
    present:true,
    is_practice:false,
    status:'Absent',
    reason:''
  },
  {
    id:'2',
    name:'Student Two',
    present:false,
    is_practice:true,
    status:'Absent',
    reason:''
  }
 ];
 constructor(
   private router: Router,
   private sessionService: SessionStorageService
 ) {}
 ngOnInit() {
   
   const profileSignal = this.sessionService.getProfile();
   this.profileData = profileSignal ? profileSignal() : null;
 }
 
 setView(view: string) {
   this.activeView = view;
   this.selectedExam = null; 
 }
 logout() {
   this.sessionService.clearProfile();
   this.router.navigate(['/login']);
 }
 
 notifications = [
   { title: 'Schedule Published', message: 'Mid-semester exam schedules published. Confirm by Jan 10.', date: 'Jan 5, 2026' },
   { title: 'Venue Change', message: 'Hall B → Hall C for Database exam on Jan 15.', date: 'Jan 4, 2026' },
   { title: 'Training Required', message: 'Mandatory training on Jan 8 at 2 PM.', date: 'Jan 3, 2026' }
 ];
 upcomingExams = [
   { course: 'MYSQL Database', date: 'Jan 15, 2026', time: '10:00 AM', venue: 'Hall A', students: 45 },
   { course: 'Computer Networks', date: 'Jan 17, 2026', time: '02:00 PM', venue: 'Hall B', students: 52 }
 ];
 viewPlan(exam: any) {
   alert(`Viewing plan for ${exam.course}`);
 }
 
 // ATTENDANCE FEATURE (NEW)
 todayExams : Exam[]= [
   { id: 1, course: 'MYSQL Database', time: '10:00 AM', venue: 'Hall A' },
   { id: 2, course: 'Computer Networks', time: '02:00 PM', venue: 'Hall B' },
   {id: 3, course:'Operating Systems',time:'11.30 PM',venue:'Hall C'},
   {id: 4, course:'Data Structures',time:'01:00 PM',venue:'Hall D'},
    {id: 5, course:'Software Engineering',time:'03:30 PM',venue:'Hall E'}
 ];
 selectedExam:Exam|null=null;
 attendanceStudents:Student[]=[];
 
 // Reports Feature
 selectedExamReport:Exam|null=null;
 reportStudents:Student[]=[];
 displayReportModal:boolean=false;
 
 openExam(exam: Exam): void {
 this.selectedExam = exam;
 
 this.attendanceStudents = [
   {
     id: 'STU01',
     name: 'Arjun Kumar',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU02',
     name: 'Sneha Rao',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU03',
     name: 'Rohan Verma',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU04',
     name: 'Ananya Singh',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU05',
     name: 'Vikram Patel',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU06',
     name: 'Pooja Nair',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU07',
     name: 'Rahul Mehta',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   }
 ];
}
saveAttendance(): void {
 const attendancePayload = {
   exam: this.selectedExam,
   students: this.attendanceStudents.map(s => ({
     id: s.id,
     name: s.name,
     status: s.present ? 'Present' : 'Absent'
   }))
 };
 console.log('Attendance Saved:', attendancePayload);
 alert('Attendance saved successfully!');
}

// REPORTS FEATURE
openExamReport(exam: Exam): void {
 this.selectedExamReport = exam;
 this.displayReportModal = true;
 
 this.reportStudents = [
   {
     id: 'STU01',
     name: 'Arjun Kumar',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU02',
     name: 'Sneha Rao',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU03',
     name: 'Rohan Verma',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU04',
     name: 'Ananya Singh',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   },
   {
     id: 'STU05',
     name: 'Vikram Patel',
     present: false,
     is_practice: false,
     status: 'Absent',
     reason: ''
   }
 ];
}

closeReportModal(): void {
 this.displayReportModal = false;
 this.selectedExamReport = null;
}

saveReport(): void {
 const reportPayload = {
   exam: this.selectedExamReport,
   students: this.reportStudents.map(s => ({
     id: s.id,
     name: s.name,
     status: s.status,
     malpractice: s.is_practice,
     remarks: s.reason
   }))
 };
 console.log('Report Saved:', reportPayload);
 alert('Report saved successfully!');
 this.closeReportModal();
}
 
 submitAttendance() {
   alert('Attendance saved successfully!');
   this.setView('dashboard');
 }
}