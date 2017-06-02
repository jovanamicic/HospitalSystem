angular.module('hospitalApp.controllers').controller('ManagerController',
		ManagerController)

ManagerController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state'];

function ManagerController($location, $stateParams, managerService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.user;
	vm.isActive = false;
	vm.isGeneralManager = false;
	
	vm.getProfile = function() {
		var id = localStorage.getItem("person")
		managerService.getUser(id)
		.then(function(data) {
			data = data.data;
			vm.user = data;
			vm.userFullName = data.name + " " + data.surname;
			
			if (data.photo)
				vm.imgSrc = data.photo;
			else
				vm.imgSrc = "images/avatar.png";
			
			if (data.role.toLowerCase() == "general")
				vm.isGeneralManager = true;
			else
				vm.isGeneralManager = false;
			
		}).catch(function(data, status, headers, config) {
			vm.errorMessage = "Something went wrong!";
		});
	}
	vm.getProfile();
	
	vm.changeProfile = function() {
		var userUpdate = {username : vm.user.username, email : vm.user.email};
		managerService.updateUser(userUpdate)
		.then(function(data) {
			console.log("OK")
			toastr.success("Vaš profil je uspešno izmenjen.")
		})
		.catch(function() {
			console.log("NOT OK")
			toastr.error("Uneli ste pogrešne podatke. Molimo Vas pokušajte ponovo!");
		});
	}
	
	vm.logout = function() {
		localStorage.clear();
		$state.go('login');
	}
	
	vm.showProifleOptions = function() {
		vm.isActive = !vm.isActive;
	}
}
