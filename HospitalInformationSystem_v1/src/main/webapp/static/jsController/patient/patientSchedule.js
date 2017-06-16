function JSONPatientID(id) {
	return JSON.stringify({
		"id" : id
	});
}

function JSONDetails(operationExaminationId, type) {
	return JSON.stringify({
		"id" : operationExaminationId,
		"type" : type
	});
}

/**
 * Function that initializes calendar with schedule.
 * @returns all operation and examinations for logged patient
 */
function getPatientSchedule() {
	var id = sessionStorage.getItem("person");

	$('#calendar').fullCalendar(
			{
				locale : 'sr',
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
				
				eventClick : function(event, jsEvent, view) {
					$('#modalTitle').html(event.title);
					$('#modalDetailBody').html(event.description);
					fillInModal(event.id);
				},
			});

	// get all examinations and operations for logged patient
	$.ajax({
		type : "POST",
		url : "/patients/schedule",
		dataType : "json",
		contentType : "application/json",
		data : JSONPatientID(id),
		success : function(data) {
			$('#calendar').fullCalendar( 'removeEvents');
			
			//date format for calendar is yyyy-mm-dd
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
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR 4: " + errorThrown);
		}

	});
};


/**
 * Function that fill in detail modal for examination/operation
 */
function fillInModal(detail){
	var detail = detail.split("-");
	var type = detail[0];
	var operationExaminationId = detail[1];
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/patients/operationExaminationDetails",
		data : JSONDetails(operationExaminationId, type),
		success : function(data) {
			console.log(document.getElementById('doctor').innerHTML)
			document.getElementById('doctor').innerHTML = data.doctor;
			document.getElementById('nameTag').innerHTML = data.name;
			var date = changeDate(data.date).split("-");
			document.getElementById('dateTag').innerHTML = date[2] + "-" + date[1] + "-" + date[0];
			document.getElementById('timeTag').innerHTML = getTime(data.date);
			document.getElementById('operationOrExaminationTitle').innerHTML = data.type;
			
			if (data.symptons != null){
				document.getElementById('symptons').style.display = "block";
				document.getElementById('symptons').innerHTML = data.symptons;
			}
			else {
				document.getElementById('symptons').style.display = "none";
			}
			if (data.diagnosis != null){
				document.getElementById('diagnosis').style.display = "block";
				document.getElementById('diagnosis').innerHTML = data.diagnosis;
			}
			else {
				document.getElementById('diagnosis').style.display = "none";
			}
			if (data.therapy != null){
				document.getElementById('therapy').style.display = "block";
				document.getElementById('therapy').innerHTML = data.therapy;
			}
			else {
				document.getElementById('therapy').style.display = "none";
			}
			
			modalDetail.style.display = "block"; // open window on click
			
		},
		error : function(e) {
			console.log("Error " + e);
		}
	});
}


/**
 * Function that return date in yyyy-mm-dd format
 * @param date
 * @returns formatted date
 */
function changeDate(date){
	var date = new Date(date);
	var dd = date.getDate();
	var mm = date.getMonth() + 1;

	var yyyy = date.getFullYear();
	if(dd<10){
	    dd='0'+dd;
	} 
	if(mm<10){
	    mm='0'+mm;
	} 
	var date = yyyy+'-'+mm+'-'+dd;
	
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
	
	if (time == "0:00")
		time = "Satnica još uvek nije utvrđena."
	
	return time;
}

// Get the modal
var modalDetail = document.getElementById('myModalDetail');

// Get the <span> element that closes the modal
var spanDetail = document.getElementById("spanCloseDetail");

// When the user clicks on <span> (x), close the modal
function closeModalDetails() {
	modalDetail.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if (event.target == modalDetail) {
		modalDetail.style.display = "none";
	}
	
}