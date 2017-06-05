angular.module('hospitalApp.services').factory('operationService', function($http) {
	
	var service = {
			loadOperation : loadOperation,
			getRooms : getRooms,
			getRoomsPage : getRoomsPage,
			getTimes : getTimes,
			updateOperation : updateOperation
	}
	return service;
	
	function loadOperation(id) {
		return $http.get('operations/' + id);
	}
	
	function getRooms() {
		return $http.get('rooms');
	}
	
	function getRoomsPage() {
		return $http.get('rooms/all');
	}
	
	function getTimes(date, roomId) {
		return $http.get('/roomSchedules/available/' + roomId + '/' + date);
	}
	
	function updateOperation(operation) {
		return $http.put('operations/saveTimeAndRoom', operation);
	}

})