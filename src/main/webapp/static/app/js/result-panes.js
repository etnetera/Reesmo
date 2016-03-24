Reesmo.ResultListPanes = Reesmo.Panes.extend(function(){
	
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
			if (!Reesmo.dataTables.isSelectable(that.$table) || !Reesmo.dataTables.isRow($this)) return;
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
				Reesmo.dataTables.toggleRowSelection($this, that.dtSettings.nTBody, null, e);
				return;
			}
			$resultA = isResultA ? $target : $target.parents('tr:first').find('a.display-result');
			if (isResultA) $target.blur();
			
			that.displayRightPane({
				id: $resultA.attr('data-result-id'),
				name: $resultA.text(),
				status: $resultA.attr('data-result-status')
			});
			if (!isRemovingEnabled) Reesmo.dataTables.toggleOneRowSelection($this, that.$table);			
		});
		
		if (this.$deleteToggler.length > 0) {
			this.$deleteToggler.change(function(e){
				Reesmo.dataTables.clearRowSelection(that.$table);
				that.$table.toggleClass('remove', $(this).is(':checked'));
				that.$deleteTrigger.toggle();
			});
			
			var confirmText = this.$deleteTrigger.attr('data-confirm-text');
			this.$deleteTrigger.click(function(){
				var rowsData = Reesmo.dataTables.getSelectedRowsData(that.$table),
					ids = [];
				
				$.each(rowsData, function(i, rowData){
					ids.push(rowData.id);
				});
				if (ids.length < 1 || !confirm(confirmText.replace('{0}', ids.length))) return;
				
				if (that.resultId != null && ids.indexOf(that.resultId) > -1 && that.isRightPaneOpened()) {
					that.closeRightPane();
				}
				
				Reesmo.ajax({
					url: Reesmo.baseUrl + 'results/delete/',
					data: {
						resultIds: ids
					}
				}, function(removedCnt){
					Reesmo.dataTables.clearRowSelection(that.$table);
					Reesmo.dataTables.reloadTable(that.$table);
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
		
		Reesmo.ajax({
			type: 'GET',
			url: Reesmo.baseUrl + 'a/result/detail/' + result.id,
			dataType: 'html'
		}, function(html) {
			that.$detail.html(html);
			that.childPanes = new Reesmo.ResultDetailPanes(that.$detail, result.id, that);
			that.childPanes.init();
			that.$detail.removeClass(that.detailLoadingCls);
		}, function() {
			that.$detail.removeClass(that.detailLoadingCls);
			that.closeRightPane();
		});
	};
	
	this.closeRightPane = function() {
		this.super.closeRightPane();
		if (!this.isRemovingEnabled()) Reesmo.dataTables.clearRowSelection(this.$table);
	};
	
	this.minimize = function() {
		this.super.minimize();
		if (!this.isRemovingEnabled()) Reesmo.dataTables.clearRowSelection(this.$table);
	};
	
	this.isRemovingEnabled = function() {
		return this.$table.hasClass('remove');
	};
	
	this.reloadTable = function() {
		Reesmo.dataTables.reloadTable(this.$table);
	};
	
});

Reesmo.ResultDetailPanes = Reesmo.Panes.extend(function(){
	
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
			
				Reesmo.ajax({
					url: Reesmo.baseUrl + 'results/delete/',
					data: {
						resultIds: [that.resultId]
					}
				}, function(removedCnt){
					if (removedCnt > 0) {
						if (that.parentPanes) {
							that.parentPanes.closeRightPane();
							that.parentPanes.reloadTable();
						} else {
							window.location.replace(Reesmo.baseUrl + 'project/home/' + that.projectId);
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
		
		if (kind == 'image' || kind == 'text' || kind == 'html') {
			this.$attBody.html('');
			this.$attBody.addClass(this.attLoadingCls);
			this.$attBody.attr('data-att-kind', kind);
			$header.find('.attachment-name').text($att.attr('data-att-name'));
			$header.find('.attachment-lnk').text(path).attr('href', $viewLnk.attr('href'));
			this.super.displayRightPane();
			
			if (kind == 'image') {
				this.$attBody.html('').append($('<img class="result-att-img"/>').attr('src', $viewLnk.attr('href')));
				that.$attBody.removeClass(this.attLoadingCls);
			} else {
				Reesmo.ajax({
					type: 'GET',
					url: Reesmo.baseUrl + 'a/result/attachment/view/' + this.resultId + '/' + path,
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
