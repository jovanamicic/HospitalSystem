angular.module('hospitalApp.controllers').controller(
		'ManagerOperationsController', ManagerOperationsController)

ManagerOperationsController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state' ];

function ManagerOperationsController($location, $stateParams, managerService,
		localStorageService, $http, $scope, $state) {

	var vm = this;
	
	vm.allOperations;
	vm.newOperations;
	
	vm.itemsPerPage = 10;
	vm.pageSize = 10;
	vm.displayOnlyNew = true;
	
	vm.currentPageAll = 1;
	vm.currentPageNew = 1;
	
	vm.totalPagesAll;
	vm.totalPagesNew;
	
	vm.newOperationsBtnActive;
	vm.allOperationsBtnActive;


	vm.getAllOperationsPage = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageAll = 1;
		
		managerService.getAllOperationsPage(newPage)
		.then(function(result) {
			vm.allOperations = result.data.content;
			vm.totalPagesAll = result.data.totalPages;
			vm.displayOnlyNew = false;
			
			vm.newOperationsBtnActive = false;
			vm.allOperationsBtnActive = true;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operations page.";
		});
	}
	
	vm.getNewOperationsPage = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageNew = 1;
		
		managerService.getNewOperationsPage(newPage)
		.then(function(result) {
			vm.newOperations = result.data.content;
			vm.totalPagesNew = result.data.totalPages;
			vm.displayOnlyNew = true;
			
			vm.newOperationsBtnActive = true;
			vm.allOperationsBtnActive = false;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operations page.";
		});
	}
	vm.getNewOperationsPage(0);
	
	vm.changePageAll = function() {
		vm.getAllOperationsPage(vm.currentPageAll - 1);
	}
	
	vm.changePageNew = function() {
		vm.getNewOperationsPage(vm.currentPageNew - 1);
	}
	
	
	vm.showOperation = function(operationId) {
		$state.go('manager.operation', {id: operationId, isManager: true})
	}
	
	
	
	

}
