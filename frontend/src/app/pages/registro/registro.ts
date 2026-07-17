import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { Auth } from '../../servicios/auth';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatIconModule
  ],
  templateUrl: './registro.html',
  styleUrls: ['./registro.css']
})
export class Registro implements OnInit {

  registroForm!: FormGroup;

  successMessage: string = '';

  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {

    this.registroForm = this.fb.group({

      nombre: ['', Validators.required],

      apellido: ['', Validators.required],

      correo: ['', [
        Validators.required,
        Validators.email
      ]],

      telefono: ['', Validators.required],

      dni: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(8)
      ]],

      passwordHash: ['', [
        Validators.required,
        Validators.minLength(6)
      ]],

      fechaNacimiento: ['', Validators.required],

      // Siempre será CLIENTE
      tipoUsuario: ['CLIENTE'],

      estadoCuenta: ['ACTIVA']

    });

  }

  onRegister(): void {

    if (this.registroForm.invalid) {

      this.errorMessage = 'Completa todos los campos.';

      return;

    }

    this.authService.registro(this.registroForm.value).subscribe({

      next: () => {

        this.successMessage = '✅ Cuenta creada correctamente.';

        this.errorMessage = '';

        setTimeout(() => {

          this.router.navigate(['/login']);

        }, 1500);

      },

      error: (error) => {

        console.error(error);

        this.successMessage = '';

        this.errorMessage =
          'No fue posible registrar el cliente.';

      }

    });

  }

}
