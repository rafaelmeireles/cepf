(function(namespace, $) {
	"use strict";

	var AppForm = function() {
		// Create reference to this instance
		var o = this;
		// Initialize app when document is ready
		$(document).ready(function() {
			o.initialize();
		});

	};
	var p = AppForm.prototype;

	// =========================================================================
	// INIT
	// =========================================================================

	p.initialize = function(parentSelector) {
		// Init events
		this._enableEvents();
		
		this._initRadioAndCheckbox(parentSelector);
		this._initFloatingLabels(parentSelector);
		this._initValidation();
	};
	
	// =========================================================================
	// EVENTS
	// =========================================================================

	// events
	p._enableEvents = function () {
		var o = this;

		// Link submit function
		$('[data-submit="form"]').on('click', function (e) {
			e.preventDefault();
			var formId = $(e.currentTarget).attr('href');
			$(formId).submit();
		});
		
		// Init textarea autosize
		$('textarea.autosize').on('focus', function () {
			$(this).autosize({append: ''});
		});
	};
	
	// =========================================================================
	// RADIO AND CHECKBOX LISTENERS
	// =========================================================================

	p._initRadioAndCheckbox = function (parentSelector) {
		// Add a span class the styled checkboxes and radio buttons for correct styling
		$(parentSelector + ' .checkbox-styled input, .radio-styled input').each(function () {
			if ($(this).next('span').length === 0) {
				$(this).after('<span></span>');
			}
		});
	};
	
	// =========================================================================
	// FLOATING LABELS
	// =========================================================================

	p._initFloatingLabels = function (parentSelector) {
		var o = this;

		$(parentSelector + ' .floating-label .form-control').on('keyup focus change', function (e) {
			var input = $(e.currentTarget);

			if ($.trim(input.val()) !== '') {
				input.addClass('dirty').removeClass('static');
			} else {
				input.removeClass('dirty').removeClass('static');
			}
		});

		$(parentSelector + ' .floating-label .form-control').each(function () {
			var input = $(this);

      // console.log($(this).context.attributes);
      // console.log(input.context.attributes['ng-reflect-model']);
      if (typeof input.context.attributes['ng-reflect-model'] !== 'undefined' && $.trim(input.context.attributes['ng-reflect-model'].value) !== '' ) {
        input.addClass('dirty').removeClass('static');
      }
			// if ($.trim(input.val()) !== '') {
			// 	input.addClass('static').addClass('dirty');
			// }
		});

		$(parentSelector + ' .form-horizontal .form-control').each(function () {
			$(this).after('<div class="form-control-line"></div>');
		});
	};
	
	// =========================================================================
	// VALIDATION
	// =========================================================================

	p._initValidation = function () {
		if (!$.isFunction($.fn.validate)) {
			return;
		}
		$.validator.setDefaults({
			highlight: function (element) {
				$(element).closest('.form-group').addClass('has-error');
			},
			unhighlight: function (element) {
				$(element).closest('.form-group').removeClass('has-error');
			},
			errorElement: 'span',
			errorClass: 'help-block',
			errorPlacement: function (error, element) {
				if (element.parent('.input-group').length) {
					error.insertAfter(element.parent());
				}
				else if (element.parent('label').length) {
					error.insertAfter(element.parent());
				}
				else {
					error.insertAfter(element);
				}
			}
		});

		$('.form-validate').each(function () {
			var validator = $(this).validate();
			$(this).data('validator', validator);
		});
	};
	
	// =========================================================================
	// DEFINE NAMESPACE
	// =========================================================================

	window.materialadmin.AppForm = new AppForm;
}(this.materialadmin, jQuery)); // pass in (namespace, jQuery):
