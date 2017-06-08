angular.module('hospitalApp.controllers').controller('ManagerController',
		ManagerController)

ManagerController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state'];

function ManagerController($location, $stateParams, managerService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.user = {};
	vm.isActive = false;
	vm.isActivePayments = false;
	vm.isGeneralManager = false;
	
	vm.oldPassword = "";
	vm.newPassword1 = "";
	vm.newPassword2 = "";
	
	
	vm.getProfile = function() {
		managerService.getPersonByToken()
		.then(function(data) {
			data = data.data;
			vm.user = data;
			vm.userFullName = data.name + " " + data.surname;
			
			if (data.photo)
				vm.imgSrc = data.photo;
			else
				vm.imgSrc = "images/avatar.png";
			
			managerService.getRoleByToken()
			.then(function(data) {
				if (data.data.role.toLowerCase() == "general manager")
					vm.isGeneralManager = true;
				else
					vm.isGeneralManager = false;
			}).catch(function(data, status, headers, config) {
				vm.errorMessage = "Error getting person role!";
			});
			
		}).catch(function(data, status, headers, config) {
			vm.errorMessage = "Something went wrong!";
		});
	}
	vm.getProfile();
	
	vm.changeProfile = function() {
		var userUpdate = {username : vm.user.username, email : vm.user.email};
		managerService.updateUser(userUpdate)
		.then(function(data) {
			toastr.success("Vaš profil je uspešno izmenjen.")
		})
		.catch(function() {
			toastr.error("Uneli ste pogrešne podatke. Molimo Vas pokušajte ponovo!");
		});
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
			managerService.changePassword(vm.password).then(function(data){
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
	
	vm.showProifleOptions = function() {
		vm.isActive = !vm.isActive;
	}
	
	vm.showPaymentsOptions = function() {
		vm.isActivePayments = !vm.isActivePayments;
	}
	

	vm.goToProfile = function() {
		$state.go("manager.profile");
	}

	vm.goToChangePassword = function() {
		$state.go("manager.passwordChange");
	}
	
	vm.goToOperations = function() {
		$state.go("manager.operations");
	}
	
	vm.goToExaminations = function() {
		$state.go("manager.examinations");
	}
	
	vm.goToAllPayments = function() {
		$state.go("manager.payments");
	}
	
	vm.goToNewPayment = function() {
		$state.go("manager.newPayment");
	}
}
