angular.module('hospitalApp.controllers').controller('MedicalStaffPatientsController',
		MedicalStaffPatientsController)

MedicalStaffPatientsController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope', '$state'];

function MedicalStaffPatientsController($location, $stateParams, medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.patients;
	vm.total;

	getAllPatients = function() {
		medicalStaffService.getPatients().then(
				function(data, status, headers, config) {
					vm.patients = data.data.content;
					vm.total = 100; //ovde promeni da sa backa dolazi objekat
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with getting all patients!";
		});
	}
	getAllPatients();
	
	vm.pagination = {
			current : 1
		}
		
	vm.itemsPerPage = 6;

}
