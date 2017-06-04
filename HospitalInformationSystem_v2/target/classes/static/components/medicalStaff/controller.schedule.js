angular.module('hospitalApp.controllers').controller('MedicalStaffScheduleController',
		MedicalStaffScheduleController)

MedicalStaffScheduleController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope', '$state'];

function MedicalStaffScheduleController($location, $stateParams, medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	
	vm.showModal = false;
	vm.showModalDetails = false;
	
}
