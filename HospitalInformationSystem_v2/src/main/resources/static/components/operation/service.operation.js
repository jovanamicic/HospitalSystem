angular.module('hospitalApp.services').factory('operationService', function($http) {
	
	var service = {
			loadOperation : loadOperation
	}
	return service;
	
	function loadOperation(id) {
		return $http.get('operations/' + id);
	}

})