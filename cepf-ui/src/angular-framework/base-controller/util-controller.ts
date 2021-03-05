import {AfterViewInit, OnInit} from '@angular/core';

import * as $ from 'jquery';

export class UtilController implements OnInit, AfterViewInit {

  constructor(protected parentSelector?: string) {
    this.initialize();
  }

  ngOnInit(): void {
    this.initialize();
  }

  protected initialize(): void {
    if (this.parentSelector === undefined) {
      this.parentSelector = '#content';
    }
    // console.log(this.parentSelector);
  }

  ngAfterViewInit() {
    $(window)[0]['materialadmin'].App.initialize(this.parentSelector);
    $(window)[0]['materialadmin'].AppCard.initialize();
    $(window)[0]['materialadmin'].AppForm.initialize(this.parentSelector);
    $(window)[0]['materialadmin'].AppNavigation.initialize(this.parentSelector);
    $(window)[0]['materialadmin'].AppNavSearch.initialize(this.parentSelector);
    $(window)[0]['materialadmin'].AppOffcanvas.update(this.parentSelector);
    $(window)[0]['materialadmin'].AppVendor.initialize(this.parentSelector);

    // $('.input-group date').datepicker({autoclose: true, todayHighlight: true});

    // $('.floating-label .form-control').each(function () {
      // console.log($(this).context);
      // $(this).addClass('dirty');
      // console.log($(this));
      // console.log($(this).hasClass(''));
      // $(this).attr('disabled', 'true');
      // console.log($(this).attr('name') + ':' + $(this).attr('value'));
      // $(this).focus();
      // $(this).keyup();
      // $(this).on('focus', console.log($(this).context.attributes));
      // $(this).addClass('dirty');
      // $(this).blur();
      // $(this).focusin();
      // $(this).focusout();
      // $('.form').focus();
      // $(this).change();
      // $(this).blur();
    // });

    // $(window)[0]['materialadmin'].Demo.initialize(this.parentSelector);
    // $(window)[0]['materialadmin'].DemoLayout.initialize(this.parentSelector);
    // $(window)[0]['materialadmin'].DemoFormComponents.initialize(this.parentSelector);
    // $(window)[0]['materialadmin'].DemoDashboard.initialize();
  }

}
