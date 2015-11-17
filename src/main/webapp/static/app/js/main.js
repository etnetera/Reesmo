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
}

Tremapp.getSelectedRowsData = function($table) {
	return $table.DataTable().rows('.selected').data();
}