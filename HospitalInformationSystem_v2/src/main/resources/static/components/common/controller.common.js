angular.module('hospitalApp.controllers').controller('CommonController',
		CommonController)

CommonController.$inject = [ '$location', '$stateParams',
		'commonService', 'localStorageService', '$http', '$scope', '$state'];

function CommonController($location, $stateParams, commonService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.loggedPerson;
	vm.headerImageSrc = "/images/slika22.jpg";
	vm.personImageSrc;

	getPersonByToken = function() {
		commonService.getPersonByToken().then(
				function(data, status, headers, config) {
					vm.loggedPerson = data.data;
					
					if (data.data.photo)
						vm.personImageSrc = vm.loggedPerson.photo;
					else if (data.data.gender)
						if (data.data.gender == 'Muško')
							vm.personImageSrc = "images/avatar.png";
						else
							vm.personImageSrc = "images/avatarFemale.png";
					else
						vm.personImageSrc = "images/avatar.png";
					
				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with getting logged person!";
		});
	}
	getPersonByToken();
	
	vm.logout = function() {
		localStorage.clear();
		$state.go('login');
	}
	

}
