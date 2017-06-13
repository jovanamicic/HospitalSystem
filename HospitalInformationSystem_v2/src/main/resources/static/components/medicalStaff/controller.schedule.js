angular.module('hospitalApp.controllers').controller(
		'MedicalStaffScheduleController', MedicalStaffScheduleController)

MedicalStaffScheduleController.$inject = [ '$location', '$stateParams',
		'medicalStaffService', 'localStorageService', '$http', '$scope',
		'$state', '$compile', 'uiCalendarConfig', '$timeout', '$window' ];

function MedicalStaffScheduleController($location, $stateParams,
		medicalStaffService, localStorageService, $http, $scope, $state,
		$compile, uiCalendarConfig, $timeout, $window) {

	var vm = this;

	 vm.operationExamination = {};
	 vm.clickedEvent = {};
	 
	 vm.monthYear;
	 vm.monthNames = ["januar", "februar", "mart", "april", "maj", "jun",
		  "jul", "avgust", "septembar", "octobar", "novembar", "decembar"
		];
	 
	 vm.displayModal = "none";
	 vm.displayModalDetails = "none";
	 
	vm.events = [];
	vm.eventSources =  [] ;
	
		
	vm.uiConfig = {
		calendar : {
			header : {
				right : 'title'
			},
			weekNumbers : true,
			weekNumbersWithinDays : true,
			weekNumberCalculation : 'ISO',

			eventLimit : true,
			displayEventTime : false,

			dayClick : function(date, jsEvent, view) {
				if (date.isAfter(moment())) {
					vm.displayModal = "block";

					var clickedDate = date.format().split("-");
					document.getElementById('date').value = clickedDate[2]
							+ "-" + clickedDate[1] + "-" + clickedDate[0];
					vm.operationExamination.date = clickedDate[2] + "-"
							+ clickedDate[1] + "-" + clickedDate[0];
				}

			},
			eventClick : function(event, jsEvent, view) {
				fillInModal(event.id);
			},

		}
	};
	
	
	displayCurrentMonthYear = function() {
		var today = new Date();
		
		var mm = today.getMonth(); //January is 0!
		var yyyy = today.getFullYear();

		mm = vm.monthNames[mm];
		
		vm.monthYear = mm + " " + yyyy;
	}
	displayCurrentMonthYear();	
		
	 getSchedule = function() {
		 medicalStaffService.getSchedule().then(function(data, status, headers, config) {
			 for (d in data.data) {
				 var date = changeDate(data.data[d].date);
							
				 var color = '#22c7b8';
				 if(data.data[d].type == "Operacija")
					 color = '#475B5A'
	
						 vm.events.push({
							 title : data.data[d].name,
							 start : new Date(date),
							 end : new Date(date),
							 color : color,
							 id : data.data[d].type + "-" + data.data[d].operationExaminationId
						 });
			 }
			 $timeout(function() {
				 vm.eventSources.push(vm.events) ;
			 }, 1000);
	
		 }).catch(function(data, status, headers, config) {
			 vm.errorMessage = "Something went wrong with getting doctor schedule!";
		 });
	 }
	 getSchedule();
		
		
	 vm.save = function() {
		 var ok = checkInputs();
		 if(ok){
			 if(vm.operationExamination.type == "operacija"){
				 medicalStaffService.saveOperation(vm.operationExamination).then(function(data, status, headers, config) {
					 var date = changeDate(data.data.date);
								
					 vm.events.push({
							 title : data.data.name,
							 start : new Date(date),
							 color : '#475B5A',
							 id : type + "-" + data.id
					 });
					 vm.closeModal();
	
				 }).catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving operation!";
					 if (data.status == 404)
						 vm.wrongPatientPersonalId = true;
				 });
			 }
			 else if(vm.operationExamination.type == "pregled"){
				 medicalStaffService.saveExamination(vm.operationExamination).then(function(data, status, headers, config) {
					 var date = changeDate(data.data.date);
								
					 vm.events.push({
							 title : data.data.name,
							 start : new Date(date),
							 end : new Date(date),
							 color : '#22c7b8',
							 id : type + "-" + data.id
					 });
					 vm.closeModal();
								
				 }).catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with saving exmination!";
					 if (data.status == 404)
						 vm.wrongPatientPersonalId = true;
				 });
			 }
		 }
	 }
	
	
	 vm.cancelOperationExamination = function(){
			var id = vm.clickedEvent.operationExaminationId;
			var type = vm.clickedEvent.title;
			
			if (type == "Operacija"){
				medicalStaffService.deleteOperation(parseInt(id))
				.then(function(data, status, headers, config) {
					vm.closeModalDetails();
					 $window.location.reload();
				})
				.catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with deleteing operation!";
					 vm.wrongPatientPersonalId = true;
				 });
			}
			else if (type == "Pregled"){
				medicalStaffService.deleteExamination(parseInt(id))
				.then(function(data, status, headers, config) {
					vm.closeModalDetails();
					 $window.location.reload();
				})
				.catch(function(data, status, headers, config) {
					 vm.errorMessageWrongPatientPersonalId = "Something went wrong with deleteing examination!";
					 vm.wrongPatientPersonalId = true;
				 });
			}
		}
	 

	 function fillInModal(detail) {
			var detail = detail.split("-");
			var type = detail[0];
			var operationExaminationId = detail[1];
			
			vm.clickedEvent.operationExaminationId = operationExaminationId;

			 medicalStaffService.getOperationExaminationDetails(type, parseInt(operationExaminationId)).then(function(data, status, headers, config) {
				 vm.clickedEvent.doctor = data.data.doctor;
				 vm.clickedEvent.patient = data.data.patient;
				 vm.clickedEvent.name = data.data.name;
				 vm.clickedEvent.doctor = data.data.doctor;
				 var date = changeDate(data.data.date).split("-");
				 vm.clickedEvent.date = date[2]
					+ "-" + date[1] + "-" + date[0];
				 
				 vm.clickedEvent.time = getTime(data.data.date);
				 vm.clickedEvent.title = data.data.type;
				 
				 vm.clickedEvent.symptons = data.data.symptons;
				 vm.clickedEvent.diagnosis = data.data.diagnosis;
				 vm.clickedEvent.therapy = data.data.therapy;
				 
				 vm.displayModalDetails = "block";
				 
			 }).catch(function(data, status, headers, config) {
				 vm.errorMessage = "Something went wrong with geting details about operation or examination!";
			 });
		}
	 
	 
	 vm.closeModal = function() {
		 vm.displayModal = "none";
	 }
	 
	 vm.closeModalDetails = function() {
		 vm.displayModalDetails = "none";
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
	 
	 /**
		 * Function that return time from time-date format
		 * 
		 * @param date
		 * @returns formatted time
		 */
	 function getTime(date) {
	 	var date = new Date(date);
	 	
	 	var time = date.getHours() + ":" 
	 	
	 	var mins = date.getMinutes();
	 	if (mins < 10)
	 		mins = '0' + mins;
	 	
	 	time += mins;
	 	
	 	return time;
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
	

	/* remove event */
	vm.remove = function(index) {
		vm.events.splice(index, 1);
	};
	
	/* Change View */
	vm.changeView = function(view, calendar) {
		uiCalendarConfig.calendars[calendar].fullCalendar('changeView', view);
	};
	

}
