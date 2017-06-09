angular.module('hospitalApp.controllers').controller(
		'ManagerExaminationsController', ManagerExaminationsController)

ManagerExaminationsController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state' ];

function ManagerExaminationsController($location, $stateParams, managerService,
		localStorageService, $http, $scope, $state) {

	var vm = this;
	
	vm.allExaminations;
	vm.newlExaminations;
	
	vm.itemsPerPage = 10;
	vm.pageSize = 10;
	vm.displayOnlyNew = false;
	
	vm.currentPageAll = 1;
	vm.currentPageNew = 1;
	
	vm.totalPagesAll;
	vm.totalPagesNew;
	
	vm.newExaminationsBtnActive;
	vm.allExaminationsBtnActive;


	vm.getAllExaminationsPage = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageAll = 1;
		
		managerService.getAllExaminationsPage(newPage)
		.then(function(result) {
			vm.allExaminations = result.data.content;
			vm.totalPagesAll = result.data.totalPages;
			vm.displayOnlyNew = false;
			
			vm.newExaminationsBtnActive = false;
			vm.allExaminationsBtnActive = true;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin examinations page.";
		});
	}
	
	
	vm.getNewExaminationsPage = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageNew = 1;
		
		managerService.getNewExaminationsPage(newPage)
		.then(function(result) {
			vm.newExaminations = result.data.content;
			vm.totalPagesNew = result.data.totalPages;
			vm.displayOnlyNew = true;
			
			vm.newExaminationsBtnActive = true;
			vm.allExaminationsBtnActive = false;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin examinations page.";
		});
	}
	
	vm.getNewExaminationsPage(0);
	
	vm.changePageAll = function() {
		vm.getAllExaminationsPage(vm.currentPageAll - 1);
	}
	
	vm.changePageNew = function() {
		vm.getNewExaminationsPage(vm.currentPageNew - 1);
	}
	
	
	vm.showExamination = function(examinationId) {
		$state.go('manager.examination', {id: examinationId, isManager: true})
	}
	
	
	
	

}
