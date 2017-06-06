angular.module('hospitalApp.controllers').controller(
		'ManagerExaminationsController', ManagerExaminationsController)

ManagerExaminationsController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state' ];

function ManagerExaminationsController($location, $stateParams, managerService,
		localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.allExaminations;
	vm.totalExaminations;
	vm.itemsPerPage = 10;
	vm.displayOnlyNew = true;
	
	vm.currentPage = 1;
	vm.pageSize = 10;
	vm.totalPages;


	vm.getAllExaminationsPage = function(newPage) {
		managerService.getAllExaminationsPage(newPage)
		.then(function(result) {
			vm.allExaminations = result.data.content;
			vm.totalPages = result.data.totalPages;
			vm.totalExaminations = result.data.totalElements;
			vm.displayOnlyNew = false;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin examinations page.";
		});
	}
	
	vm.getNewExaminationsPage = function(newPage) {
		managerService.getNewExaminationsPage(newPage)
		.then(function(result) {
			vm.allExaminations = result.data.content;
			vm.totalPages = result.data.totalPages;
			vm.totalExaminations = result.data.totalElements;
			vm.displayOnlyNew = true;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin examinations page.";
		});
	}
	vm.getNewExaminationsPage(0);
	
	vm.changePage = function() {
		var newPage = vm.currentPage - 1;
		
		if (vm.displayOnlyNew)
			vm.getNewExaminationsPage(newPage);
		else
			vm.getAllExaminationsPage(newPage);
	}
	
	
	vm.showExamination = function(examinationId) {
		$state.go('manager.examination', {id: examinationId, isManager: true})
	}
	
	
	
	

}
