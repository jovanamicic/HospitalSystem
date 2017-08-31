angular.module('hospitalApp.controllers').controller(
		'PatientProfileController',
		PatientProfileController)

PatientProfileController.$inject = [ '$location', '$stateParams',
		'patientService', 'medicalStaffService', 'commonService', 'localStorageService', '$http', '$scope',
		'$state' ];

function PatientProfileController($location, $stateParams,
		patientService, medicalStaffService, commonService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.patient = {};
	vm.doctor = "";
	vm.asDoctor = false;
	vm.showRecord = false;
	vm.startExaminationShow = false;
	
	vm.operationExamination = {};
	vm.displayModal = "none";
	
	vm.patientNameSurname;
	
	vm.loadPatient = function(){
		if($stateParams.id != null){
			vm.asDoctor = true; // hide all what doctor does not need
			patientService.getPatient($stateParams.id).then(
					function(data){
						vm.patient = data.data;
						vm.patientNameSurname = data.data.name + " " + data.data.surname;
						if (vm.patient.doctor != -1) {
							patientService.getPerson(vm.patient.doctor).then(
								function(data){
									var doc = data.data;
									vm.doctor = doc.name + " " + doc.surname;
									//check if chosen doctor is logged
									commonService.getPersonByToken().then(
											function(data, status, headers, config) {
												vm.loggedPerson = data.data;
												if (vm.loggedPerson.username == doc.username){
													vm.showRecord = true;
												}
												else {
													vm.showRecord = false;
												}
											}).catch(function(data, status, headers, config) {
												console.log("Dogodila se greška.");
									});
								}).catch(function(data){
									toastr.error("Dogodila se greška.");
								});
						} else {
							vm.showRecord = true;
							vm.doctor = "Nema izabranog lekara.";
						}
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
		else{
			vm.asDoctor = false;
			patientService.getLoggedPatient().then(
					function(data){
						vm.patient = data.data;
						if (vm.patient.doctor != -1) {
							patientService.getPerson(vm.patient.doctor).then(
								function(data){
									var doc = data.data;
									vm.doctor = doc.name + " " + doc.surname;
									vm.showRecord = true;
								}).catch(function(data){
									toastr.error("Dogodila se greška.");
								});
						} else {
							vm.doctor = "Nema izabranog lekara."
						}
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
	}
	vm.loadPatient();
	
	vm.checkUsername = function(){
		
		if(vm.patient.username != ""){
			patientService.checkUsername(vm.patient.username).then(function(){
						console.log("OK moze ovo");
					}).catch(function(data){
						toastr.error("Korisničko ime se već koristi! Unesite drugo.");
					});
		}
	}
	
	vm.checkEmail = function(){
		if (vm.patient.email != "" || vm.patient.email != null){
		medicalStaffService.checkEmail(vm.patient.email).then(
				function(data){
				}).catch(function(data){
					toastr.error("Email adresa se već koristi! Unesite drugu adresu.");
				});
		}
	}
	
	vm.changeProfile = function(){
		if (vm.patient.username == "" || vm.patient.email == ""){
			toastr.error("Unesite sva obavezna polja.")
		}
		else{
			patientService.updatePatient(vm.patient).then(
					function(data){
						toastr.success("Vaš profil je uspešno izmenjen.");
					}).catch(function(data){
						toastr.error("Uneli ste pogrešne podatke. Molimo Vas pokušajte ponovo!");
					});
		}
	}
	
	vm.patientRecord = function(){
		if ($stateParams.id != null){
			$location.path("medicalStaff/patient/record/"+$stateParams.id);
		}
		else{
			$state.go("patient.record");
		}
	}
	
	vm.checkNewPassword = function() {
		if (vm.newPassword.length < 8){
			vm.passwordLenght = true;
		}
		else
			vm.passwordLenght = false;
		
		if ((/[A-Z]/).test(vm.newPassword))
			vm.passwordUpperCase = false;
		else{
			vm.passwordUpperCase = true;
		}
		
		if (vm.newPassword.match(/\d+/g) == null)
			vm.passwordNumber = true;
		else{
			vm.passwordNumber = false;
		}
		
		if (!/[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?]/g.test(vm.newPassword))
			vm.passwordSpecialChar = true;
		else{
			vm.passwordSpecialChar = false;
		}
	}
	
	vm.checkRePassword = function() {
		if(vm.newPassword != vm.newRPassword)
			vm.newRePasswordWrong = true;
		else
			vm.newRePasswordWrong = false;
	}
	
	vm.changePassword = function(){
		if (vm.oldPassword == "" || vm.oldPassword == null)
			vm.oldPasswordEmpty = true;
		
		if (vm.newPassword == "" || vm.newPassword == null)
			vm.passwordLenght = true;
		
		if (vm.newPassword != vm.newRPassword )
			vm.newRePasswordWrong = true;
		
		if (vm.newPassword == vm.newRPassword && vm.oldPassword != "" && vm.newPassword != "" && vm.newRPassword != "" && vm.passwordLenght == false && vm.passwordUpperCase == false && vm.passwordNumber == false && vm.passwordSpecialChar == false){
			vm.password = {
					oldPassword : vm.oldPassword,
					newPassword : vm.newPassword
			};
			patientService.changePassword(vm.password).then(function(data){
				toastr.success("Vaša lozinka je uspešno izmenjena.");
				vm.clear();
			}).catch(function(data){
				vm.oldPasswordWrong = true;
			});
		}
		else{
			toastr.error("Ispravite greške!");
		}
		
	}
	
	vm.clear = function() {
		vm.oldPassword = "";
		vm.newPassword = "";
		vm.newRPassword = "";
		
		vm.oldPasswordEmpty = false;
		vm.newRePasswordWrong = false;
		vm.passwordLenght = false;
		vm.passwordUpperCase = false;
		vm.passwordNumber = false;
		vm.passwordSpecialChar = false;
		vm.wrongDateFormat = false;
	}
	
	vm.startExamination = function(){
		vm.startExaminationShow = true;
	}
	
	vm.saveExamination = function(){
		vm.startExaminationShow = false;
		 if (vm.symptons == "" || vm.diagnosis == "" || vm.therapy == ""){
		    	toastr.error("Niste popunili sva polja!");
		 }
		 else {
			 vm.examination = {
					 patientID : $stateParams.id,
					 symptons : vm.symptons,
					 diagnosis : vm.diagnosis,
					 therapy : vm.therapy
			 }
		    	patientService.saveExamination(vm.examination).then(function(data){
		    		toastr.info("Pregled je sačuvan!");
		    		vm.symptons = "";
		    		vm.diagnosis = "";
		    		vm.therapy = "";
		    	}).catch(function(data){
		    		toastr.error("Dogodila se greška.");
		    	});
		 }
	};
	
	vm.resetEx = function(){
		vm.symptons = "";
		vm.diagnosis = "";
		vm.therapy = "";
		vm.startExaminationShow = false;
	}
	
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
		vm.clear();
	}
	
	
	vm.save = function() {
		 var ok = checkInputs();
		 vm.operationExamination.personalId = vm.patient.personalID;
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
					vm.wrongDateFormat = false;
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
			 else if (vm.operationExamination.duration != parseInt(vm.operationExamination.duration, 10)){
				 vm.wrongDuration = true;
				 return false;
			 	}
		 }
		 if (vm.operationExamination.date == null){
			 vm.wrongDate = true;
			 return false;
		 }
		 if (vm.wrongDateFormat == true){
			 return false;
		 }
		 return true;
	 }
	
}