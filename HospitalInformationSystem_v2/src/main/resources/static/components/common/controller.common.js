angular.module('hospitalApp.controllers').controller('CommonController',
		CommonController)

CommonController.$inject = [ '$location', '$stateParams',
		'commonService', 'localStorageService', '$http', '$scope', '$state'];

function CommonController($location, $stateParams, commonService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.loggedPerson;

	getPersonByToken = function() {
		commonService.getPersonByToken().then(
				function(data, status, headers, config) {
					vm.loggedPerson = data.data;
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with getting logged person!";
		});
	}
	getPersonByToken();

}
