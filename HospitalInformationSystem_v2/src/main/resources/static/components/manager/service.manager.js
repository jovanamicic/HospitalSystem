angular.module('hospitalApp.services').factory('managerService', function($http) {
	var service = {
			getUser : getUser,
			getPersonByToken : getPersonByToken,
			updateUser : updateUser ,
			updatePassword : updatePassword
	}
	return service;
	
	function getUser(id) {
		return $http.get('managers/' + id);
	}
	
	function updateUser(user) {
		return $http.put('managers', user);
	}
	
	function updatePassword(passwords) {
		return $http.put('/persons/password', passwords);
	}
	
	function getPersonByToken() {
		return $http.get('persons/personByToken')
	};


})
