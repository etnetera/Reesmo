Reesmo.dataTables.impl.result = {
			
	init: function(settings, json) {
		var $table = $(settings.nTable),
			$panes = $table.parents('.panes:first'),
			tableId = $table.attr('id'),
			projectId = $panes.attr('data-project-id');
		new Reesmo.ResultListPanes($panes, settings, json, $('#' + $panes.attr('id') + '-panes-title').html()).init();
		if (projectId)
			new Reesmo.DataTablesViews(tableId, projectId).init();
	},
	
	renderName: function(name, type, result) {
		return '<a href="' + Reesmo.baseUrl + 'result/home/' + result.id + '" class="display-result" data-result-id="' + result.id + '" data-result-status="' + result.statusValue + '">' + (name == null ? result.id : name) + '</a>';
	},
	
	renderProject: function(project, type, result) {
		return Reesmo.dataTables.renderLink('project/home/' + result.projectId, project == null ? result.projectId : project);
	},
	
	renderStatus: function(status, type, result) {
		if (result.statusValue == null) return '';
		return '<span class="label bg-' + Reesmo.dataTables.impl.result.getStatusColor(result.statusValue) + '">' + status + '</span>';
	},
	
	renderSeverity: function(severity, type, result) {
		if (result.severityValue == null) return '';
		var cls = Reesmo.dataTables.impl.result.getSeverityIconClass(result.severityValue);
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
	
};

Reesmo.DataTablesViews = Class.extend(function(){
	
	this.tableId;
	
	this.projectId;
	
	this.views;
	
	this.activeViewId;
	
	this.$control;
	
	this.$button;
	
	this.$formBox;
	
	this.$form;
	
	this.invalidForm;
	
	this.allowManage;
	
	this.constructor = function(tableId, projectId) {
		this.tableId = tableId;
		this.projectId = projectId;
		var viewsDef = window['oTable_' + tableId + '_viewsDef'];
		this.views = viewsDef.views;
		this.activeViewId = viewsDef.activeViewId;
	};
	
	this.init = function() {
		this.$formBox = $('#' + this.tableId + 'ViewForm');
		this.allowManage = this.$formBox.length > 0;
		if (this.allowManage) {
			this.$form = this.$formBox.find('form');
			this.invalidForm = this.$form.hasClass('invalid');
		}
		this.createControl();
		this.placeControlInFilters();
		this.bindEvents();
		if (this.invalidForm)
			this.$formBox.show();
	};
	
	this.createControl = function() {
		var that = this;
		
		this.$control = $('<div class="btn-group dt-views-container"/>');
		this.$button = $('<button type="button" class="btn btn-default dropdown-toggle dt-views-trigger" data-toggle="dropdown"><i class="fa fa-eye"></i></button>');
		if (this.activeViewId != null)
			this.$button.addClass('btn-success');
		this.$control.append(this.$button);
		
		var $dropdown = $('<ul class="dropdown-menu"/>');
		if (this.views.length > 0) {
			var $activeView = null;
			
			$.each(this.views, function(i, view){
				var $li = $('<li/>'),
					$a = $('<a/>');
				$a.attr('href', Reesmo.baseUrl + 'project/results/' + that.projectId + '/view/' + view.id);
				$a.append(view.name);
				$li.append($a);
				if (that.activeViewId != null && view.id == that.activeViewId) {
					$li.addClass('active');
					$activeView = $li;
				} else {
					$dropdown.append($li);
				}
			});
			if ($activeView != null)
				$dropdown.prepend($activeView);
		} else {
			this.$control.addClass('empty');
		}
		
		if (this.activeViewId != null)
			$dropdown.prepend('<li class="dt-view-changed-box" style="display: none"><span class="text-warning">' + Reesmo.i18n.get('views.control.changedFilters') + '</span></li>');
		
		if (this.allowManage) {
			$dropdown.append('<li class="dt-view-update-trigger-box" style="display: none"><button class="btn btn-warning dt-view-update-trigger"><i class="fa fa-save"></i> <span>' + Reesmo.i18n.get('views.control.updateView') + '</span></button></li>');
			$dropdown.append('<li class="dt-view-create-trigger-box"><button class="btn btn-primary dt-view-create-trigger"><i class="fa fa-plus"></i> <span>' + Reesmo.i18n.get('views.control.createView') + '</span></button></li>');
		}
		
		this.$control.append($dropdown);
	};
	
	this.placeControlInFilters = function() {
		if (!this.allowManage)
			return;
		this.getFilters().$container.find('.dt-filters-trigger').after(this.$control);
		this.$button.dropdown();
	}
	
	this.bindEvents = function() {
		var that = this;
		
		if (this.activeViewId != null)
			this.getFilters().addOnChangeListener('activeViewChange', this.onFiltersChange);
		
		if (this.allowManage) {
			this.$control.on('click', '.dt-view-create-trigger', function(e){
				that.$formBox.show();
				$(this).blur();
			});
			this.$formBox.on('click', '.btn-cancel', function(e){
				that.$formBox.hide();
				e.preventDefault();
			});
			this.$form.submit(function(e){
				var filtersData = that.getFilters().modifyAjaxData({});
				if (filtersData.filtersCnt == null || filtersData.filtersCnt < 1) {
					alert(Reesmo.i18n.get('views.create.noFiltersSelected'));
					e.preventDefault();
					return;
				}
				$(this).addHiddenInputData(filtersData);
			});
		}
	};
	
	this.getFilters = function() {
		return Reesmo.dataTables.filters.getFiltersFromId(this.tableId);
	};
	
	this.onFiltersChange = function(filters) {
		if (this.activeViewId != null) {
			this.$button.removeClass('btn-success').addClass('btn-warning');
			this.$control.find('.dt-view-changed-box').show();
			if (this.allowManage)
				this.$control.find('.dt-view-update-trigger-box').show();
		}
	};
	
});
