angular.module('hospitalApp.controllers').controller(
		'PatientScheduleController', PatientScheduleController)

PatientScheduleController.$inject = [ '$location', '$stateParams',
		'patientService', 'localStorageService', '$http', '$scope',
		'$state', '$compile', 'uiCalendarConfig', '$timeout', '$window' ];

function PatientScheduleController($location, $stateParams,
		patientService, localStorageService, $http, $scope, $state,
		$compile, uiCalendarConfig, $timeout, $window) {

	var vm = this;

	 vm.clickedEvent = {};
	 
	 vm.displayModalDetails = "none";
	 
	/* event source that contains custom events on the scope */
	vm.events = [];
	vm.eventSources =  [] ;
	
		
	vm.uiConfig = {
		calendar : {
			// lang : 'sr',
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month'
			},
			weekNumbers : true,
			weekNumbersWithinDays : true,
			weekNumberCalculation : 'ISO',

			eventLimit : true,
			displayEventTime : false,

			eventClick : function(event, jsEvent, view) {
				fillInModal(event.id);
			},

			eventRender : vm.eventRender

		}
	};
		
		
	 getSchedule = function() {
		 patientService.getSchedule().then(function(data, status, headers, config) {
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
			 vm.errorMessage = "Something went wrong with getting patient schedule!";
		 });
	 }
	 getSchedule();
		
		

	 function fillInModal(detail) {
			var detail = detail.split("-");
			var type = detail[0];
			var operationExaminationId = detail[1];
			
			vm.clickedEvent.operationExaminationId = operationExaminationId;

			 patientService.getOperationExaminationDetails(type, parseInt(operationExaminationId)).then(function(data, status, headers, config) {
				 vm.clickedEvent.doctor = data.data.doctor;
				 vm.clickedEvent.patient = data.data.patient;
				 vm.clickedEvent.name = data.data.name;
				 vm.clickedEvent.doctor = data.data.doctor;
				 var date = changeDate(data.data.date).split("-");
				 vm.clickedEvent.date = date[2]
					+ "-" + date[1] + "-" + date[0];
				 
				 vm.clickedEvent.time = getTime(data.data.date);
				 vm.clickedEvent.title = data.data.type;
				 
				 vm.displayModalDetails = "block";
				 
			 }).catch(function(data, status, headers, config) {
				 vm.errorMessage = "Something went wrong with geting details about operation or examination!";
			 });
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
		
		
	

	/* add and removes an event source of choice */
	vm.addRemoveEventSource = function(sources, source) {
		var canAdd = 0;
		angular.forEach(sources, function(value, key) {
			if (sources[key] === source) {
				sources.splice(key, 1);
				canAdd = 1;
			}
		});
		if (canAdd === 0) {
			sources.push(source);
		}
	};
	
	/* remove event */
	vm.remove = function(index) {
		vm.events.splice(index, 1);
	};
	
	/* Change View */
	vm.changeView = function(view, calendar) {
		uiCalendarConfig.calendars[calendar].fullCalendar('changeView', view);
	};
	
	/* Change View */
	vm.renderCalender = function(calendar) {
		if (uiCalendarConfig.calendars[calendar]) {
			uiCalendarConfig.calendars[calendar].fullCalendar('render');
		}
	};

	vm.uiConfig.calendar.dayNames = [ "Nedelja", "Ponedeljak", "Utorak",
		"Sreda", "Četvrtak", "Petak", "Subota" ];
	vm.uiConfig.calendar.dayNamesShort = [ "Ned", "Pon", "Uto", "Sre",
		"Čet", "Pet", "Sub" ];
	vm.uiConfig.calendar.monthNames = ['Januar', 'Februar', 'Mart', 'April', 'Maj', 'Jun', 'Jul',
		 'Avgust', 'Septembar', 'Octobar', 'Novembar', 'Decembar']
	vm.uiConfig.calendar.currentText = "Danas";
	vm.uiConfig.calendar.month = "Mesec";

}
