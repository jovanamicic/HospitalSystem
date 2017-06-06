angular.module('hospitalApp.controllers').controller('PaymentsController',
		PaymentsController)

PaymentsController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state'];

function PaymentsController($location, $stateParams, managerService, localStorageService, $http, $scope, $state) {

	var vm = this;
	
	vm.allPayments;
	vm.totalPayments;
	vm.itemsPerPage = 1;
	
	vm.currentPage = 1;
	vm.pageSize = 1;
	vm.totalPages;


	vm.getAllPaymentsPage = function(newPage) {
		managerService.getAllPaymentsPage(newPage)
		.then(function(result) {
			vm.allPayments = result.data.content;
			vm.totalPages = result.data.totalPages;
			vm.totalPayments = result.data.totalElements;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operations page.";
		});
	}
	vm.getAllPaymentsPage(0);
	
	vm.changePage = function() {
		var newPage = vm.currentPage - 1;
		vm.getAllPaymentsPage(newPage);
	}
	

}
