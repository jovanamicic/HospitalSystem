angular.module('hospitalApp.controllers').controller('MedicalStaffPatientsController',
		MedicalStaffPatientsController)

MedicalStaffPatientsController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope', '$state'];

function MedicalStaffPatientsController($location, $stateParams, medicalStaffService, localStorageService, $http, $scope, $state) {

	var vm = this;

	vm.displayAll = true;
	vm.displayNew = false;
	vm.displaySearchResults = false;
	
	vm.allPatients;
	vm.myPatients;
	vm.foundedPatients;
	
	vm.itemsPerPage = 10;
	vm.pageSize = 10;
	
	vm.currentPageAll = 1;
	vm.currentPageMy = 1;
	vm.currentPageSearch = 1;
	
	vm.totalPagesAll;
	vm.totalPagesMy;
	vm.totalPagesSearch;
	
	
	
	vm.allPatientsBtnActive;
	vm.myPatientsBtnctive;
	
	vm.noSearchResult;
	
	vm.searchData;
	

	vm.getAllPatients = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageAll = 1;
		
		medicalStaffService.getPatients(newPage).then(
				function(data) {
					vm.allPatients = data.data.content;
					vm.totalPagesAll = data.data.totalPages;
					
					vm.displayAll = true;
					vm.displayMy = false;
					vm.displaySearchResults = false;
					
					vm.allPatientsBtnActive = true;
					vm.myPatientsBtnctive = false;
					
				}).catch(function() {
					vm.errorMessage = "Something went wrong with getting all patients!";
		});
	}
	vm.getAllPatients(0);
	
	
	vm.getMyPatients = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageMy = 1;
		
		medicalStaffService.getMyPatients(newPage).then(
				function(data) {
					vm.myPatients = data.data.content;
					vm.totalPagesMy = data.data.totalPages;
					
					vm.displayAll = false;
					vm.displayMy = true;
					vm.displaySearchResults = false;
					
					vm.allPatientsBtnActive = false;
					vm.myPatientsBtnctive = true;
					
				}).catch(function() {
					vm.errorMessage = "Something went wrong with getting my patients!";
		});
	}
	
	vm.searchPatients  = function(newPage) {
		
		if (newPage == 0)
			vm.currentPageSearch = 1;
		
		if (!vm.searchData || vm.searchData.length == 0) {
			vm.getAllPatients(0);
			return;
		}
		
		medicalStaffService.getPatientsBySearchData(vm.searchData, newPage).then(
				function(data) {
					vm.foundedPatients = data.data.content;
					if (vm.foundedPatients == 0){
						$location.path('/medicalStaff/noResults/'+vm.searchData);
					}
					vm.totalPagesSearch = data.data.totalPages;
					
					vm.displayAll = false;
					vm.displayMy = false;
					vm.displaySearchResults = true;
					
					vm.allPatientsBtnActive = true;
					vm.myPatientsBtnctive = false;
					
				}).catch(function() {
					vm.errorMessage = "Something went wrong with searching patients!";
		});
	}
	
	vm.emptySearch = function() {
		console.log('empty')
		if (vm.searchData.length == 0)
			vm.getAllPatients(0);
	}
	
	vm.showPatient = function(id) {
		$location.path('/medicalStaff/patient/profile/'+id);
	}
	
	vm.changePageAll = function() {
		vm.getAllPatients(vm.currentPageAll - 1);
	}
	
	vm.changePageMy = function() {
		vm.getMyPatients(vm.currentPageAll - 1);
	}
	
	vm.changePageSearch = function() {
		vm.searchPatients(vm.currentPageSearch - 1);
	}
	
}
