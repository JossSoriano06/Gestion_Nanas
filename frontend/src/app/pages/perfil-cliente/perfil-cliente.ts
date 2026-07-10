import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { NavbarCliente } from '../../shared/components/navbar-cliente/navbar-cliente';
import { Footer } from '../../shared/components/footer/footer';

import { ClienteService } from '../../servicios/cliente'; 
import { Cliente } from '../../modelos/cliente'; 


@Component({
  selector: 'app-perfil-cliente',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NavbarCliente,
    Footer
  ],
  templateUrl: './perfil-cliente.html',
  styleUrl: './perfil-cliente.css',
})
export class PerfilCliente implements OnInit {
  perfilForm!: FormGroup;
  modoEdicion: boolean = false;
  cargando: boolean = true;
  errorMensaje: string = ''; 
  
  clienteDatos!: Cliente;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    this.obtenerUsuarioSesion();
  }

  obtenerUsuarioSesion(): void {
    this.cargando = true;
    this.errorMensaje = '';

    // Recuperamos al usuario real guardado tras el Login
    const usuarioSession = localStorage.getItem('usuario'); 
    
    if (usuarioSession) {
      this.clienteDatos = JSON.parse(usuarioSession);
      this.inicializarFormulario();
      this.cargando = false;
    } else {
      this.errorMensaje = 'No se encontró una sesión activa. Por favor, inicia sesión.';
      this.cargando = false;
    }
  }

  inicializarFormulario(): void {
    this.perfilForm = this.fb.group({
      nombre: [this.clienteDatos.nombre, [Validators.required]],
      apellido: [this.clienteDatos.apellido, [Validators.required]],
      telefono: [this.clienteDatos.telefono, [Validators.required]],
      correo: [this.clienteDatos.correo, [Validators.required, Validators.email]] // <-- Revisa que diga 'correo'
    });
  }

  activarEdicion(): void { 
    this.modoEdicion = true; 
  }

  cancelarEdicion(): void { 
    this.modoEdicion = false; 
    // Si cancela, restauramos los valores originales que tiene clienteDatos
    this.perfilForm.reset({
      nombre: this.clienteDatos.nombre,
      apellido: this.clienteDatos.apellido,
      telefono: this.clienteDatos.telefono,
      correo: this.clienteDatos.correo
    });
  }

  guardarCambios(): void {
    if (this.perfilForm.valid) {
      // Unimos los datos fijos del cliente con los nuevos del formulario
      const clienteActualizado: Cliente = {
        ...this.clienteDatos,
        ...this.perfilForm.value
      };

      // Mandamos la actualización al backend usando el ID del usuario real logueado
      this.clienteService.actualizarPerfil(this.clienteDatos.idUsuario, clienteActualizado).subscribe({
        next: (response) => {
          this.clienteDatos = response; // Actualizamos la vista estática
          localStorage.setItem('usuario', JSON.stringify(response)); // Actualizamos el localStorage
          this.modoEdicion = false; // Regresamos al modo lectura de solo ver
          console.log('¡Perfil actualizado con éxito en el servidor y localmente!');
        },
        error: (err) => {
          console.error('Error al intentar guardar los cambios:', err);
          alert('Hubo un error al guardar los cambios en el servidor.');
        }
      });
    }
  }
}