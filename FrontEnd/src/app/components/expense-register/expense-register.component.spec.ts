import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpenseRegisterComponent } from './expense-register.component';

describe('ExpenseRegisterComponent', () => {
  let component: ExpenseRegisterComponent;
  let fixture: ComponentFixture<ExpenseRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpenseRegisterComponent]
    });
    fixture = TestBed.createComponent(ExpenseRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
