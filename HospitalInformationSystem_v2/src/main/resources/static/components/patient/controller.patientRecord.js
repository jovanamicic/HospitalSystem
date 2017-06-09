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
	
	vm.operationExamination = {};
	vm.displayModal = "none";
	
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
	
	
	vm.openModalOperation = function() {
		vm.displayModal = "block";
		vm.operationExamination.type = "Operacija"
	}
	
	vm.openModalExamination = function() {
		vm.displayModal = "block";
		vm.operationExamination.type = "Pregled"
	}
	
	vm.closeModal = function() {
		vm.displayModal = "none";
		vm.operationExamination = {};
	}
	
	
	vm.save = function() {
		 var ok = checkInputs();
		 vm.operationExamination.personalId = vm.personalId;
		 if(ok){
			 if(vm.operationExamination.type == "Operacija"){
				 medicalStaffService.saveOperation(vm.operationExamination).then(function(data, status, headers, config) {
					 toastr.info("Operacija je zakazana za datum " + vm.operationExamination.date);
					 vm.closeModal();
					 vm.getExaminationsPage(0);
	
				 }).catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving operation!";
				 });
			 }
			 else if(vm.operationExamination.type == "Pregled"){
				 medicalStaffService.saveExamination(vm.operationExamination).then(function(data, status, headers, config) {
					 toastr.info("Pregled je zakazan za datum " + vm.operationExamination.date);
					 vm.closeModal();
					 vm.getExaminationsPage(0);
								
				 }).catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving exmination!";
				 });
			 }
		 }
	 }
	
	
	vm.checkDate = function(){
		medicalStaffService.checkDate(vm.operationExamination.date).then(
				function(data){
				}).catch(function(data){
					vm.wrongDateFormat = true;
				});
	}
	
	function checkInputs() {
		if (vm.operationExamination.name == null){
			 vm.wrongName = true;
			 return false;
		 }
		 if (vm.operationExamination.type == "Operacija"){
			 if (vm.operationExamination.duration == null){
				 vm.wrongDuration = true;
				 return false;
			 	}
		 }
		 if (vm.operationExamination.date == null){
			 vm.wrongDate = true;
			 return false;
		 }
		 return true;
	 }
	
	
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
}