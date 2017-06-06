angular.module('hospitalApp.controllers').controller(
		'PatientController', PatientController)

PatientController.$inject = [ '$location', '$stateParams',
		'patientService', 'medicalStaffService', 'localStorageService',
		'$http', '$scope', '$state' ];

function PatientController($location, $stateParams, patientService,
		medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;

	vm.goToProfile = function() {
		$state.go("patient.profile");
	}
	
	vm.goToChangePassword = function() {
		$state.go("patient.changePassword");
	}

	vm.goToRecord = function() {
		$state.go("patient.record");
	}
	
	vm.goToSchedule = function() {
		$state.go("patient.schedule");
	}
	
	vm.logout = function() {
		localStorage.clear();
		$state.go('login');
	}
}