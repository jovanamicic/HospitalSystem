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
	vm.examinationShow = false;
	vm.showBtns = true;
	vm.asDoctor = false;
	
	vm.operationExamination = {};
	vm.displayModal = "none";
	
	vm.allRecords;
	vm.displayExaminations = true;
	
	vm.currentPage = 1;
	vm.pageSize = 10;
	vm.totalPages;
	
	vm.examinationsBtnActive;
	vm.operationsBtnActive;
	
	
	vm.getProfile = function(){
		if($stateParams.id != null){
			vm.asDoctor = true;
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
			vm.asDoctor = false;
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
	
	vm.startExamination = function(){
		vm.examinationShow = true;
		vm.showBtsn = false;
	};
	
	vm.saveExamination = function(){
		vm.examinationShow = false;
		vm.showBtsn = true;
		
		 if (vm.symptoms == "" || vm.diagnosis == "" || vm.therapy == ""){
		    	toastr.error("Niste popunili sva polja!");
		 }
		 else {
			 vm.examination = {
					 patientID : $stateParams.id,
					 symptons : vm.symtoms,
					 diagnosis : vm.diagnosis,
					 therapy : vm.therapy
			 }
		    	patientService.saveExamination(vm.examination).then(function(data){
		    		toastr.info("Pregled je sačuvan!");
		    	}).catch(function(data){
		    		toastr.error("Dogodila se greška.");
		    	});
		 }
	};

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
	
				 }).catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving operation!";
				 });
			 }
			 else if(vm.operationExamination.type == "Pregled"){
				 medicalStaffService.saveExamination(vm.operationExamination).then(function(data, status, headers, config) {
					 toastr.info("Pregled je zakazan za datum " + vm.operationExamination.date);
					 vm.closeModal();
								
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
		
		if($stateParams.id != null){
			patientService.getExaminationsPage(newPage, $stateParams.id)
			.then(function(result) {
				vm.allRecords = result.data.content;
				vm.totalPages = result.data.totalPages;
				vm.totalRecords = result.data.totalElements;
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
				vm.allRecords = result.data.content;
				vm.totalPages = result.data.totalPages;
				vm.totalRecords = result.data.totalElements;
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
		if($stateParams.id != null){
			patientService.getOperationsPage(newPage, $stateParams.id)
			.then(function(result) {
				vm.allRecords = result.data.content;
				vm.totalPages = result.data.totalPages;
				vm.totalRecords = result.data.totalElements;
				vm.displayOperationsExaminations = false;
				
				vm.examinationsBtnActive = false;
				vm.operationsBtnActive = true;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin operations page.";
			});
		} else {
			patientService.getMyOperationsPage(newPage)
			.then(function(result) {
				vm.allRecords = result.data.content;
				vm.totalPages = result.data.totalPages;
				vm.totalRecords = result.data.totalElements;
				vm.displayExaminations = false;
				
				vm.examinationsBtnActive = false;
				vm.operationsBtnActive = true;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin operations page.";
			});
		}
	}
	
	vm.changePage = function() {
		var newPage = vm.currentPage - 1;
		
		if (vm.displayExaminations)
			vm.getExaminationsPage(newPage);
		else
			vm.getOperationsPage(newPage);
	}
}