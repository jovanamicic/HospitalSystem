angular.module('hospitalApp.services').factory('managerService', function($http) {
	var service = {
			getUser : getUser,
			getPersonByToken : getPersonByToken,
			updateUser : updateUser ,
			updatePassword : updatePassword,
			getAllOperationsPage : getAllOperationsPage,
			getNewOperationsPage : getNewOperationsPage,
			getAllExaminationsPage : getAllExaminationsPage,
			getNewExaminationsPage : getNewExaminationsPage
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
	}
	
	function getAllOperationsPage(page) {
		return $http.get('/operations/all?page=' + page)
	}
	
	function getNewOperationsPage(page) {
		return $http.get('/operations/newOperations?page=' + page)
	}
	
	function getAllExaminationsPage(page) {
		return $http.get('/examinations/all?page=' + page)
	}
	
	function getNewExaminationsPage(page) {
		return $http.get('/examinations/newExaminations?page=' + page)
	}
	
	


})
