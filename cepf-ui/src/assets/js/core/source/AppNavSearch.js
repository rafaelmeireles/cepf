(function (namespace, $) {
	"use strict";

	var AppNavSearch = function () {
		// Create reference to this instance
		var o = this;
		// Initialize app when document is ready
		$(document).ready(function () {
			o.initialize();
		});

	};
	var p = AppNavSearch.prototype;

	// =========================================================================
	// MEMBERS
	// =========================================================================

	p._clearSearchTimer = null;

	// =========================================================================
	// INIT
	// =========================================================================

	p.initialize = function (parentSelector) {
		this._enableEvents(parentSelector);
	};

	// =========================================================================
	// EVENTS
	// =========================================================================

	// events
	p._enableEvents = function (parentSelector) {
		var o = this;

		// Listen for the nav search button click
		$(parentSelector + ' .navbar-search .btn').on('click', function (e) {
			o._handleButtonClick(e);
		});
	
		
		// When the search field loses focus
		$(parentSelector + ' .navbar-search input').on('blur', function (e) {
			o._handleFieldBlur(e);
		});
	};

	// =========================================================================
	// NAV SEARCH
	// =========================================================================

	p._handleButtonClick = function (e) {
		e.preventDefault();

		var form = $(e.currentTarget).closest('form');
		var input = form.find('input');
		var keyword = input.val();

		if ($.trim(keyword) === '') {
			// When there is no keyword, just open the bar
			form.addClass('expanded');
			input.focus();
		}
		else {
			// When there is a keyword, submit the keyword
			$('#content').html('');
			
			form.addClass('expanded');
			
			input.val(keyword);
			
			$.fda_struts2_jquery.simulate($('#fda_btn_search_central')[0], "click");
			
			/*$.ajax({
				  method: "POST",
				  url: "centralAtendimento_visualizar.action?objetoAtualiza.cadastroUnico.pessoaFisica.identificadorPrincipal=" + keyword
				})
				  .done(function( data ) {
					  $('#content').html(data);
					  form.removeClass('expanded');
					  input.val('');
				  });
			*/	  

			// Clear the timer that removes the keyword
			clearTimeout(this._clearSearchTimer);
		}
	};

	// =========================================================================
	// FIELD BLUR
	// =========================================================================

	p._handleFieldBlur = function (e) {
		// When the search field loses focus
		var input = $(e.currentTarget);
		var form = input.closest('form');

		// Collapse the search field
		form.removeClass('expanded');

		// Clear the textfield after 300 seconds (the time it takes to collapse the field)
		clearTimeout(this._clearSearchTimer);
		this._clearSearchTimer = setTimeout(function () {
			input.val('');
		}, 300);
	};

	// =========================================================================
	// DEFINE NAMESPACE
	// =========================================================================

	window.materialadmin.AppNavSearch = new AppNavSearch;
}(this.materialadmin, jQuery)); // pass in (namespace, jQuery):
