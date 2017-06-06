angular.module('hospitalApp.controllers').controller('PaymentsController',
		PaymentsController)

PaymentsController.$inject = [ '$location', '$stateParams',
		'paymentsService', 'localStorageService', '$http', '$scope', '$state'];

function PaymentsController($location, $stateParams, paymentsService, localStorageService, $http, $scope, $state) {

	var vm = this;

}
