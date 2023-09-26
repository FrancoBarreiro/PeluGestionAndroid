import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobRegisterComponent } from './job-register.component';

describe('JobRegisterComponent', () => {
  let component: JobRegisterComponent;
  let fixture: ComponentFixture<JobRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JobRegisterComponent]
    });
    fixture = TestBed.createComponent(JobRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
