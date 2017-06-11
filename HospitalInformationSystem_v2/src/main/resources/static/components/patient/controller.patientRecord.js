angular.module('hospitalApp.controllers').controller(
		'PatientRecordController', PatientRecordController)

PatientRecordController.$inject = [ '$location', '$stateParams',
		'patientService', 'medicalStaffService', 'localStorageService',
		'$http', '$scope', '$state' ];

function PatientRecordController($location, $stateParams, patientService,
		medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.nameSurname = "";
	vm.personalId;
	
	
	vm.examinations;
	vm.operations;
	
	vm.itemsPerPage = 10;
	vm.pageSize = 10;
	vm.displayExaminations = true;
	
	vm.currentPageExaminations = 1;
	vm.currentPageOperations = 1;
	
	vm.totalPagesExaminations;
	vm.totalPagesOperations;
	
	vm.examinationsBtnActive;
	vm.operationsBtnActive;
	
	
	vm.getProfile = function(){
		if($stateParams.id != null){
			patientService.getPatient($stateParams.id).then(
					function(data){
						vm.personalId = data.data.personalID;
						vm.nameSurname = data.data.name + " "+ data.data.surname;
						vm.patientId = data.data.id;
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
		else{
			patientService.getLoggedPatient().then(
					function(data){
						vm.nameSurname = data.data.name + " "+ data.data.surname;
						vm.patientId = data.data.id;
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
	}
	vm.getProfile();
	

	vm.goToPatientProfile = function(){
			if ($stateParams.id != null){
				$location.path("medicalStaff/patient/profile/"+$stateParams.id);
			}
			else{
				$state.go("patient.profile");
			}
	};
	
	
	
	
	vm.getExaminationsPage = function(newPage){
		
		if (newPage == 0)
			vm.currentPageExaminations = 1;
		
		if($stateParams.id != null){
			patientService.getExaminationsPage(newPage, $stateParams.id)
			.then(function(result) {
				vm.examinations = result.data.content;
				vm.totalPagesExaminations = result.data.totalPages;
				vm.displayExaminations = true;
				
				vm.examinationsBtnActive = true;
				vm.operationsBtnActive = false;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin examinations page.";
			});
		} else {
			patientService.getMyExaminationsPage(newPage)
			.then(function(result) {
				vm.examinations = result.data.content;
				vm.totalPagesExaminations = result.data.totalPages;
				vm.displayExaminations = true;
				
				vm.examinationsBtnActive = true;
				vm.operationsBtnActive = false;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin examinations page.";
			});
		}
	}
	vm.getExaminationsPage(0);
	
	vm.getOperationsPage = function(newPage){
		
		if (newPage == 0)
			vm.currentPageOperations = 1;
		
		if($stateParams.id != null){
			patientService.getOperationsPage(newPage, $stateParams.id)
			.then(function(result) {
				vm.operations = result.data.content;
				vm.totalPagesOperations = result.data.totalPages;
				vm.displayExaminations = false;
				
				vm.examinationsBtnActive = false;
				vm.operationsBtnActive = true;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin operations page.";
			});
		} else {
			patientService.getMyOperationsPage(newPage)
			.then(function(result) {
				vm.operations = result.data.content;
				vm.totalPagesOperations = result.data.totalPages;
				vm.displayExaminations = false;
				
				vm.examinationsBtnActive = false;
				vm.operationsBtnActive = true;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin operations page.";
			});
		}
	}
	
	vm.changePageExaminations = function() {
		vm.getExaminationsPage(vm.currentPageExaminations - 1);
	}
	
	vm.changePageOperations = function() {
		vm.getOperationsPage(vm.currentPageOperations - 1);
	}
	
	vm.showExamination = function(examinationId) {
		if($stateParams.id == null)
			$state.go('patient.examination', {id: examinationId, isManager: false})
		else
			$state.go('medicalStaff.examination', {id: examinationId, isManager: false})
	}
	
	vm.showOperation = function(operationId) {
		if($stateParams.id == null)
			$state.go('patient.operation', {id: operationId, isManager: false})
		else
			$state.go('medicalStaff.operation', {id: operationId, isManager: false})
	}
}