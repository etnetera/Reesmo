Tremapp.initCompleteSelectableRowCallback = function(settings, json) {
	$(settings.nTBody).on('click', 'tr', function(){
		var $this = $(this);
		if ($this.hasClass('selected')) {
			$this.removeClass('selected');
		} else {
			$(settings.nTBody).find('tr.selected').removeClass('selected');
			$this.addClass('selected');
		}
	});
};

Tremapp.initCompleteSelectableRowsCallback = function(settings, json) {
	$(settings.nTBody).on('click', 'tr', function(){
		$(this).toggleClass('selected');
	});
};

Tremapp.getSelectedRowData = function($table) {
	return $table.DataTable().row('.selected').data();
};

Tremapp.getSelectedRowsData = function($table) {
	return $table.DataTable().rows('.selected').data();
};

Tremapp.clearRowSelection = function($table) {
	return $table.find('tbody tr.selected').removeClass('selected');
};

Tremapp.reloadTable = function($table) {
	$table.DataTable().ajax.reload();
};

Tremapp.ajax = function(options, successCallback, errorCallback) {
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
};