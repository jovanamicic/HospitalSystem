angular.module('hospitalApp.controllers').controller(
		'PatientRecordController', PatientRecordController)

PatientRecordController.$inject = [ '$location', '$stateParams',
		'patientService', 'medicalStaffService', 'localStorageService',
		'$http', '$scope', '$state' ];

function PatientRecordController($location, $stateParams, patientService,
		medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.nameSurname = "";
	vm.examinationShow = false;
	vm.showBtns = true;
	vm.asDoctor = false;
	
	vm.getProfile = function(){
		if($stateParams.id != null){
			vm.asDoctor = true;
			patientService.getPatient($stateParams.id).then(
					function(data){
						vm.nameSurname = data.data.name + " "+ data.data.surname;
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
		else{
			vm.asDoctor = false;
			patientService.getLoggedPatient().then(
					function(data){
						vm.nameSurname = data.data.name + " "+ data.data.surname;
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
	
}