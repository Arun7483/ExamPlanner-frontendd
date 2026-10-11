import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { SelectModule } from 'primeng/select'; // Added SelectModule
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { SessionStorageService } from './service/session-storage';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-admin-signup-page',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, InputTextModule, PasswordModule, SelectModule],
  templateUrl: './admin-signup-page.html',
  styleUrl: './admin-signup-page.css',
})
export class AdminSignupPage {
  private authService = inject(AuthService);
  private router = inject(Router);

  // Define roles for the dropdown
  roles = [
    { label: 'Admin', value: 'admin' },
    { label: 'Student', value: 'student' },
    { label: 'Invigilator', value: 'invigilator' }
  ];

  formdata: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    role: new FormControl('admin', [Validators.required]), // Default to admin
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    confirmPassword: new FormControl('', [Validators.required])
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    return password && confirmPassword && password.value !== confirmPassword.value 
      ? { 'passwordMismatch': true } : null;
  }

  SignupAdmin() {
    if (this.formdata.valid) {
      // Create the object to send to the backend
      const signupRequest = {
        name: this.formdata.value.name,
        email: this.formdata.value.email,
        password: this.formdata.value.password,
        role: this.formdata.value.role // Now sending the actual selected role!
      };

      this.authService.register(signupRequest).subscribe({
        next: (response) => {
          console.log("Registration successful:", response);
          
          // Use the response from backend to set profile
          this.router.navigate(['login']);
        },
        error: (err) => {
          console.error("Registration failed:", err);
          alert("Registration failed. Check if email already exists.");
        }
      });
    } else {
      this.formdata.markAllAsTouched();
    }
  }
}