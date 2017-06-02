angular.module('hospitalApp.services').factory('managerService', function($http) {
	var service = {
			getUser : getUser,
			updateUser : updateUser 
	}
	return service;
	
	function getUser(id) {
		return $http.get('managers/' + id);
	}
	
	function updateUser(user) {
		return $http.put('managers', user);
	}


})
