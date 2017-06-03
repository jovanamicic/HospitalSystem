angular.module('hospitalApp.controllers').controller('LoginController',
		LoginController)

LoginController.$inject = [ '$location', '$stateParams',
		'loginService', 'localStorageService', '$http', '$scope', '$state'];

function LoginController($location, $stateParams, loginService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.personLogin;

	vm.login = function() {
		loginService.loginPerson(vm.personLogin).then(
				function(data, status, headers, config) {
					localStorage.setItem("token", data.data.tokenValue);
					loginService.getPersonRole()
						.then(function(data){
							vm.personLogin = {};
							localStorage.setItem("role", data.data.role);

							if (data.data.role == 'patient')
								$location.path('/' + data.data.role + '/profile');
							else if (data.data.role == 'medical staff')
								$location.path('/medicalStaff/patients');
							else if (data.data.role == 'manager')
								$location.path('/' + data.data.role + '/profile');
							
							vm.errorMessage = "";
						})
						.catch(function(data){
							vm.errorMessage = data;
						})
				
					
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Wrong email or password, try again!";
		});
	}

}
