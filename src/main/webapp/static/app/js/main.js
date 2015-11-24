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
				
				_$box: null,
				
				_$nav: null,
				
				_$content: null,
				
				_boxCls: 'box',
				
				_resultNavTabCls: 'result-nav-tab',
				
				_resultTabCls: 'result-tab',
				
				init: function($box, dtSettings, dtJson) {
					this._$box = $box;
					this._$nav = $box.find('> .nav');
					this._$content = $box.find('> .box-body');
					
					var that = this;
					$(dtSettings.nTBody).on('click', 'a.display-result', function(e){
						var $this = $(this);
						that.displayResult({
							id: $this.attr('data-result-id'),
							name: $this.text(),
							status: $this.attr('data-result-status')
						});
						e.preventDefault();
					});
					
					this._$nav.on('click', 'a', function(e){
						$(this).tab('show');
						e.preventDefault();
					});
					
					this._$nav.on('click', 'i.close-result', function(e){
						var $this = $(this),
							$a = $this.parent('a'),
							$li = $a.parent('li'),
							$tab = that._$content.find($a.attr('href')),
							active = $li.hasClass('active');
						
						if (active) {
							$li.prev('li').find('a').tab('show');
						}
						$li.remove();
						$tab.remove();
						
						if (that.getDisplayedResultsCnt() < 1)
							that.toggleResultTabs(false);
						
						e.preventDefault();
					});
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
				},
				
				displayResult: function(result) {
					var $anchor = this.findResultNavTabAnchor(result);
					if ($anchor.length < 1) {
						// create result tab nav
						var statusColor = this.getStatusColor(result.status);
						if (statusColor == 'gray')
							statusColor = 'muted';
						
						$anchor = $('<a href="#' + this.createResultTabId(result) + '"/>')
							.append($('<span class="text-' + statusColor + '"/>').text(result.name))
							.append($('<i class="fa fa-remove close-result"/>'));
						this._$nav.append($('<li class="' + this._resultNavTabCls + '"/>').append($anchor));
						
						// create result tab
						var $tab = $('<div class="tab-pane ' + this._resultTabCls + '" id="' + this.createResultTabId(result) + '"/>').text(result.name);
						this._$content.append($tab);
						
						// TODO - load async result data
						
						if (this.getDisplayedResultsCnt() < 2)
							this.toggleResultTabs(true);
					}
					$anchor.tab('show');
				},
				
				getDisplayedResultsCnt: function() {
					return this._$nav.find('li.' + this._resultNavTabCls).length;
				},
				
				toggleResultTabs: function(show) {
					this._$nav.toggle(!!show);
					this._$box.toggleClass(this._boxCls, !show);
				},
				
				findResultNavTabAnchor: function(result) {
					return this._$nav.find('li.' + this._resultNavTabCls + ' > a[href=#' + this.createResultTabId(result) + ']');
				},
				
				createResultTabId: function(result) {
					return 'result-' + result.id;
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