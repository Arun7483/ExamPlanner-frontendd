import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private students = new Array(2500).fill('Student'); 
  private exams = new Array(15).fill('Exam');
  private staff = new Array(75).fill('Staff');
  private halls = new Array(12).fill('Hall');

  private activities = [
    { text: 'Student John Doe registered.', time: '2 minutes ago' },
    { text: 'Math Exam schedule created for 2024-07-15.', time: '1 hour ago' },
    { text: 'Invigilator Jane Smith assigned to Hall A.', time: '3 hours ago' }
  ];

  getStudentCount() { return this.students.length; }
  getExamCount() { return this.exams.length; }
  getInvigilatorCount() { return this.staff.length; } 
  getHallCount() { return this.halls.length; }
  getRecentActivity() { return this.activities; }
}