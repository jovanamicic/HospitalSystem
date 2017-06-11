angular.module('hospitalApp.controllers').controller(
		'ExaminationController',
		ExaminationController)

ExaminationController.$inject = [ '$location', '$stateParams',
		'examinationService', 'localStorageService', '$http', '$scope',
		'$state' ];

function ExaminationController($location, $stateParams,
		examinationService, localStorageService, $http, $scope, $state) {

	var vm = this;
	vm.examination = {};
	vm.isManager;
	vm.displayBtnAddTime;
	vm.selectTimeDisabled;
	
	vm.loadExamination = function(){
		
		examinationService.loadExamination($stateParams.id)
		.then(function(data) {
			
			data = data.data;
			
			vm.examination= data;
			
			if (!data.newEx) {
				var date = new Date(data.date);
				var time = date.getHours() + ":" 
				
				var mins = date.getMinutes();
				if (mins < 10)
					mins = '0' + mins;
				time += mins
				
				vm.time = time + " h";
				vm.displayBtnAddTime = false;
			} else {
				vm.time = "Nije izabrano vreme.";
				vm.displayBtnAddTime = true;
				//vm.displayBtnAddTime = $stateParams.isManager;
				// TODO $stateParams.isManager change to getRole!!!
			}
			
		})
		.catch(function() {
			vm.errorMessage = "Error loadin examination.";
		});
		
	}
	vm.loadExamination();
	
	vm.openModal = function() {
		vm.displayModal = "block";
		
		vm.times = []
		vm.times.push({
			'name' : "- Vreme -",
			'value' :  "- Vreme -"
		})
		
		for (var i = 8; i < 23; i++) {
			
			var val;
			if ( i < 10)
				val = "0" + i + ":00" 
			else
				val = i + ":00"
			
			vm.times.push({
				'name' : val,
				'value' :  val
			})
		}
		
		vm.selectedTime = vm.times[0].value;
		
		
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
			examinationService.getTimes(getDate(vm.examination.date), vm.roomId)
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
	
	vm.saveExamination = function() {
		
		var time = vm.selectedTime + ""
		if (time.indexOf("-") != -1) {
			toastr.warning("Niste izabrali vreme.")
			return;
		}
		
		var date = new Date(vm.examination.date);
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
		
		var examinationUpdate = {
				"examinationId" : vm.examination.id,
				"date" : date
		}
		examinationService.updateExamination(examinationUpdate)
		.then(function(data) {
			vm.displayModal = "none";
			vm.loadExamination();
		})
		.catch(function() {
			vm.errorMessage = "Error updating examination.";
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