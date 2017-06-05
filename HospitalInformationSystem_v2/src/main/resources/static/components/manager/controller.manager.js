angular.module('hospitalApp.controllers').controller('ManagerController',
		ManagerController)

ManagerController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state'];

function ManagerController($location, $stateParams, managerService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.user = {};
	vm.isActive = false;
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
	
	vm.changePassword = function() {
		
		if (vm.newPassword1 == vm.newPassword2 && vm.oldPassword != "" && vm.newPassword1 != "" && vm.newPassword2 != ""){
			
			var passwordUpdate = {oldPassword : vm.oldPassword, newPassword : vm.newPassword};
			managerService.updatePassword(passwordUpdate)
			.then(function(data) {
				toastr.success("Vaš profil je uspešno izmenjen.")
			})
			.catch(function() {
				toastr.error("Uneli ste pogrešnu staru lozinku. Molimo Vas pokušajte ponovo!");
			});
		}
		else{
			toastr.error("Dogodila se greška, molimo Vas pokušajte ponovo!");
		}
		
		vm.oldPassword = "";
		vm.newPassword1 = "";
		vm.newPassword2 = "";
	}
	
	vm.showProifleOptions = function() {
		vm.isActive = !vm.isActive;
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
	
	vm.goToPayments = function() {
		$state.go("manager.payments");
	}
}
