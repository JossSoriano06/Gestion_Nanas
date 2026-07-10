import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { Auth } from '../../servicios/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login implements OnInit {

  loginForm!: FormGroup;

  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {

    this.loginForm = this.fb.group({

      correo: ['', [Validators.required, Validators.email]],

      contrasena: ['', Validators.required]

    });

  }

  onSubmit(): void {

    if (this.loginForm.invalid) {

      return;

    }

    this.authService.login(this.loginForm.value).subscribe({

      next: (response) => {

        console.log(response);
        
        //Guarda el objeto entero de Juan en el navegador como texto string
        localStorage.setItem('usuario', JSON.stringify(response));

        localStorage.setItem("usuario_id", response.idUsuario);

        if(response.idNana){
            localStorage.setItem("id_nana", response.idNana);
        }

        const rol = response.tipoUsuario;

        if (rol === 'ADMIN') {

          this.router.navigate(['/dashboard-admin']);

        }

        else if (rol === 'CLIENTE') {

          this.router.navigate(['/dashboard-cliente']);

        }

        else if (rol === 'NANA') {

          this.router.navigate(['/dashboard-nana']);

        }

        else {

          this.errorMessage = 'Rol no reconocido.';

        }

      },

      error: (error) => {

        console.error(error);

        this.errorMessage = error.error?.error || 'Correo o contraseña incorrectos.';

      }

    });

  }

}
