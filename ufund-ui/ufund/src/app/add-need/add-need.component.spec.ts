import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddNeedComponent } from './add-need.component';

describe('AddNeedComponent', () => {
  let component: AddNeedComponent;
  let fixture: ComponentFixture<AddNeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddNeedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddNeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
