import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

// PrimeNG Imports
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';

// Services
import { SessionStorageService } from '../../signup/admin-signup-page/service/session-storage';
import { AuthService } from '../../signup/admin-signup-page/service/auth.service';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    InputTextModule, 
    PasswordModule, 
    ButtonModule, 
    CardModule, 
    RouterLink, 
    SelectModule, 
    MessageModule
  ],
  templateUrl: './admin-login.html',
  styleUrl: './admin-login.css',
})
export class AdminLogin {
  isLoading = signal(false);
  // Fixed: using undefined instead of null to avoid TS2322 error
  errorMessage = signal<string | undefined>(undefined);

  private authService = inject(AuthService);
  private router = inject(Router);
  private session = inject(SessionStorageService);
  private fb = inject(FormBuilder);

  roles = [
    { label: 'Admin', value: 'admin' },
    { label: 'Student', value: 'student' },
    { label: 'Invigilator', value: 'invigilator' }
  ];

  adminForm = this.fb.group({
    userRole: ['admin', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  login() {
    if (this.adminForm.valid) {
      this.isLoading.set(true);
      this.errorMessage.set(undefined);

      const credentials = {
        email: this.adminForm.value.email,
        password: this.adminForm.value.password,
        role: this.adminForm.value.userRole
      };

      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.isLoading.set(false);
          const dbRole = response.role.toLowerCase().trim();

          this.session.setToken(response.token);
          this.session.setProfile({
            name: response.name,
            email: response.email,
            role: dbRole
          });

          this.handleNavigation(dbRole);
        },
        error: (err) => {
          this.isLoading.set(false);
          // Handles backend error messages (like "Role mismatch")
          const msg = typeof err.error === 'string' ? err.error : "Invalid email or password.";
          this.errorMessage.set(msg);
        }
      });
    } else {
      this.adminForm.markAllAsTouched();
    }
  }

  private handleNavigation(role: string) {
    if (role === 'admin') {
      this.router.navigate(['/dashboard/home']);
    } else if (role === 'student') {
      this.router.navigate(['/student-dashboard']);
    } else if (role === 'invigilator') {
      this.router.navigate(['/invigilator-dashboard']);
    }
  }
}