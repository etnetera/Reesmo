$.extend(Tremapp, {
	
	goBack: function() {
		window.history.back();
	},
	
	dataTables: {
		
		initCompleteSelectableRowCallback: function(settings, json) {
			$(settings.nTBody).on('click', 'tr', function(){
				var $this = $(this);
				if (!Tremapp.dataTables.isSelectable(settings.nTable) || !Tremapp.dataTables.isRow($this)) return;
				if (e && e.target && $(e.target).is('a')) return;
				Tremapp.dataTables.toggleOneRowSelection($this, settings.nTBody);
			});
		},
		
		initCompleteSelectableRowsCallback: function(settings, json) {
			$(settings.nTBody).on('click', 'tr', function(e){
				var $this = $(this);
				if (!Tremapp.dataTables.isSelectable(settings.nTable) || !Tremapp.dataTables.isRow($this)) return;
				if (e && e.target && $(e.target).is('a')) return;
				Tremapp.dataTables.toggleRowSelection($this, settings.nTBody, null, e);
			});
		},
		
		getSelectedRowData: function($table) {
			return $table.DataTable().row('.selected').data();
		},
		
		getSelectedRowsData: function($table) {
			return $table.DataTable().rows('.selected').data();
		},
		
		clearRowSelection: function(tableEl) {
			var $body = $(tableEl);
			if ($body.is('table')) $body = $body.find('tbody');
			return $body.find('tr.selected').removeClass('selected');
		},
		
		toggleRowSelection: function(rowEl, bodyEl, select, e) {
			var $row = $(rowEl),
				$body = this.$getTableBody($row, bodyEl);
				
			if (e && (e.ctrlKey || e.metaKey)) {
				$row.toggleClass('selected', select == undefined ? undefined : !!select);
				if (this.isRowSelected($row)) {
					$body.data('last-selected', $body.find('tr').index($row));
				}
			} else if (e && e.shiftKey) {
				var lastSelected = $body.data('last-selected'),
					$trs = $body.find('tr'),
					start = (lastSelected && this.isRowSelected($trs.eq(lastSelected))) ? lastSelected : 0,
					end = $trs.index($row);
				
				this.clearRowSelection($body);	
	            $trs.slice(Math.min(start, end), Math.max(start, end) + 1)
	                .addClass('selected');
			} else {
				this.toggleOneRowSelection(rowEl, bodyEl, true);
			}
		},
		
		toggleOneRowSelection: function(rowEl, bodyEl, select) {
			var $row = (rowEl),
				select = (select == undefined ? !this.isRowSelected($row) : !!select);
			if (select) {
				$body = this.$getTableBody($row, bodyEl);
				this.clearRowSelection($body);
				$row.addClass('selected');
				$body.data('last-selected', $body.find('tr').index($row));
			} else {
				$row.removeClass('selected');
			}
		},
		
		isRowSelected: function(rowEl) {
			return $(rowEl).hasClass('selected');
		},
		
		isRow: function(rowEl) {
			return !$(rowEl).find('td:first').hasClass('dataTables_empty');
		},
		
		isSelectable: function(tableEl) {
			return $(tableEl).hasClass('selectable');
		},
		
		reloadTable: function($table) {
			$table.DataTable().ajax.reload();
		},
		
		renderLink: function(uri, name) {
			return '<a href="' + Tremapp.baseUrl + uri + '">' + (name == null ? uri : name) + '</a>';
		},
		
		$getTableBody: function(rowEl, bodyEl) {
			return bodyEl ? $(bodyEl) : $(rowEl).parents('tbody:first');
		},
		
		filters: {
			
			instances: {},
			
			dtInit: function(settings, json) {
				this.getFiltersFromSettings(settings).init();
			},
			
			dtStateLoaded: function(settings, data) {
				// just creates new filter instance with loaded state
				this.getFiltersFromSettings(settings, data);
			},
			
			/**
			 * Get filtering values from filter so it is updated
			 * in storage.
			 */
			dtStateSaveParams: function(settings, data) {
				this.getFiltersFromSettings(settings).stateSaveParams(settings, data);
			},
			
			/**
			 * Bind ajax data modification. This is called from DatatablesFiltersExtension java extension.
			 */
			dtInitAjaxData: function(tableId, tableVar, tableParams) {
				var that = this;
				tableParams.ajax.data = function(data) {
					return that.getFiltersFromId(tableId).modifyAjaxData(data);
				};
			},
			
			/**
			 * Returns filters using settings and creates a filter if not exists yet.
			 */
			getFiltersFromSettings: function(settings, initialData) {
				return this.getFiltersFromId($(settings.nTable).attr('id'), initialData);
			},
			
			/**
			 * Returns filters using id and creates a filter if not exists yet.
			 */
			getFiltersFromId: function(id, initialData) {
				var filters = this.instances[id];
				if (filters == null) {
					filters = new Tremapp.DataTablesFilters(id, initialData || {});
					this.instances[id] = filters;
				}
				return filters;
			}
		},
		
		impl: {
			
			result: {
				
				init: function(settings, json) {
					var $panes = $(settings.nTable).parents('.panes:first');
					new Tremapp.ResultListPanes($panes, settings, json, $('#' + $panes.attr('id') + '-panes-title').html()).init();
				},
				
				renderName: function(name, type, result) {
					return '<a href="' + Tremapp.baseUrl + 'result/home/' + result.id + '" class="display-result" data-result-id="' + result.id + '" data-result-status="' + result.statusValue + '">' + (name == null ? result.id : name) + '</a>';
				},
				
				renderProject: function(project, type, result) {
					return Tremapp.dataTables.renderLink('project/home/' + result.projectId, project == null ? result.projectId : project);
				},
				
				renderStatus: function(status, type, result) {
					if (result.statusValue == null) return '';
					return '<span class="label bg-' + Tremapp.dataTables.impl.result.getStatusColor(result.statusValue) + '">' + status + '</span>';
				},
				
				renderSeverity: function(severity, type, result) {
					if (result.severityValue == null) return '';
					var cls = Tremapp.dataTables.impl.result.getSeverityIconClass(result.severityValue);
					return '<i class="severity fa ' + cls + '" title="' + severity + '"></i>';
				},
				
				getStatusColor: function(status) {
					var color = null;
					switch (status) {
						case 'FAILED':
							color = 'red';
							break;
						case 'BROKEN':
							color = 'yellow';
							break;
						case 'SKIPPED':
							color = 'gray';
							break;
						case 'PASSED':
							color = 'green';
							break;
					}
					return color;
				},
				
				getSeverityIconClass: function(severity) {
					var cls = null;
					switch (severity) {
						case 'BLOCKER':
							cls = 'fa-ban text-red';
							break;
						case 'CRITICAL':
							cls = 'fa-long-arrow-up text-red';
							break;
						case 'NORMAL':
							cls = 'fa-angle-double-up text-red';
							break;
						case 'MINOR':
							cls = 'fa-angle-double-down text-green';
							break;
						case 'TRIVIAL':
							cls = 'fa-long-arrow-down text-muted';
							break;
					}
					return cls;
				}
				
			}
			
		}
		
	},
	
	ajax: function(options, successCallback, errorCallback) {
		options = options || {};
		if (successCallback) options.successCallback = successCallback;
		if (errorCallback) options.errorCallback = errorCallback;
		
		var errors = {
			global : function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: ' + errorThrown);
			},
			timeout: function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: Specified timeout was exceeded!');
			},
			abort: function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: Requested resource was not modified since last request!');
			},
			parseerror: function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: Wrong response format!');
			},
			unknown: function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: Wrong response format!');
			},
			0 : function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: You are not connected to internet or server not responds!');
			},
			401 : function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: You were logged out and therefore not authorized! You will be redirected to login page.');
				window.location.replace(Tremapp.baseUrl);
			},
			403 : function(status, JSON, xhr, textStatus, errorThrown) {
				alert('Error: This operation is forbidden for you!');
			}
		};
		
		var data = {};
		data[Tremapp.csrfName] = Tremapp.csrfValue;
		
		if (options.data) {
			options.data = $.extend(data, options.data);
		} else {
			options.data = data;
		}
		options.data = $.param(options.data, true);
		
		var settings = $.extend({
			type : 'POST',
			cache : false,
			dataType : 'json',
			success : function(JSON){
				if (typeof JSON == 'string') {
					if (options.successCallback) {
						options.successCallback(JSON);
					}
				} else {
					if (JSON.status == 'SUCCESS') {
						if (options.successCallback) {
							options.successCallback(JSON.result);
						}
					} else {
						if (options.errorCallback) {
							options.errorCallback(JSON.result);
						} else {
							// TODO - show error messages
							alert(JSON.result);
						}
					}
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				var error,
					status = 0,
					JSON = null;
		
				if (!textStatus) {
					error = 'unknown';
				} else if (textStatus == 'error') {
					if (xhr.status) {
						status = xhr.status;
					}
					error = status;
		
					try {
						JSON = $.parseJSON( xhr.responseText );
					} catch(e) {
						JSON = null;
					}
				} else {
					error = textStatus;
				}
		
				if (errorCallback) {
					errorCallback(status, JSON, xhr, textStatus, errorThrown);
				}
				if (errors[error]) {
					errors[error](status, JSON, xhr, textStatus, errorThrown);
				} else {
					errors.global(status, JSON, xhr, textStatus, errorThrown);
				}
			}
		}, options);

		$.ajax(settings);

		return false;
	}
	
});

Tremapp.DataTablesFilters = Class.extend(function(){
	
	this.$table;
	
	this.$container;
	
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
			filters: [],
			visibleFilters: this.definition.visibleFilters
		}, dtStateData.filters || {});
	};
	
	this.init = function() {
		if (!this.exists)
			return;
		this.createFilters();
		this.bindEvents();
		this.$container.show();
	};
	
	this.bindEvents = function() {
		var that = this;
		this.$container.on('click', '.dt-filters-trigger', function(e){
			that.onChange();
			e.preventDefault();
		});
	};
	
	this.createFilters = function() {
		var that = this,
			filters = [],
			visibleFilters = this.definition.visibleFilters; // TODO - use this.state.visibleFilters when custom filer selection is implemented 
		
		$.each(visibleFilters, function(i, field){
			var definition = that.getFilterDefinition(field),
				state = that.getFilterState(field),
				instance;
			switch (definition.type) {
				case 'text':
					instance = new Tremapp.DataTablesFilterText(that, definition, state).init();
					break;
				case 'select':
					instance = new Tremapp.DataTablesFilterSelect(that, definition, state).init();
					break;
			}
			if (instance) {
				that.$container.append(instance.$getElement());
				filters.push(instance);
			}
		});
		this.$container.append('<button type="button" class="btn btn-default dt-filters-trigger"><i class="fa fa-search"></i></button>');
		
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
	
	this.stateSaveParams = function(settings, dtStateData) {
		dtStateData.filters = this.state;
	};
	
	this.onChange = function() {
		// update data from filter elements
		var filters = [],
			visibleFilters = [];
		$.each(this.filters, function(i, filter){
			var state = filter.getState();
			if (state) filters.push(state);
			visibleFilters.push(filter.getField());
		});
		this.state.filters = filters;
		this.state.visibleFilters = visibleFilters;

		// reload table
		Tremapp.dataTables.reloadTable(this.$table);
	};
	
	this.modifyAjaxData = function(dtAjaxData) {
		if (this.state.filters && this.state.filters.length > 0) {
			var jsonFilters = {};
			$.each(this.state.filters, function(i, filter){
				jsonFilters[i] = JSON.stringify(filter);
				//jsonFilters.push(filter);
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

Tremapp.DataTablesFilter = Class.extend(function(){
	
	this.$element;
	
	this.filters;
	
	this.definition;
	
	this.state;
	
	this.constructor = function(filters, definition, state) {
		this.filters = filters;
		this.definition = definition;
		this.state = state;
	};
	
	this.init = function() {
		this.initElement();
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
	
});

Tremapp.DataTablesFilterText = Tremapp.DataTablesFilter.extend(function(){
	
	this.$button;
	
	this.$label;
	
	this.$value;
	
	this.$update;
	
	this.$close;
	
	this.constructor = function(filters, definition, state) {
		this.super(filters, definition, state);
	};
	
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
		this.changeValue(this.state ? this.state.prefix : "");
		
		this.$update = $('<button class="btn btn-default"/>').text(Tremapp.i18n.update).click(function(){
			that.$button.dropdown('toggle');
		});
		this.$close = $('<button class="btn btn-link"/>').text(Tremapp.i18n.close).click(function(){
			that.changeValue(that.state ? that.state.prefix : "");
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
	
	this.changeValue = function(value) {
		if (value && value.length > 0) {
			this.$label.text(this.definition.label + ': ' + value);
			this.$value.val(value);
		} else {
			this.$label.text(this.definition.label + ': ' + Tremapp.i18n.all);
		}
	};
	
	this.update = function() {
		var lastValue = (this.state || {}).prefix || '', 
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
		this.changeValue(this.state ? this.state.prefix : "");
		this.onChange();
	};
	
});

Tremapp.DataTablesFilterSelect = Tremapp.DataTablesFilter.extend(function(){
	
	this.$button;
	
	this.$label;
	
	this.$options;
	
	this.constructor = function(filters, definition, state) {
		this.super(filters, definition, state);
	};
	
	this.initElement = function() {
		var that = this;
		
		this.$element = $('<div class="btn-group dt-filter-type-select"/>');
		this.$button = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"/>');
		this.$label = $('<span/>');
		this.$button.append(this.$label).append(' <span class="caret"></span>');
		this.$element.append(this.$button);
		
		var values = this.state ? this.state.terms : [],
			$dropdown = $('<ul class="dropdown-menu"/>');
		
		$.each(this.definition.options, function(i, option){
			var $li = $('<li/>'),
				$label = $('<label/>'),
				$checkbox = $('<input type="checkbox"/>').attr('value', option.value);
			
			if (values.indexOf(option.value) > -1) {
				$checkbox.attr('checked', 'checked');
			}
			$label.append($checkbox).append(' ' + option.label);
			$dropdown.append($li.append($label));
		});
		
		this.changeValue(values);
		
		this.$element.append($dropdown);
		this.$options = this.$element.find('input[type=checkbox]');
		this.$element.on('change', 'input[type=checkbox]', function(){
			that.update();
		});
		
		$dropdown.click(function(e){
			e.stopPropagation();
		});
		
		this.$button.dropdown();
	};
	
	this.changeValue = function(values) {
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
		} else {
			this.$label.text(this.definition.label + ': ' + Tremapp.i18n.all);
		}
	};
	
	this.update = function() {
		var lastOptions = (this.state || {}).terms || [], 
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
		this.changeValue(options);
		this.onChange();
	};
	
});

Tremapp.Panes = Class.extend(function(){
	
	this.$body;
	
	this.$panes;
	
	this.$leftPane;
	
	this.$rightPane;
	
	this.parentPanes;
	
	this.childPanes;
	
	this.maximizedCls = 'maximized';
	
	this.activeCls = 'active';
	
	this.bodyMaximizedCls = 'panes-maximized';
	
	this.rightPaneOnCls = 'right-pane-on';
	
	this.rightPaneExpandedCls = 'right-pane-expanded';
	
	this.constructor = function($panes, parentPanes, childPanes, title) {
		this.$body = $('body');
		this.$panes = $panes;
		this.$leftPane = $panes.find('> .pane.pane-left');
		this.$rightPane = $panes.find('> .pane.pane-right');
		this.parentPanes = parentPanes;
		this.childPanes = childPanes;
		if (title != null) this.$panes.find('> .panes-title').html(title);
		if (!parentPanes) this.$panes.append($('<div class="panes-logo"/>').html(this.$body.find('.logo .logo-lg').html()));
	};
	
	this.init = function() {
		this.bindEvents();
	};
	
	this.bindEvents = function() {
		var that = this;
		if (!this.parentPanes) {
			this.$panes.on('click', '> .panes-minimizer', function(e){
				that.minimize();
				e.preventDefault();
			});
			this.$panes.on('click', '> .panes-maximizer', function(e){
				that.maximize();
				e.preventDefault();
			});
		}
		
		this.$leftPane.on('click', '> .expand-overlay', function(e){
			that.collapseRightPane();
			e.preventDefault();
		});
	};
	
	this.bindRightPaneEvents = function(controlsSelector) {
		var that = this;
		this.$rightPane.on('click', controlsSelector + ' i.close-right-pane', function(e){
			that.closeRightPane();
			e.preventDefault();
		});
		this.$rightPane.on('click', controlsSelector + ' i.expand-right-pane', function(e){
			that.expandRightPane();
			e.preventDefault();
		});
		this.$rightPane.on('click', controlsSelector + ' i.collapse-right-pane', function(e){
			that.collapseRightPane();
			e.preventDefault();
		});
	};
	
	this.minimize = function() {
		this.$body.removeClass(this.bodyMaximizedCls);
		this.$panes.removeClass([this.maximizedCls, this.activeCls, this.rightPaneOnCls].join(' '));
		this.childPanes && this.childPanes.minimize();
	};
	
	this.maximize = function() {
		this.$body.addClass(this.bodyMaximizedCls);
		this.$panes.removeClass(this.rightPaneOnCls);
		this.$panes.addClass([this.maximizedCls, this.activeCls].join(' '));
	};
	
	this.displayRightPane = function() {
		this.childPanes && this.childPanes.isRightPaneOpened() && this.childPanes.closeRightPane();
		this.$panes.removeClass(this.rightPaneExpandedCls);
		
		var addCls = [this.activeCls, this.rightPaneOnCls];
		if (!this.parentPanes) {
			addCls.push(this.maximizedCls);
			this.$body.addClass(this.bodyMaximizedCls);
		} else {
			this.parentPanes.expandRightPane();
		}
		this.$panes.addClass(addCls.join(' '));
	};
	
	this.closeRightPane = function() {
		this.$panes.removeClass(this.rightPaneOnCls);
		this.childPanes && this.childPanes.closeRightPane();
		this.parentPanes && this.parentPanes.isRightPaneExpanded() && this.parentPanes.collapseRightPane();
	};
	
	this.expandRightPane = function() {
		this.$panes.addClass(this.rightPaneExpandedCls);
	};
	
	this.collapseRightPane = function() {
		this.$panes.removeClass(this.rightPaneExpandedCls);
		this.childPanes && this.childPanes.isRightPaneOpened() && this.childPanes.closeRightPane();
	};
	
	this.isRightPaneOpened = function() {
		return this.$panes.hasClass(this.rightPaneOnCls);
	};
	
	this.isRightPaneExpanded = function() {
		return this.$panes.hasClass(this.rightPaneExpandedCls);
	};
	
});

Tremapp.ResultListPanes = Tremapp.Panes.extend(function(){
	
	this.resultId;
	
	this.dtSettings;
	
	this.dtJson;
	
	this.$table;
	
	this.$detail;
	
	this.$deleteToggler;
	
	this.$deleteTrigger;
	
	this.detailLoadingCls = 'loading';
	
	this.resultId;
	
	this.constructor = function($panes, dtSettings, dtJson, title) {
		this.super($panes, null, null, title);
		this.dtSettings = dtSettings;
		this.dtJson = dtJson;
		this.$table = $(dtSettings.nTable);
		this.$detail = this.$rightPane.find('.result-detail');
		this.$deleteToggler = this.$leftPane.find('.checkbox-enabled-delete-results');
		this.$deleteTrigger = this.$leftPane.find('.btn-delete-results');
	};
	
	this.bindEvents = function() {
		this.super.bindEvents();
		this.bindRightPaneEvents('.result-detail-pane-controls');
		
		var that = this;
		
		$(this.dtSettings.nTBody).on('click', 'tr', function(e){
			var $this = $(this);
			if (!Tremapp.dataTables.isSelectable(that.$table) || !Tremapp.dataTables.isRow($this)) return;
			var $target,
				$resultA,
				isA,
				isResultA,
				isRemovingEnabled;
			
			if (e && e.target) {
				$target = $(e.target);
				isA = $target.is('a');
			}
			if (isA) isResultA = $target.hasClass('display-result');
			if (isA && !isResultA) return;
			e.preventDefault();
			
			isRemovingEnabled = that.isRemovingEnabled();
			
			if (!isResultA && isRemovingEnabled) {
				Tremapp.dataTables.toggleRowSelection($this, that.dtSettings.nTBody, null, e);
				return;
			}
			$resultA = isResultA ? $target : $target.parents('tr:first').find('a.display-result');
			if (isResultA) $target.blur();
			
			that.displayRightPane({
				id: $resultA.attr('data-result-id'),
				name: $resultA.text(),
				status: $resultA.attr('data-result-status')
			});
			if (!isRemovingEnabled) Tremapp.dataTables.toggleOneRowSelection($this, that.$table);			
		});
		
		if (this.$deleteToggler.length > 0) {
			this.$deleteToggler.change(function(e){
				Tremapp.dataTables.clearRowSelection(that.$table);
				that.$table.toggleClass('remove', $(this).is(':checked'));
				that.$deleteTrigger.toggle();
			});
			
			var confirmText = this.$deleteTrigger.attr('data-confirm-text');
			this.$deleteTrigger.click(function(){
				var rowsData = Tremapp.dataTables.getSelectedRowsData(that.$table),
					ids = [];
				
				$.each(rowsData, function(i, rowData){
					ids.push(rowData.id);
				});
				if (ids.length < 1 || !confirm(confirmText.replace('{0}', ids.length))) return;
				
				if (that.resultId != null && ids.indexOf(that.resultId) > -1 && that.isRightPaneOpened()) {
					that.closeRightPane();
				}
				
				Tremapp.ajax({
					url: Tremapp.baseUrl + 'results/delete/',
					data: {
						resultIds: ids
					}
				}, function(removedCnt){
					Tremapp.dataTables.clearRowSelection(that.$table);
					Tremapp.dataTables.reloadTable(that.$table);
					// TODO - show count
				});
			});
		}
	};
	
	this.displayRightPane = function(result) {
		var that = this;
		this.$detail.html('');
		this.$detail.addClass(this.detailLoadingCls);
		this.resultId = result.id;
		this.super.displayRightPane();
		
		Tremapp.ajax({
			type: 'GET',
			url: Tremapp.baseUrl + 'a/result/detail/' + result.id,
			dataType: 'html'
		}, function(html) {
			that.$detail.html(html);
			that.childPanes = new Tremapp.ResultDetailPanes(that.$detail, result.id, that);
			that.childPanes.init();
			that.$detail.removeClass(that.detailLoadingCls);
		}, function() {
			that.$detail.removeClass(that.detailLoadingCls);
			that.closeRightPane();
		});
	};
	
	this.closeRightPane = function() {
		this.super.closeRightPane();
		if (!this.isRemovingEnabled()) Tremapp.dataTables.clearRowSelection(this.$table);
	};
	
	this.minimize = function() {
		this.super.minimize();
		if (!this.isRemovingEnabled()) Tremapp.dataTables.clearRowSelection(this.$table);
	};
	
	this.isRemovingEnabled = function() {
		return this.$table.hasClass('remove');
	};
	
	this.reloadTable = function() {
		Tremapp.dataTables.reloadTable(this.$table);
	};
	
});

Tremapp.ResultDetailPanes = Tremapp.Panes.extend(function(){
	
	this.resultId;
	
	this.projectId;
	
	this.attLoadingCls = 'loading';
	
	this.$attBody;
	
	this.$deleteTrigger;
	
	this.constructor = function($panes, resultId, parentPanes, title) {
		this.super($panes, parentPanes, null, title);
		this.resultId = resultId;
		this.projectId = this.$leftPane.attr('data-project-id');
		this.$attBody = this.$rightPane.find('.box-body');
		this.$deleteTrigger = this.$leftPane.find('.btn-delete-result');
	};
	
	this.bindEvents = function() {
		this.super.bindEvents();
		this.bindRightPaneEvents('.result-attachment-pane-controls');
		
		var that = this;
		
		this.$leftPane.on('click', 'div.result-info .nextlines-trigger', function(e){
			that.toggleInfo($(this));
			e.preventDefault();
		});
		
		this.$leftPane.on('click', 'div.result-attachment', function(e){
			if (e && e.target && ($(e.target).hasClass('download') || $(e.target).parent().hasClass('download'))) return;
			var $this = $(this);
			$this.blur();
			that.displayRightPane($this);
			e.preventDefault();
		});
		
		this.$attBody.on('click', 'img.result-att-img', function(e){
			if (that.isRightPaneExpanded()) that.collapseRightPane();
			else that.expandRightPane();
			e.preventDefault();
		});
		
		if (this.$deleteTrigger.length > 0) {
			var confirmText = this.$deleteTrigger.attr('data-confirm-text');
			this.$deleteTrigger.click(function(){
				if (!confirm(confirmText)) return;
			
				Tremapp.ajax({
					url: Tremapp.baseUrl + 'results/delete/',
					data: {
						resultIds: [that.resultId]
					}
				}, function(removedCnt){
					if (removedCnt > 0) {
						if (that.parentPanes) {
							that.parentPanes.closeRightPane();
							that.parentPanes.reloadTable();
						} else {
							window.location.replace(Tremapp.baseUrl + 'project/home/' + that.projectId);
						}
					}
				});
			});
		}
	};
	
	this.minimize = function() {
		this.super.minimize();
		this.$attBody.html('');
	};
	
	this.displayRightPane = function($att) {
		var that = this,
			$header = this.$rightPane.find('.box-header'),
			$viewLnk = $att.find('a.view'),
			path = $viewLnk.find('> span').text(),
			kind = $att.find('.result-attachment-inner').attr('data-att-kind');
		
		if (kind == 'image' || kind == 'txt' || kind == 'html') {
			this.$attBody.html('');
			this.$attBody.addClass(this.attLoadingCls);
			$header.find('.attachment-name').text($att.attr('data-att-name'));
			$header.find('.attachment-lnk').text(path).attr('href', $viewLnk.attr('href'));
			this.super.displayRightPane();
			
			if (kind == 'image') {
				this.$attBody.html('').append($('<img class="result-att-img"/>').attr('src', $viewLnk.attr('href')));
			} else {
				Tremapp.ajax({
					type: 'GET',
					url: Tremapp.baseUrl + 'a/result/attachment/view/' + this.resultId + '/' + path,
					dataType: 'html'
				}, function(html) {
					that.$attBody.html(html);
					if (that.$attBody.find('pre.prettyprint').length > 0) {
						prettyPrint(function(){
							that.$attBody.removeClass(that.attLoadingCls);
						}, that.$attBody.get(0));
					} else {
						that.$attBody.removeClass(that.attLoadingCls);
					}
				}, function() {
					that.$attBody.removeClass(this.attLoadingCls);
					that.closeRightPane();
				});
			}
		} else {
			window.open($viewLnk.attr('href'));
		}
	};
	
	this.closeRightPane = function() {
		this.super.closeRightPane();
		this.$attBody.html('');
	};
	
	this.toggleInfo = function($trigger) {
		$trigger.parent().toggleClass('expanded');
	};
	
});
