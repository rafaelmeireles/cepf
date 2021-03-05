import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OperacaoEditComponent } from './operacao-edit.component';

describe('OperacaoEditComponent', () => {
  let component: OperacaoEditComponent;
  let fixture: ComponentFixture<OperacaoEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OperacaoEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OperacaoEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
