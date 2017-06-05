angular.module('hospitalApp.controllers').controller(
		'OperationController',
		OperationController)

OperationController.$inject = [ '$location', '$stateParams',
		'operationService', 'localStorageService', '$http', '$scope',
		'$state' ];

function OperationController($location, $stateParams,
		operationService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.operation = {};
	vm.isManager;
	vm.displayBtnAddTimeAndRoom;
	
	vm.loadOperation = function(){
		
		operationService.loadOperation($stateParams.id)
		.then(function(data) {
			
			data = data.data;
			
			vm.operation= data;
			
			if (data.room) {
				var date = new Date(data.date);
				var time = date.getHours() + ":" 
				
				var mins = date.getMinutes();
				if (mins < 10)
					mins = '0' + mins;
				time += mins
				
				vm.timeAndPlace = time + " h, " + data.room.name;
				vm.displayBtnAddTimeAndRoom = false;
			} else {
				vm.timeAndPlace = "Nije izabrana sala.";
				vm.displayBtnAddTimeAndRoom = $stateParams.isManager;
			}
			
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operation.";
		});
		
	}
	vm.loadOperation();

	
}