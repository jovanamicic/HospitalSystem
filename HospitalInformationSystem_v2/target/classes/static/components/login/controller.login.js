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
							
							var role = data.data.role;
							localStorage.setItem("role", role);
							
							if (role == 'patient')
								$location.path('/patient/profile');
							else if (role == 'medical staff')
								$location.path('/medicalStaff/patients');
							else if (role == 'general manager' || role == 'financial manager')
								$location.path('/manager/profile');
							
							vm.errorMessage = "";
						})
						.catch(function(data){
							vm.errorMessage = data;
						})
				
					
				}).catch(function(data, status, headers, config) {
					toastr.error("Pogrešno korisničko ime ili lozinka. Molimo Vas pokušajte ponovo!");
					vm.errorMessage = "Wrong email or password, try again!";
		});
	}

}
