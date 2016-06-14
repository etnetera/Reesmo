Reesmo.goBack = function() {
	window.history.back();
};

Reesmo.i18n.get = function(key) {
	var value = this.values[key];
	return value == null ? ('_' + key + '_') : value;
};

Reesmo.ajax = function(options, successCallback, errorCallback) {
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
			window.location.replace(Reesmo.baseUrl);
		},
		403 : function(status, JSON, xhr, textStatus, errorThrown) {
			alert('Error: This operation is forbidden for you!');
		}
	};
	
	var data = {};
	data[Reesmo.csrfName] = Reesmo.csrfValue;
	
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
};

$.fn.addHiddenInputData = function(data) {          
    var keys = {};          
    var addData = function(data, prefix) {          
        for (var key in data) {
            var value = data[key];
            if (!prefix) {
              var nprefix = key;                                            
            } else {
              var nprefix = prefix + '[' + key + ']';
            }
            if (typeof(value) == 'object') {                                    
                addData(value, nprefix);
                continue;
            }
            keys[nprefix] = value;
        }          
    }          
    addData(data);          
    var $form = $(this);      
    for(var k in keys) {
        $form.addHiddenInput(k, keys[k]);
    }
};

$.fn.addHiddenInput = function(key, value) {      
    var $input = $('<input type="hidden" name="' + key + '" />');
    $input.val(value);
    $(this).append($input);
};

$('[data-toggle="tooltip"]').tooltip();