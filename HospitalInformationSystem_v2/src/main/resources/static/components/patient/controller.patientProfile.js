angular.module('hospitalApp.controllers').controller(
		'PatientProfileController',
		PatientProfileController)

PatientProfileController.$inject = [ '$location', '$stateParams',
		'patientService', 'medicalStaffService', 'localStorageService', '$http', '$scope',
		'$state' ];

function PatientProfileController($location, $stateParams,
		patientService, medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.patient = {};
	vm.doctor = "";
	vm.asDoctor = false;
	
	vm.loadPatient = function(){
		if($stateParams.id != null){
			vm.asDoctor = true; // hide all what doctor does not need
			patientService.getPatient($stateParams.id).then(
					function(data){
						vm.patient = data.data;
						patientService.getDoctor(vm.patient.doctor).then(
								function(data){
									var doc = data.data;
									vm.doctor = doc.name + " " + doc.surname;
								}).catch(function(data){
									toastr.error("Dogodila se greška.");
								});
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
		else{
			vm.asDoctor = false;
			patientService.getLoggedPatient().then(
					function(data){
						vm.patient = data.data;
						patientService.getDoctor(vm.patient.doctor).then(
								function(data){
									var doc = data.data;
									vm.doctor = doc.name + " " + doc.surname;
								}).catch(function(data){
									toastr.error("Dogodila se greška.");
								});
					}).catch(function(data){
						toastr.error("Dogodila se greška.");
					});
		}
	}
	vm.loadPatient();

	
	vm.checkUsername = function(){
		patientService.checkUsername(vm.patient.username).then(function(){
					console.log("OK moze ovo");
				}).catch(function(data){
					toastr.error("Korisničko ime se već koristi! Unesite drugo.");
				});
	}
	
	vm.checkEmail = function(){
		medicalStaffService.checkEmail(vm.patient.email).then(
				function(data){
				}).catch(function(data){
					toastr.error("Email adresa se već koristi! Unesite drugu adresu.");
				});
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
		
	}
	
}