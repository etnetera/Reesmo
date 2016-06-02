Reesmo.DataTablesFilters = Class.extend(function(){
	
	this.$table;
	
	this.$container;
	
	this.$activeCounter;
	
	this.definition;
	
	this.state;
	
	this.exists = false;
	
	this.filters;
	
	this.constructor = function(tableId, dtStateData) {
		this.$table = $('#' + tableId);
		this.$container = $('#' + tableId + 'Filters');
		if (this.$container.length < 1)
			return;
		this.exists = true;
		
		this.definition = $.extend({
			filters: [],
			visibleFilters: []
		}, window['oTable_' + tableId + '_filtersDef'] || {});
		
		this.state = $.extend({
			filters: []
		}, dtStateData.filters || {});
	};
	
	this.init = function() {
		if (!this.exists)
			return;
		this.createFilters();
		this.bindEvents();
		this.$container.show();
		$.each(this.filters, function(i, filter){
			filter.onShow();
		});
	};
	
	this.bindEvents = function() {
		var that = this;
		this.$container.on('click', '.dt-filters-trigger', function(e){
			that.onChange();
			$(this).blur();
		});
		this.$container.on('click', '.dt-filters-trigger-more', function(e){
			if (that.$container.hasClass('dt-filters-less-view')) {
				that.$container.addClass('dt-filters-more-view');
				that.$container.removeClass('dt-filters-less-view');
			} else {
				that.$container.addClass('dt-filters-less-view');
				that.$container.removeClass('dt-filters-more-view');
			}
			$(this).blur();
		});
	};
	
	this.createFilters = function() {
		var that = this,
			filters = []; 
		
		$.each(this.definition.filters, function(i, definition){
			var visible = that.isFilterVisible(definition.field),			
				state = that.getFilterState(definition.field),
				instance;
			switch (definition.type) {
				case 'text':
					instance = new Reesmo.DataTablesFilterText(that, definition, state, visible).init();
					break;
				case 'select':
					instance = new Reesmo.DataTablesFilterSelect(that, definition, state, visible).init();
					break;
				case 'daterange':
					instance = new Reesmo.DataTablesFilterDateRange(that, definition, state, visible).init();
					break;
				case 'numberrange':
					instance = new Reesmo.DataTablesFilterNumberRange(that, definition, state, visible).init();
					break;
			}
			if (instance) {
				var $element = instance.$getElement();
				that.$container.append($element);
				filters.push(instance);
			}
		});
		
		this.$container
			.addClass('dt-filters-less-view')
			.append('<button type="button" class="btn btn-default dt-filters-trigger"><i class="fa fa-search"></i></button>')
			.append('<button type="button" class="btn btn-link dt-filters-trigger-more">'
						+ '<span class="dt-filters-more-label">' + Reesmo.i18n.get('dt.filters.showMore') + '</span>'
						+ '<span class="dt-filters-less-label">' + Reesmo.i18n.get('dt.filters.showLess') + '</span>'
						+ '<i class="fa fa-angle-left"></i></button>');
		
		this.filters = filters;
	};
	
	this.getFilterDefinition = function(field) {
		return $.grep(this.definition.filters, function(filter) {
			return filter.field == field;
		})[0] || null;
	};
	
	this.getFilterState = function(field) {
		return $.grep(this.state.filters, function(filter) {
			return filter.field == field;
		})[0] || null;
	};
	
	this.getFilterInstance = function(field) {
		return $.grep(this.filters, function(filter) {
			return filter.getField() == field;
		})[0] || null;
	};
	
	this.isFilterVisible = function(field) {
		// TODO - use this.state.visibleFilters when custom filter selection is implemented
		return this.definition.visibleFilters.indexOf(field) > -1;
	};
	
	this.stateSaveParams = function(settings, dtStateData) {
		dtStateData.filters = this.state;
	};
	
	this.onChange = function() {
		// update data from filter elements
		var filters = [];
		$.each(this.filters, function(i, filter){
			var state = filter.getState();
			if (state) filters.push(state);
		});
		this.state.filters = filters;

		// reload table
		Reesmo.dataTables.reloadTable(this.$table);
	};
	
	this.modifyAjaxData = function(dtAjaxData) {
		if (this.state.filters && this.state.filters.length > 0) {
			var jsonFilters = {};
			$.each(this.state.filters, function(i, filter){
				jsonFilters[i] = JSON.stringify(filter);
			});
			return $.extend({}, dtAjaxData, {
				filtersCnt: this.state.filters.length,
				filters: jsonFilters
			});
		} else {
			return dtAjaxData;
		}
	};
	
});

Reesmo.DataTablesFilter = Class.extend(function(){

	this.filterClass = 'dt-filter';
	
	this.hiddenClass = 'dt-filter-hidden';
	
	this.$element;
	
	this.filters;
	
	this.definition;
	
	this.state;
	
	this.visible;
	
	this.constructor = function(filters, definition, state, visible) {
		this.filters = filters;
		this.definition = definition;
		this.state = state;
		this.visible = visible;
	};
	
	this.init = function() {
		this.initElement();
		this.$element.addClass(this.filterClass);
		return this;
	};
	
	this.initElement = function() {
		throw "$createContainer method must be overriden in subtype";
	};
	
	this.$getElement = function() {
		return this.$element;
	};
	
	this.getDefinition = function() {
		return this.definition;
	};
	
	this.getField = function() {
		return this.definition.field;
	};
	
	this.getState = function() {
		if (this.state) {
			// make sure state contains field
			this.state.field = this.getField();
			return this.state;
		}
		return null;
	};
	
	this.remove = function() {
		this.$element.remove();
	};
	
	this.onChange = function() {
		this.filters.onChange();
	};
	
	this.onShow = function() {
		// custom logic can be run here after filter is shown
	};
	
	this.afterValueChange = function(hasValue) {
		if (!this.visible) {
			this.$element.toggleClass(this.hiddenClass, !hasValue);
		}
	};
	
});

Reesmo.DataTablesFilterText = Reesmo.DataTablesFilter.extend(function(){
	
	this.$button;
	
	this.$label;
	
	this.$value;
	
	this.$update;
	
	this.$close;
	
	this.initElement = function() {
		var that = this;
		
		this.$element = $('<div class="btn-group dt-filter-type-text"/>');
		this.$button = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"/>');
		this.$label = $('<span/>');
		this.$button.append(this.$label).append(' <span class="caret"></span>');
		this.$element.append(this.$button);
		
		var $dropdown = $('<ul class="dropdown-menu"/>');
		this.$value = $('<input type="text" class="form-control"/>');
		this.$value.keypress(function(e){
			if (e.which == 13) that.$button.dropdown('toggle');
		});
		this.setValue(this.getValueFromState(this.state));
		
		this.$update = $('<button class="btn btn-default"/>').text(Reesmo.i18n.get('update')).click(function(){
			that.$button.dropdown('toggle');
		});
		this.$close = $('<button class="btn btn-link"/>').text(Reesmo.i18n.get('close')).click(function(){
			that.setValue(that.getValueFromState(that.state));
			that.$button.dropdown('toggle');
		});
		
		$dropdown
			.append($('<li/>').append(this.$value))
			.append($('<li class="dropdown-buttons"/>').append(this.$update).append(this.$close));
		this.$element.append($dropdown);
		$dropdown.click(function(e){
			e.stopPropagation();
		});
		
		this.$button.dropdown();
		this.$element.on('hidden.bs.dropdown', function(){
			that.update();
		});
		this.$element.on('shown.bs.dropdown', function(){
			that.$value.focus();
		});
	};
	
	this.getValueFromState = function(state) {
		return state ? state.prefix : "";
	};
	
	this.setValue = function(value) {
		this.$value.val(value);
		this.onValueChange(value);
	};
	
	this.onValueChange = function(value) {
		if (value && value.length > 0) {
			this.$label.text(this.definition.label + ': ' + value);
			this.$button.removeClass('btn-default').addClass('btn-success');
			this.afterValueChange(true);
		} else {
			this.$label.text(this.definition.label + ': ' + Reesmo.i18n.get('all'));
			this.$button.addClass('btn-default').removeClass('btn-success');
			this.afterValueChange(false);
		}
	};
	
	this.update = function() {
		var lastValue = this.getValueFromState(this.state), 
			value = this.$value.val();
		if (lastValue == value)
			return;
		if (value && value.length > 0)
			this.state = {
				prefix: value,
				type: 'prefix'
			};
		else
			this.state = null;
		this.onValueChange(this.getValueFromState(this.state));
		this.onChange();
	};
	
});

Reesmo.DataTablesFilterSelect = Reesmo.DataTablesFilter.extend(function(){
	
	this.$button;
	
	this.$label;
	
	this.$options;
	
	this.initElement = function() {
		var that = this;
		
		this.$element = $('<div class="btn-group dt-filter-type-select"/>');
		this.$button = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"/>');
		this.$label = $('<span/>');
		this.$button.append(this.$label).append(' <span class="caret"></span>');
		this.$element.append(this.$button);
		
		var $dropdown = $('<ul class="dropdown-menu"/>');
		
		$.each(this.definition.options, function(i, option){
			var $li = $('<li/>'),
				$label = $('<label/>'),
				$checkbox = $('<input type="checkbox"/>').attr('value', option.value);
			$label.append($checkbox).append(' ' + option.label);
			$dropdown.append($li.append($label));
		});
		
		this.$element.append($dropdown);
		this.$options = this.$element.find('input[type=checkbox]');
		
		this.setValues(this.getValuesFromState(this.state));
		this.$element.on('change', 'input[type=checkbox]', function(){
			that.update();
		});
		
		$dropdown.click(function(e){
			e.stopPropagation();
		});
		
		this.$button.dropdown();
	};
	
	this.getValuesFromState = function(state) {
		return state ? state.terms : [];
	};
	
	this.setValues = function(values) {
		values = values || [];
		this.$options.each(function(){
			var $option = $(this);
			if (values.indexOf($option.attr('value')) > -1) {
				$option.attr('checked', 'checked');
			} else {
				$option.removeAttr('checked');
			}
		});
		this.onValuesChange(values);
	};
	
	this.onValuesChange = function(values) {
		var that = this,
			labels = [];
		$.each(values, function(i, value){
			var option = $.grep(that.definition.options, function(option){
				return option.value == value;
			})[0];
			if (option) labels.push(option.label);
			else labels.push(value);
		});
		if (labels && labels.length > 0) {
			this.$label.text(this.definition.label + ': ' + labels.join(', '));
			this.$button.removeClass('btn-default').addClass('btn-success');
			this.afterValueChange(true);
		} else {
			this.$label.text(this.definition.label + ': ' + Reesmo.i18n.get('all'));
			this.$button.addClass('btn-default').removeClass('btn-success');
			this.afterValueChange(false);
		}
	};
	
	this.update = function() {
		var lastOptions = this.getValuesFromState(this.state), 
			options = [];
		
		this.$options.filter(':checked').each(function(){
			options.push($(this).attr('value'));
		});
		if (JSON.stringify(lastOptions) == JSON.stringify(options))
			return;
		if (options && options.length > 0)
			this.state = {
				terms: options,
				type: 'terms'
			};
		else
			this.state = null;
		this.onValuesChange(this.getValuesFromState(this.state));
		this.onChange();
	};
	
});

Reesmo.DataTablesFilterDateRange = Reesmo.DataTablesFilter.extend(function(){
	
	this.format = 'YYYY-MM-DD';
	
	this.$button;
	
	this.$label;
	
	this.$from;
	
	this.$to;
	
	this.$update;
	
	this.$close;
	
	this.initElement = function() {
		var that = this;
		
		this.$element = $('<div class="btn-group dt-filter-type-daterange"/>');
		this.$button = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"/>');
		this.$label = $('<span/>');
		this.$button.append(this.$label).append(' <span class="caret"></span>');
		this.$element.append(this.$button);
		
		var $dropdown = $('<ul class="dropdown-menu"/>');
		
		this.$from = $('<input type="text" class="form-control date-from date-input"/>');
		this.$from.keypress(function(e){
			if (e.which == 13) that.$button.dropdown('toggle');
		});
		this.$to = $('<input type="text" class="form-control date-to date-input"/>');
		this.$to.keypress(function(e){
			if (e.which == 13) that.$button.dropdown('toggle');
		});
		
		this.setValue(this.getValueFromState(this.state));
		
		this.$from.daterangepicker({
			singleDatePicker: true,
			showDropdowns: true,
			autoUpdateInput: false,
			parentEl: $dropdown,
			locale: {
				format: this.format
			}
		});
		this.$to.daterangepicker({
			singleDatePicker: true,
			showDropdowns: true,
			autoUpdateInput: false,
			parentEl: $dropdown,
			locale: {
				format: this.format
			}
		});
		this.$from.on('apply.daterangepicker', function(ev, picker) {
			$(this).val(picker.startDate.format(that.format));
			ev.stopPropagation();
		});
		this.$to.on('apply.daterangepicker', function(ev, picker) {
			$(this).val(picker.startDate.format(that.format));
			ev.stopPropagation();
		});
		
		this.$update = $('<button class="btn btn-default"/>').text(Reesmo.i18n.get('update')).click(function(){
			that.$button.dropdown('toggle');
		});
		this.$close = $('<button class="btn btn-link"/>').text(Reesmo.i18n.get('close')).click(function(){
			that.setValue(that.getValueFromState(that.state));
			that.$button.dropdown('toggle');
		});
		
		$dropdown
			.append($('<li/>').append(Reesmo.i18n.get('between')).append(this.$from).append(Reesmo.i18n.get('and').toLowerCase()).append(this.$to))
			.append($('<li class="dropdown-buttons"/>').append(this.$update).append(this.$close));
		this.$element.append($dropdown);
		$dropdown.click(function(e){
			e.stopPropagation();
		});
		
		this.$button.dropdown();
		this.$element.on('hidden.bs.dropdown', function(){
			that.update();
		});
	};
	
	this.getValueFromState = function(state) {
		if (!state) return null;
		var from = moment(parseInt(state.from)),
			to = moment(parseInt(state.to)),
			hasValue = false,
			value = {};
		
		if (from.isValid()) {
			value.from = from;
			hasValue = true;
		}
		if (to.isValid()) {
			value.to = to;
			hasValue = true;
		}
		
		return hasValue ? value : null;
	};
	
	this.getValueFromHtml = function() {
		var from = moment(this.$from.val()),
			to = moment(this.$to.val()),
			hasValue = false,
			value = {};
		
		if (from.isValid()) {
			value.from = from;
			hasValue = true;
		}
		if (to.isValid()) {
			value.to = to;
			hasValue = true;
		}
		
		return hasValue ? value : null;
	};
	
	this.setValue = function(value) {
		if (value) {
			if (value.from != null) this.$from.val(value.from.format(this.format));
			else this.$from.val('');
			if (value.to != null) this.$to.val(value.to.format(this.format));
			else this.$to.val('');
		} else {
			this.$from.val('');
			this.$to.val('');
		}
		this.onValueChange(value);
	};
	
	this.onValueChange = function(value) {
		if (value) {
			var label = '';
			if (value.from != null && value.to != null) {
				label = (Reesmo.i18n.get('between') + ' ' + value.from.format(this.format) + ' ' + Reesmo.i18n.get('and').toLowerCase() + ' ' + value.to.format(this.format));
			} else if (value.from != null) {
				label = (Reesmo.i18n.get('after') + ' ' + value.from.format(this.format));
			} else {
				label = (Reesmo.i18n.get('before') + ' ' + value.to.format(this.format));
			}
			this.$label.text(this.definition.label + ': ' + label);
			this.$button.removeClass('btn-default').addClass('btn-success');
			this.afterValueChange(true);
		} else {
			this.$label.text(this.definition.label + ': ' + Reesmo.i18n.get('all'));
			this.$button.addClass('btn-default').removeClass('btn-success');
			this.afterValueChange(false);
		}
	};
	
	this.areSameValues = function(value1, value2) {
		if (value1 == null && value2 == null)
			return true;
		if (!!value1 != !!value2)
			return false;
		if ((!!value1.from != !!value2.from) || (value1.from != null && value2.from != null && !value1.from.isSame(value2.from)))
			return false;
		if ((!!value1.to != !!value2.to) || (value1.to != null && value2.to != null && !value1.to.isSame(value2.to)))
			return false;
		return true;
	};
	
	this.update = function() {
		var lastValue = this.getValueFromState(this.state), 
			value = this.getValueFromHtml();
		if (this.areSameValues(lastValue, value))
			return;
		if (value)
			this.state = {
				from: value.from ? value.from.valueOf() : null,
				to: value.to ? value.to.valueOf() : null,
				type: 'daterange'
			};
		else
			this.state = null;
		this.onValueChange(this.getValueFromState(this.state));
		this.onChange();
	};
	
});

Reesmo.DataTablesFilterNumberRange = Reesmo.DataTablesFilter.extend(function(){
	
	this.$button;
	
	this.$label;
	
	this.$from;
	
	this.$to;
	
	this.$update;
	
	this.$close;
	
	this.initElement = function() {
		var that = this;
		
		this.$element = $('<div class="btn-group dt-filter-type-numberrange"/>');
		this.$button = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"/>');
		this.$label = $('<span/>');
		this.$button.append(this.$label).append(' <span class="caret"></span>');
		this.$element.append(this.$button);
		
		var $dropdown = $('<ul class="dropdown-menu"/>');
		
		this.$from = $('<input type="text" class="form-control number-from number-input"/>');
		this.$from.keypress(function(e){
			if (e.which == 13) that.$button.dropdown('toggle');
		});
		this.$to = $('<input type="text" class="form-control number-to number-input"/>');
		this.$to.keypress(function(e){
			if (e.which == 13) that.$button.dropdown('toggle');
		});
		
		this.setValue(this.getValueFromState(this.state));
		
		this.$update = $('<button class="btn btn-default"/>').text(Reesmo.i18n.get('update')).click(function(){
			that.$button.dropdown('toggle');
		});
		this.$close = $('<button class="btn btn-link"/>').text(Reesmo.i18n.get('close')).click(function(){
			that.setValue(that.getValueFromState(that.state));
			that.$button.dropdown('toggle');
		});
		
		$dropdown
			.append($('<li/>').append(Reesmo.i18n.get('between')).append(this.$from).append(Reesmo.i18n.get('and').toLowerCase()).append(this.$to))
			.append($('<li class="dropdown-buttons"/>').append(this.$update).append(this.$close));
		this.$element.append($dropdown);
		$dropdown.click(function(e){
			e.stopPropagation();
		});
		
		this.$button.dropdown();
		this.$element.on('hidden.bs.dropdown', function(){
			that.update();
		});
	};
	
	this.getValueFromState = function(state) {
		if (!state) return null;
		var from = parseFloat(state.from),
			to = parseFloat(state.to),
			hasValue = false,
			value = {};
		
		if (!isNaN(from)) {
			value.from = from;
			hasValue = true;
		}
		if (!isNaN(to)) {
			value.to = to;
			hasValue = true;
		}
		
		return hasValue ? value : null;
	};
	
	this.getValueFromHtml = function() {
		var from = parseFloat(this.$from.val()),
			to = parseFloat(this.$to.val()),
			hasValue = false,
			value = {};
		
		if (!isNaN(from)) {
			value.from = from;
			hasValue = true;
		}
		if (!isNaN(to)) {
			value.to = to;
			hasValue = true;
		}
		
		return hasValue ? value : null;
	};
	
	this.setValue = function(value) {
		if (value) {
			if (value.from != null) this.$from.val(value.from);
			else this.$from.val('');
			if (value.to != null) this.$to.val(value.to);
			else this.$to.val('');
		} else {
			this.$from.val('');
			this.$to.val('');
		}
		this.onValueChange(value);
	};
	
	this.onValueChange = function(value) {
		if (value) {
			var label = '';
			if (value.from != null && value.to != null) {
				label = (Reesmo.i18n.get('between') + ' ' + value.from + ' ' + Reesmo.i18n.get('and').toLowerCase() + ' ' + value.to);
			} else if (value.from != null) {
				label = (Reesmo.i18n.get('moreThan') + ' ' + value.from);
			} else {
				label = (Reesmo.i18n.get('lessThan') + ' ' + value.to);
			}
			this.$label.text(this.definition.label + ': ' + label);
			this.$button.removeClass('btn-default').addClass('btn-success');
			this.afterValueChange(true);
		} else {
			this.$label.text(this.definition.label + ': ' + Reesmo.i18n.get('all'));
			this.$button.addClass('btn-default').removeClass('btn-success');
			this.afterValueChange(false);
		}
	};
	
	this.areSameValues = function(value1, value2) {
		if (value1 == null && value2 == null)
			return true;
		if (!!value1 != !!value2)
			return false;
		if (!!value1.from != !!value2.from)
			return false;
		if (!!value1.to != !!value2.to)
			return false;
		return true;
	};
	
	this.update = function() {
		var lastValue = this.getValueFromState(this.state), 
			value = this.getValueFromHtml();
		if (this.areSameValues(lastValue, value))
			return;
		if (value)
			this.state = {
				from: value.from ? value.from : null,
				to: value.to ? value.to : null,
				type: 'doublerange'
			};
		else
			this.state = null;
		this.onValueChange(this.getValueFromState(this.state));
		this.onChange();
	};
	
});
