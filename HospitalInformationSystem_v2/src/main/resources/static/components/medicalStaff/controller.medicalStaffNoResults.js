angular.module('hospitalApp.controllers').controller(
		'MedicalStaffNoResultsController',
		MedicalStaffNoResultsController)

MedicalStaffNoResultsController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope',
		'$state' ];

function MedicalStaffNoResultsController($location, $stateParams,
		medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	
	vm.getSearchData = function(){
		vm.searchData = $stateParams.searchData;
	}
	
	vm.getSearchData();
	
}