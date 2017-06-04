angular.module('hospitalApp.controllers').controller('MedicalStaffController',
		MedicalStaffController)

MedicalStaffController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope', '$state'];

function MedicalStaffController($location, $stateParams, medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	
	//switch state for medical staff
	vm.goToPatients = function() {
		$state.go("medicalStaff.patients");
	}

	vm.goToPatientRegistration = function() {
		$state.go("medicalStaff.patientRegistration");
	}
	
	vm.goToSchedule = function() {
		$state.go("medicalStaff.schedule");
	}
	
	vm.logout = function() {
		localStorage.clear();
		$state.go('login');
	}
}
