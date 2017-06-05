angular.module('hospitalApp.controllers').controller(
		'ManagerOperationsController', ManagerOperationsController)

ManagerOperationsController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state' ];

function ManagerOperationsController($location, $stateParams, managerService,
		localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.allOperations;
	vm.totalOperations;
	vm.itemsPerPage = 10;
	vm.displayOnlyNew = true;
	
	vm.currentPage = 1;
	vm.pageSize = 10;
	vm.totalPages;


	vm.getAllOperationsPage = function(newPage) {
		managerService.getAllOperationsPage(newPage)
		.then(function(result) {
			vm.allOperations = result.data.content;
			vm.totalPages = result.data.totalPages;
			vm.totalOperations = result.data.totalElements;
			vm.displayOnlyNew = false;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operations page.";
		});
	}
	
	vm.getNewOperationsPage = function(newPage) {
		managerService.getNewOperationsPage(newPage)
		.then(function(result) {
			vm.allOperations = result.data.content;
			vm.totalPages = result.data.totalPages;
			vm.totalOperations = result.data.totalElements;
			vm.displayOnlyNew = true;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operations page.";
		});
	}
	vm.getNewOperationsPage(0);
	
	vm.changePage = function() {
		var newPage = vm.currentPage - 1;
		
		if (vm.displayOnlyNew)
			vm.getNewOperationsPage(newPage);
		else
			vm.getAllOperationsPage(newPage);
	}
	
	
	vm.showOperation = function(id) {
		console.log(id)
	}
	
	
	
	

}
