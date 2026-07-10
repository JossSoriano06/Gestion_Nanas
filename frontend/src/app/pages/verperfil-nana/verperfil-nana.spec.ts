import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerperfilNana } from './verperfil-nana';

describe('VerperfilNana', () => {
  let component: VerperfilNana;
  let fixture: ComponentFixture<VerperfilNana>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerperfilNana]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerperfilNana);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
