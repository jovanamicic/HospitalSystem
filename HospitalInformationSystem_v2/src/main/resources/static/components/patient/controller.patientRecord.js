angular.module('hospitalApp.controllers').controller(
		'PatientRecordController', PatientRecordController)

PatientRecordController.$inject = [ '$location', '$stateParams',
		'patientService', 'medicalStaffService', 'localStorageService',
		'$http', '$scope', '$state' ];

function PatientRecordController($location, $stateParams, patientService,
		medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;

}