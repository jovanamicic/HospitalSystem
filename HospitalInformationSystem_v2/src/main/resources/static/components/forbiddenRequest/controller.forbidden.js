angular.module('hospitalApp.controllers').controller('ForbiddenController',
		ForbiddenController)

ForbiddenController.$inject = [ '$location', '$stateParams', 'localStorageService', '$http', '$scope', '$state'];

function ForbiddenController($location, $stateParams, localStorageService, $http, $scope, $state) {

	var vm = this;

	vm.gotoLogin = function() {
		localStorage.clear();
		$state.go('login');
	}

}
