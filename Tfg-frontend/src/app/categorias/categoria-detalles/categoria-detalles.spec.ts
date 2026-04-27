import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoriaDetalles } from './categoria-detalles';

describe('CategoriaDetalles', () => {
  let component: CategoriaDetalles;
  let fixture: ComponentFixture<CategoriaDetalles>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoriaDetalles]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoriaDetalles);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
