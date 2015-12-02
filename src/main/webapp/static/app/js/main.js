$.extend(Tremapp, {
	
	dataTables: {
		
		initCompleteSelectableRowCallback: function(settings, json) {
			$(settings.nTBody).on('click', 'tr', function(){
				if (e && e.target && $(e.target).is('a')) return;
				var $this = $(this);
				if ($this.find('td:first').hasClass('dataTables_empty')) return;
				if ($this.hasClass('selected')) {
					$this.removeClass('selected');
				} else {
					$(settings.nTBody).find('tr.selected').removeClass('selected');
					$this.addClass('selected');
				}
			});
		},
		
		initCompleteSelectableRowsCallback: function(settings, json) {
			$(settings.nTBody).on('click', 'tr', function(e){
				if (e && e.target && $(e.target).is('a')) return;
				var $this = $(this);
				if ($this.find('td:first').hasClass('dataTables_empty')) return;
				$this.toggleClass('selected');
			});
		},
		
		getSelectedRowData: function($table) {
			return $table.DataTable().row('.selected').data();
		},
		
		getSelectedRowsData: function($table) {
			return $table.DataTable().rows('.selected').data();
		},
		
		clearRowSelection: function($table) {
			return $table.find('tbody tr.selected').removeClass('selected');
		},
		
		reloadTable: function($table) {
			$table.DataTable().ajax.reload();
		},
		
		renderLink: function(uri, name) {
			return '<a href="' + Tremapp.baseUrl + uri + '">' + (name == null ? uri : name) + '</a>';
		},
		
		impl: {
			
			result: {
				
				init: function(settings, json) {
					new Tremapp.ResultListPanes($(settings.nTable).parents('.panes:first'), settings, json).init();
				},
				
				renderName: function(name, type, result) {
					return '<a href="' + Tremapp.baseUrl + 'result/home/' + result.id + '" class="display-result" data-result-id="' + result.id + '" data-result-status="' + result.statusValue + '">' + (name == null ? result.id : name) + '</a>';
				},
				
				renderProject: function(project, type, result) {
					return Tremapp.dataTables.renderLink('project/home/' + result.projectId, project == null ? result.projectId : project);
				},
				
				renderStatus: function(status, type, result) {
					return '<span class="label bg-' + Tremapp.dataTables.impl.result.getStatusColor(result.statusValue) + '">' + status + '</span>';
				},
				
				renderSeverity: function(severity, type, result) {
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
	
	this.constructor = function($panes, parentPanes, childPanes) {
		this.$body = $('body');
		this.$panes = $panes;
		this.$leftPane = $panes.find('> .pane.pane-left');
		this.$rightPane = $panes.find('> .pane.pane-right');
		this.parentPanes = parentPanes;
		this.childPanes = childPanes;
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
	};
	
	this.maximize = function() {
		this.$body.addClass(this.bodyMaximizedCls);
		this.$panes.removeClass(this.rightPaneOnCls);
		this.$panes.addClass([this.maximizedCls, this.activeCls].join(' '));
	};
	
	this.displayRightPane = function() {
		this.childPanes && this.childPanes.closeRightPane();
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
	};
	
	this.expandRightPane = function() {
		this.$panes.addClass(this.rightPaneExpandedCls);
	};
	
	this.collapseRightPane = function() {
		this.$panes.removeClass(this.rightPaneExpandedCls);
		this.childPanes && this.childPanes.closeRightPane();
	};
	
});

Tremapp.ResultListPanes = Tremapp.Panes.extend(function(){
	
	this.resultId;
	
	this.dtSettings;
	
	this.dtJson;
	
	this.$detail;
	
	this.detailLoadingCls = 'loading';
	
	this.constructor = function($panes, dtSettings, dtJson) {
		this.super($panes);
		this.dtSettings = dtSettings;
		this.dtJson = dtJson;
		this.$detail = this.$rightPane.find('.result-detail');
	};
	
	this.bindEvents = function() {
		this.super.bindEvents();
		this.bindRightPaneEvents('.result-detail-pane-controls');
		
		var that = this;
		
		$(this.dtSettings.nTBody).on('click', 'a.display-result', function(e){
			var $this = $(this);
			$this.blur();
			that.displayRightPane({
				id: $this.attr('data-result-id'),
				name: $this.text(),
				status: $this.attr('data-result-status')
			});
			e.preventDefault();
		});
	};
	
	this.displayRightPane = function(result) {
		var that = this;
		this.$detail.html('');
		this.$detail.addClass(this.detailLoadingCls);
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
	
});

Tremapp.ResultDetailPanes = Tremapp.Panes.extend(function(){
	
	this.resultId;
	
	this.constructor = function($panes, resultId, parentPanes) {
		this.super($panes, parentPanes);
		this.resultId = resultId;
	};
	
	this.bindEvents = function() {
		this.super.bindEvents();
		this.bindRightPaneEvents('.result-attachment-pane-controls');
		
		var that = this;
		
		this.$leftPane.on('click', 'div.result-info.clickable', function(e){
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
	};
	
	this.displayRightPane = function($att) {
		var that = this,
			$header = this.$rightPane.find('.box-header'),
			$body = this.$rightPane.find('.box-body'),
			$viewLnk = $att.find('a.view'),
			path = $viewLnk.find('> span').text();
		
		$body.html('');
		$header.find('.attachment-name').text($att.attr('data-att-name'));
		$header.find('.attachment-lnk').text(path).attr('href', $viewLnk.attr('href'));
		this.super.displayRightPane();
		
		/*Tremapp.ajax({
			type: 'GET',
			url: Tremapp.baseUrl + 'a/result/attachment/view/' + this._detailId + '/' + path,
			dataType: 'html'
		}, function(html) {
			$body.html(html);
			that._$detail.removeClass(that._loadingCls);
		}, function() {
			that._$detail.removeClass(that._loadingCls + ' ' + that._withAttPreviewCls);
		});*/
	};
	
	this.toggleInfo = function($info) {
		$info.toggleClass('expanded');
	};
	
});
