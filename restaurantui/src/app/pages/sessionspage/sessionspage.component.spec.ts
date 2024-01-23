import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionspageComponent } from './sessionspage.component';

describe('SessionspageComponent', () => {
  let component: SessionspageComponent;
  let fixture: ComponentFixture<SessionspageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SessionspageComponent]
    });
    fixture = TestBed.createComponent(SessionspageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
