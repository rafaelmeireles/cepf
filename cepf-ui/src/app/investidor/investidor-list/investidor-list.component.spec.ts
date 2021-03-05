import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InvestidorListComponent } from './investidor-list.component';

describe('InvestidorListComponent', () => {
  let component: InvestidorListComponent;
  let fixture: ComponentFixture<InvestidorListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InvestidorListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InvestidorListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
