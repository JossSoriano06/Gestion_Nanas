import { Component } from '@angular/core';

import { NavbarCliente } from '../../shared/components/navbar-cliente/navbar-cliente';
import { Footer } from '../../shared/components/footer/footer';

@Component({
  selector: 'app-favoritos',
  imports: [
    NavbarCliente,
    Footer
  ],
  templateUrl: './favoritos.html',
  styleUrl: './favoritos.css',
})
export class Favoritos {

}
