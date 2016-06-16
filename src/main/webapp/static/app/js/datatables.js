Reesmo.dataTables = {
		
	initCompleteSelectableRowCallback: function(settings, json) {
		$(settings.nTBody).on('click', 'tr', function(){
			var $this = $(this);
			if (!Reesmo.dataTables.isSelectable(settings.nTable) || !Reesmo.dataTables.isRow($this)) return;
			if (e && e.target && $(e.target).is('a')) return;
			Reesmo.dataTables.toggleOneRowSelection($this, settings.nTBody);
		});
	},
	
	initCompleteSelectableRowsCallback: function(settings, json) {
		$(settings.nTBody).on('click', 'tr', function(e){
			var $this = $(this);
			if (!Reesmo.dataTables.isSelectable(settings.nTable) || !Reesmo.dataTables.isRow($this)) return;
			if (e && e.target && $(e.target).is('a')) return;
			Reesmo.dataTables.toggleRowSelection($this, settings.nTBody, null, e);
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
		return '<a href="' + Reesmo.baseUrl + uri + '">' + (name == null ? uri : name) + '</a>';
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
				filters = new Reesmo.DataTablesFilters(id, initialData || {});
				this.instances[id] = filters;
			}
			return filters;
		}
	},
	
	/**
	 * Holds specific datatables implementations
	 */
	impl: {}
	
};
