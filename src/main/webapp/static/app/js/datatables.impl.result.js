Tremapp.dataTables.impl.result = {
			
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
	
};
