import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
@Component({
 selector: 'app-forgot-password-page',
 standalone: true,
 imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule,CardModule],
 templateUrl: './forgot-password-page.html',
 styleUrls: ['./forgot-password-page.css']
})
export class ForgotPasswordPage {
 form: FormGroup;
 constructor(private fb: FormBuilder) {
   this.form = this.fb.group({
     email: ['', [Validators.required, Validators.email]]
   });
 }
 submit() {
   if (this.form.invalid) {
     this.form.markAllAsTouched();
     return;
   }
   console.log('Send reset link to:', this.form.value.email);
   // later: call API service here
 }
}