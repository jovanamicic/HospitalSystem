angular.module('hospitalApp.controllers').controller('LoginController',
		LoginController)

LoginController.$inject = [ '$location', '$stateParams',
		'loginService', 'localStorageService', '$http', '$scope', '$state'];

function LoginController($location, $stateParams, loginService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.personLogin;

	vm.login = function() {
		loginService.loginUser(vm.personLogin)
		.then(function(data, status, headers, config) {
				data = data.data;
			
				localStorage.setItem("token", data.tokenValue);
				localStorage.setItem("person", data.id);
				
				var state = "";
				
				if (data.role.toLowerCase().indexOf("manager") != -1) 
					state = "manager.profile";
				else if(data.role == "medical staff")
					state = "doctorProfile";
				else if(data.role == "patient")
					state = "patientProfile";
				
				setTimeout(function(){
					$state.go(state)
					}, 1000);
		})
	}

	
}
