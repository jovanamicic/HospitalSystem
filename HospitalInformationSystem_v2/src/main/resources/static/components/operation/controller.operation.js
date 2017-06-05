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
	vm.selectTimeDisabled;
	
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
				vm.displayBtnAddTimeAndRoom = true;
				//vm.displayBtnAddTimeAndRoom = $stateParams.isManager;
				// TODO $stateParams.isManager change to getRole!!!
			}
			
		})
		.catch(function() {
			vm.errorMessage = "Error loadin operation.";
		});
		
	}
	vm.loadOperation();
	
	vm.openModal = function() {
		vm.displayModal = "block";
		
		operationService.getRooms()
		.then(function(data) {
			
			data = data.data;

			vm.rooms = []
			vm.rooms.push({
				'name' : "- Sala -",
				'value' :  "- Sala -"
			});
			
			for (var i = 0; i < data.length; i++) {
				var option = {
					'name' : data[i].name,
					'value' : data[i].id
				}
				vm.rooms.push(option);
			}
			vm.roomId = vm.rooms[0].value;
			vm.selectTimeDisabled = true;
			
			vm.times = []
			vm.times.push({
				'name' : "- Vreme -",
				'value' :  "- Vreme -"
			})
			vm.selectedTime = vm.times[0].value;
		})
		.catch(function() {
			vm.errorMessage = "Error loadin rooms.";
		});
		
		
	}
	
	vm.closeModal = function() {
		vm.displayModal = "none";
	}
	
	vm.fillTimes = function() {
		var room = vm.roomId + "";
		
		vm.times = []
		vm.times.push( {
			'name' : "- Vreme -",
			'value' :  "- Vreme -"
		});
		
		if (room.indexOf("-") != -1) {
			vm.selectedTime = vm.times[0].value;
			vm.selectTimeDisabled = true;  //selected option is "- Sala -"
		} else {
			operationService.getTimes(getDate(vm.operation.date), vm.roomId)
			.then(function(data) {
				data = data.data;
				for (var i = 0; i < data.length; i++) {
					var option = {
						'name' : data[i],
						'value' : data[i]
					}
					vm.times.push(option);
				}
				vm.selectedTime = vm.times[0].value;
				vm.selectTimeDisabled = false;
			})
			.catch(function() {
				vm.errorMessage = "Error loadin times.";
				
				vm.times = []
				vm.times.push({
					'name' : "- Vreme -",
					'value' :  "- Vreme -"
				})
				
				vm.selectedTime = vm.times[0].value;
				vm.selectTimeDisabled = true;
			});
		}
	}
	
	vm.saveOperation = function() {
		
		var room = vm.roomId + ""
		if (room.indexOf("-") != -1) {
			toastr.warning("Niste izabrali salu.")
			return;
		}
		
		var time = vm.selectedTime + ""
		if (time.indexOf("-") != -1) {
			toastr.warning("Niste izabrali vreme.")
			return;
		}
		
		var date = new Date(vm.operation.date);
		var dd = date.getDate();
		var mm = date.getMonth() + 1;
		var yyyy = date.getFullYear();
		
		if(dd<10){
		    dd='0'+dd;
		} 
		if(mm<10){
		    mm='0'+mm;
		} 
		date = dd + '-' + mm + '-' + yyyy + ' ' + time;
		
		var operationUpdate = {
				"operationId" : vm.operation.id,
				"roomId" : room,
				"date" : date
		}
		console.log(operationUpdate)
		operationService.updateOperation(operationUpdate)
		.then(function(data) {
			vm.displayModal = "block";
			vm.loadOperation();
		})
		.catch(function() {
			vm.errorMessage = "Error updating operation.";
		});
		
	}
	
	getDate = function(dateD) {
		
		var date = new Date(dateD);
		var dd = date.getDate();
		var mm = date.getMonth() + 1;
		var yyyy = date.getFullYear();
		
		if(dd<10){
		    dd='0'+dd;
		} 
		if(mm<10){
		    mm='0'+mm;
		} 
		date = dd + '-' + mm + '-' + yyyy
		
		return date;
	}
	
}