angular.module('hospitalApp.controllers').controller(
		'MedicalStaffPatientRegistrationController',
		MedicalStaffPatientRegistrationController)

MedicalStaffPatientRegistrationController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope',
		'$state' ];

function MedicalStaffPatientRegistrationController($location, $stateParams,
		medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.patient = {};
	vm.doctors = [{name:"- Izabrani lekar -", id: ""}];
	
	vm.getDoctors= function(){
		medicalStaffService.getDoctors().then(
				function(data){
					var docs = data.data;
					for (i = 0; i < docs.length; i++) { 
						var option = {};
					    option.name = docs[i].name + " " + docs[i].surname;
					    option.id = docs[i].id;
					    vm.doctors.push(option);
					}
				console.log(vm.doctors);
				}).catch(function(data){
					toastr.error("Dogodila se greška.");
				});
	}
	vm.getDoctors();
	
	
	vm.checkEmail = function(){
		medicalStaffService.checkEmail(vm.email).then(
				function(data){
				}).catch(function(data){
					toastr.error("Email adresa se već koristi! Unesite drugu adresu.");
				});
	}
	
	//check if bday is before today
	vm.checkBirthday = function(){
		medicalStaffService.checkBirthday(vm.birthday).then(
				function(data){
				}).catch(function(data){
					toastr.error("Uneli ste neispravan datum!");
				});
	}
	
	vm.checkPID = function(){
		medicalStaffService.checkPID(vm.personalID).then(
				function(data){
				}).catch(function(data){
					toastr.error("JMBG se već koristi!");
				});
	}
	
	vm.addPatient = function(){
		if (vm.name == "" || vm.surname == "" || vm.personalID == ""){
			toastr.error("Niste popunili sva obavezna polja!");
		}
		else {
		vm.patient = {
				 name  : vm.name,
				 surname  : vm.surname,
				 personalID  : vm.personalID,
				 birthday  : vm.birthday,
				 email  : vm.email,
				 gender  : vm.gender,
				 doctor  : vm.doctor,
				 country  : vm.country,
				 city  : vm.city,
				 zipCode  : vm.zipCode,
				 street  : vm.street,
				 number  : vm.number
			}
		console.log(vm.patient);
		medicalStaffService.addPatient(vm.patient).then(
				function(data){
					vm.patient = data;
					$state.go("medicalStaff.patients");
				}).catch(function(data){
					toastr.error("Dogodila se greška.");
				});
		}
	}

}