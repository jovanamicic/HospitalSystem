angular.module('hospitalApp.controllers').controller('MedicalStaffPatientsController',
		MedicalStaffPatientsController)

MedicalStaffPatientsController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope', '$state'];

function MedicalStaffPatientsController($location, $stateParams, medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.patients;
	
	vm.allPatientsBtnActive;
	vm.myPatientsBtnctive;
	
	vm.noSearchResult;
	
	vm.searchData;
	

	vm.getAllPatients = function() {
		medicalStaffService.getPatients().then(
				function(data, status, headers, config) {
					vm.patients = [];
					vm.patients = data.data.content;
					
					vm.allPatientsBtnActive = true;
					vm.myPatientsBtnctive = false;
					
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with getting all patients!";
		});
	}
	vm.getAllPatients();
	
	
	vm.getMyPatients = function() {
		medicalStaffService.getMyPatients().then(
				function(data, status, headers, config) {
					vm.patients = [];
					vm.patients = data.data.content;
					
					vm.allPatientsBtnActive = false;
					vm.myPatientsBtnctive = true;
					
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with getting my patients!";
		});
	}
	
	vm.searchPatients  = function() {
		medicalStaffService.getPatientsBySearchData(vm.searchData).then(
				function(data, status, headers, config) {
					vm.patients = [];
					vm.patients = data.data.content;
					
					vm.allPatientsBtnActive = true;
					vm.myPatientsBtnctive = false;
					
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with searching patients!";
		});
	}
	
	vm.emptySearch = function() {
		if (vm.searchData.length == 0)
			vm.getAllPatients();
	}
	
	vm.showPatient = function(id) {
		$location.path('/patient/profile/'+id);
	}
	
}
