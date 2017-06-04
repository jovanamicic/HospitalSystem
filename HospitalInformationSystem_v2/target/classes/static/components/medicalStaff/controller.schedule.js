angular.module('hospitalApp.controllers').controller(
		'MedicalStaffScheduleController', MedicalStaffScheduleController)

MedicalStaffScheduleController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope',
		'$state', '$compile', 'uiCalendarConfig' ];

function MedicalStaffScheduleController($location, $stateParams,
		medicalStaffService, localStorageService, $http, $scope, $state,
		$compile, uiCalendarConfig) {

	var vm = this;

	vm.operationExamination = {};
	
	vm.displayModal = "none";

	$scope.uiConfig = {
		calendar : {
			// lang : 'sr',
			editable : true,
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month,agendaWeek,agendaDay,listWeek'
			},
			navLinks : true,

			weekNumbers : true,
			weekNumbersWithinDays : true,
			weekNumberCalculation : 'ISO',

			editable : true,
			eventLimit : true,
			displayEventTime: false,
			events : [],
			
			dayClick : function(date, jsEvent, view) {
				if (date.isAfter(moment())) {
					vm.displayModal = "block";
					
					var clickedDate = date.format().split("-");
					document.getElementById('date').value = clickedDate[2]
							+ "-" + clickedDate[1] + "-" + clickedDate[0];
					vm.operationExamination.date = clickedDate[2]
					+ "-" + clickedDate[1] + "-" + clickedDate[0];
				}

			},

		}
	};
	
	
	vm.getSchedule = function() {
		medicalStaffService.getSchedule().then(
				function(data, status, headers, config) {
					
					for (d in data) {
						var date = changeDate(data[d].date);
						
						var color = '#22c7b8';
						if(data[d].type == "Operacija")
							color = '#475B5A'

						eventData = {
							title : data[d].name,
							start : new Date(date),
							color : color,
							id : data[d].type + "-" + data[d].operationExaminationId
						};
						$('#calendar').fullCalendar('renderEvent', eventData, true);
					}

				}).catch(function(data, status, headers, config) {
					vm.errorMessage = "Something went wrong with getting all patients!";
		});
	}
	
	
	vm.save = function() {
		var ok = checkInputs();
		if(ok){
			if(vm.operationExamination.type == "operacija"){
				medicalStaffService.saveOperation(vm.operationExamination).then(
						function(data, status, headers, config) {
							var date = changeDate(data.date);
							
							eventData = {
								title : data.name,
								start : new Date(date),
								color : '#22c7b8',
								id : type + "-" + data.id
							};
							
							vm.closeModal();
							// $('#calendar').fullCalendar('renderEvent', eventData,
							// true);

						}).catch(function(data, status, headers, config) {
							vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving operation!";
				});
			}
			else if(vm.operationExamination.type == "pregled"){
				medicalStaffService.saveExamination(vm.operationExamination).then(
						function(data, status, headers, config) {
							var date = changeDate(data.date);
							
							eventData = {
								title : data.name,
								start : new Date(date),
								color : '#22c7b8',
								id : type + "-" + data.id
							};
							
							vm.closeModal();
							// $('#calendar').fullCalendar('renderEvent', eventData,
							// true);
							
						}).catch(function(data, status, headers, config) {
							vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving exmination!";
							vm.wrongPatientPersonalId = true;
				});
			}
		}
	}
	
	vm.closeModal = function() {
		vm.displayModal = "none";
	}
	
	
	/**
	 * Function that return date in yyyy-mm-dd format
	 * 
	 * @param date
	 * @returns formatted date
	 */
	function changeDate(date) {
		var date = new Date(date);
		var dd = date.getDate();
		var mm = date.getMonth() + 1;

		var yyyy = date.getFullYear();
		if (dd < 10) {
			dd = '0' + dd;
		}
		if (mm < 10) {
			mm = '0' + mm;
		}
		var date = yyyy + '-' + mm + '-' + dd;

		return date;
	}
	
	
	function checkInputs() {
		if (vm.operationExamination.personalId == null){
			vm.wrongId = true;
			return false;
		}
		if (vm.operationExamination.type == null){
			vm.wrongType = true;
			return false;
		}
		if (vm.operationExamination.type == "operacija"){
			if (vm.operationExamination.duration == null){
				vm.wrongDuration = true;
				return false;
			}
		}
		if (vm.operationExamination.name == null){
			vm.wrongName = true;
			return false;
		}
		return true;
	}

}
