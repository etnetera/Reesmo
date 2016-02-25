Reesmo.Panes = Class.extend(function(){
	
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
