import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InvestidorEditComponent } from './investidor-edit.component';

describe('InvestidorEditComponent', () => {
  let component: InvestidorEditComponent;
  let fixture: ComponentFixture<InvestidorEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InvestidorEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InvestidorEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
