import { Routes } from '@angular/router';
import { AdminLogin } from './features/auth/login/admin-login/admin-login';
import { AdminDashboard } from './features/dashboard/admin-dashboard/admin-dashboard';
import { ForgotPasswordPage } from './features/auth/forgot-password/forgot-password-page';
import { routeGuard } from './route-guard-guard';
import { LandingPage } from './features/auth/landing-page/landing-page';

export const routes: Routes = [
    { path: '', component: LandingPage},
    {path: 'login',component: AdminLogin},
    { path: 'admin-signup-page', loadComponent: () => import('./features/auth/signup/admin-signup-page/admin-signup-page').then(m =>m.AdminSignupPage) },
    { path: 'forgot-password', component: ForgotPasswordPage },
    { path: 'student-dashboard', loadComponent: () => import('./features/student-dashboard/student-dashboard').then(m => m.StudentDashboard)},
    { path: 'invigilator-dashboard', loadComponent: () => import('./features/invigilator-dashboard/invigilator-dashboard').then(m => m.InvigilatorDashboard)},
    {
        path: 'dashboard',
        component: AdminDashboard,
        canActivate: [routeGuard],
        children: [
            { path: '', redirectTo: 'home', pathMatch: 'full'},
            {path: 'home',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/admin-home-page/admin-home-page').then(m => m.AdminHomePage)},
            {path: 'department',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/departments-page/departments-page').then(m => m.DepartmentsPage)},
            {path: 'courses',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/courses-page/courses-page').then(m => m.CourseComponents)},
            {path: 'students',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/students-page/students-page').then(m => m.StudentComponent)},
            {path: 'invigilator',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/invigilators-page/invigilators-page').then(m => m.InvigilatorsPage)},
            {path: 'schedules',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/exam-scheduling-page/exam-scheduling-page').then(m => m.ExamSchedulingPage),},
            {path: 'schedules/create_schedule',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/exam-schedule-form/exam-schedule-form').then(m => m.ExamScheduleForm)},
            {path: 'halls',canActivate: [routeGuard],loadComponent: () => import('./features/dashboard/halls-page/halls-page').then(m => m.HallsPage)},
        
        ]
    },
    {path: '**',loadComponent: () => import('./features/wildcard-page/wildcard-page').then(m => m.WildcardPage)}
];
