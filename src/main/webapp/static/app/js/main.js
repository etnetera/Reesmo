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
				Tremapp.dataTables.toggleRowSelection($this);
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
		
		toggleRowSelection: function(rowEl, select) {
			$(rowEl).toggleClass('selected', select == undefined ? undefined : !!select);
		},
		
		toggleOneRowSelection: function(rowEl, bodyEl, select) {
			var $row = (rowEl),
				select = (select == undefined ? !$row.hasClass('selected') : !!select);
			if (select) {
				(bodyEl ? $(bodyEl) : $row.parents('tbody:first')).find('tr.selected').removeClass('selected');
				$row.addClass('selected');
			} else {
				$row.removeClass('selected');
			}
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
			if (!Tremapp.dataTables.isSelectable(that.$table) || !Tremapp.dataTables.isRow($this)) return;
			var $this = $(this),
				$target,
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
				Tremapp.dataTables.toggleRowSelection($this);
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
