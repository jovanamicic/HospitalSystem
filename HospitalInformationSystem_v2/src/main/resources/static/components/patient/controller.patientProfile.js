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
			vm.asDoctor = true; //hide all what doctor does not need
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
		$location.path("medicalStaff/patient/record/"+$stateParams.id);
	}
}