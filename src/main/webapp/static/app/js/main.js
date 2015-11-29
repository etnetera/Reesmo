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
				
				_$boxList: null,
				
				_$boxDetail: null,
				
				_$detail: null,
				
				_withDetailCls: 'with-detail',
				
				_detailLoadingCls: 'loading',
				
				_expandedCls: 'expanded',
				
				init: function($box, dtSettings, dtJson) {
					this._$box = $box;
					this._$boxList = this._$box.find('> .results-box-list');
					this._$boxDetail = this._$box.find('> .results-box-detail');
					this._$detail = this._$boxDetail.find('> .result-detail');
					
					var that = this;
					$(dtSettings.nTBody).on('click', 'a.display-result', function(e){
						var $this = $(this);
						$this.blur();
						that.displayResult({
							id: $this.attr('data-result-id'),
							name: $this.text(),
							status: $this.attr('data-result-status')
						});
						e.preventDefault();
					});
					
					this._$boxList.on('click', '.expand-overlay', function(e){
						that.collapseResult();
						e.preventDefault();
					});
					
					this._$detail.on('click', 'i.close-result', function(e){
						that.closeResult();
						e.preventDefault();
					});
					this._$detail.on('click', 'i.expand-result', function(e){
						that.expandResult();
						e.preventDefault();
					});
					this._$detail.on('click', 'i.collapse-result', function(e){
						that.collapseResult();
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
					var that = this;
					this._$detail.html('');
					this._$box.removeClass(this._expandedCls);
					this._$box.addClass(this._detailLoadingCls + ' ' + this._withDetailCls);
					Tremapp.ajax({
						type: 'GET',
						url: Tremapp.baseUrl + 'a/result/detail/' + result.id,
						dataType: 'html'
					}, function(html) {
						that._$detail.html(html);
						that._$box.removeClass(that._detailLoadingCls);
					}, function() {
						that._$box.removeClass(that._detailLoadingCls + ' ' + that._withDetailCls);
					});
				},
				
				closeResult: function() {
					this._$box.removeClass(this._withDetailCls);
				},
				
				expandResult: function() {
					this._$box.addClass(this._expandedCls);
				},
				
				collapseResult: function() {
					this._$box.removeClass(this._expandedCls);
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