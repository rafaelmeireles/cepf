import {Input} from '@angular/core';

export abstract class BaseFields {

  @Input() public title: string;
  @Input() public fullTitle: boolean;

  protected abstract generateTitle(): void;
}

