import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarCliente } from './navbar-cliente';

describe('NavbarCliente', () => {
  let component: NavbarCliente;
  let fixture: ComponentFixture<NavbarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
