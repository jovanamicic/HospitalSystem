function JSONDoctorsID(id) {
	return JSON.stringify({
		"id" : id
	});
}

function JSONOperationExaminationID(id) {
	return JSON.stringify({
		"id" : id
	});
}

function JSONExaminationOperation(personalId, type, name, date, doctorId,
		duration) {
	return JSON.stringify({
		"personalId" : personalId,
		"type" : type,
		"name" : name,
		"date" : date,
		"doctorId" : doctorId,
		"duration" : duration
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
 * 
 * @returns all operation and examinations for logged doctor
 */
function getDoctorsSchedule() {
	var id = sessionStorage.getItem("person");

	$('#calendar').fullCalendar(
			{
				locale : 'sr',
				header : {
					left : 'prev,next today',
					center : 'title',
					right : 'month'
				},
				navLinks : true,

				weekNumbers : true,
				weekNumbersWithinDays : true,
				weekNumberCalculation : 'ISO',

				editable : true,
				eventLimit : true,
				displayEventTime : false,
				events : [],

				// set up operation or examination for clicked day
				dayClick : function(date, jsEvent, view) {
					if (date.isAfter(moment())) {
						$('#modalTitle').html(event.title);
						$('#modalBody').html(event.description);
						modal.style.display = "block";

						var clickedDate = date.format().split("-");
						document.getElementById('date').value = clickedDate[2]
								+ "-" + clickedDate[1] + "-" + clickedDate[0];
					}

				},

				eventClick : function(event, jsEvent, view) {
					fillInModal(event.id);
				},

			});

	// get all examinations and operations for logged doctor
	$.ajax({
		type : "POST",
		url : "/medicalstaff/schedule",
		dataType : "json",
		contentType : "application/json",
		data : JSONDoctorsID(id),
		success : function(data) {
			$('#calendar').fullCalendar('removeEvents');

			for (d in data) {
				var date = changeDate(data[d].date);

				var color = '#22c7b8';
				if (data[d].type == "Operacija")
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
 * Function that saves new operation or examination
 * 
 * @returns saved operation or examination
 */
function save() {

	var ok = checkInputs();
	if (ok) {
		
		var personalID = $('#personalID').val();
		var type = $('#type').val();
		var name = $('#name').val();
		var date = $('#date').val();
		var duration = "";
		var doctorId = sessionStorage.getItem("person");
		var url;

		if (type == "operacija")
			url = "/operations/saveOperation";
		else
			url = "/examinations/saveExamination";
		
		if(type == "operacija")
			duration = $('#duration').val();

		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					url : url,
					data : JSONExaminationOperation(personalID, type, name,
							date, doctorId, duration),
					success : function(data) {
						var date = changeDate(data.date);
						eventData = {
							title : data.name,
							start : new Date(date),
							color : '#22c7b8',
							id : type + "-" + data.id
						};
						$('#calendar').fullCalendar('renderEvent', eventData,
								true);
						modal.style.display = "none";

						// clean modal
						$('#personalID').val("");
						$('#type').val("");
						$('#name').val("");
						$('#date').val("");

					},
					error : function(e) {
						$('#personalID').focus();
						document.getElementById("invalidPatientID").style.display = "inline";
						$('#personalID').val("");
					}
				});
	}
}

/**
 * Function that check inputs for setting up operation/examination.
 * 
 * @returns
 */
function checkInputs() {
	if ($('#personalID').val() == "" || $('#personalID').val() == null) {
		document.getElementById('nonPatientID').style.display = "inline";
		return false;
	} 
	else if ($('#type').val() == "" || $('#type').val() == null) {
		document.getElementById('invalidType').style.display = "inline";
		return false;
	}
	else if ($('#type').val() == "operacija") {
		if ($('#duration').val() == "" || $('#duration').val() == null) {
			document.getElementById('invalidDuration').style.display = "inline";
			return false;
		}
	}
	if ($('#name').val() == "" || $('#name').val() == null) {
		document.getElementById('invalidName').style.display = "inline";
		return false;
	} 
	return true;
}

/**
 * Function that fill in detail modal for examination/operation
 */
function fillInModal(detail) {
	var detail = detail.split("-");
	var type = detail[0];
	var operationExaminationId = detail[1];

	document.getElementById('operationExaminationId').innerHTML = operationExaminationId;

	$
			.ajax({
				type : "POST",
				contentType : "application/json",
				url : "/medicalstaff/operationExaminationDetails",
				data : JSONDetails(operationExaminationId, type),
				success : function(data) {
					document.getElementById('patient').innerHTML = data.patient;
					document.getElementById('nameTag').innerHTML = data.name;
					var date = changeDate(data.date).split("-");
					document.getElementById('dateTag').innerHTML = date[2]
							+ "-" + date[1] + "-" + date[0];
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
					
					modalDetail.style.display = "block"; 
					

				},
				error : function(e) {
					console.log("Error " + e);
				}
			});
}

/**
 * Function that cancels operation or examination
 */
function cancelOperationExamination() {
	var id = $('#operationExaminationId').text();
	var type = $('#operationOrExaminationTitle').text();

	if (type == "Operacija")
		url = "/operations/" + id;
	else
		url = "/examinations/" + id;

	$.ajax({
		type : "DELETE",
		contentType : "application/json",
		url : url,
		data : JSONOperationExaminationID(id),
		success : function(data) {
			modalDetail.style.display = "none";
			location.reload();
		},
		error : function(e) {
			alert("greska");
			console.log("Error " + e);
		}
	});
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
	
	if (time == "0:00")
		time = "Satnica još uvek nije utvrđena."

	return time;
}

/**
 * Function that display duration if operation is check
 */
$("#type").change(function() {
	if ($(this).val() == "operacija")
		document.getElementById('durationDiv').style.display = "block";
	else
		document.getElementById('durationDiv').style.display = "none";
});

/**
 * Functions that disposes span errors on focus
 * 
 * @returns
 */
function disposeErrorsPatientId() {
	document.getElementById('nonPatientID').style.display = "none";
	document.getElementById('invalidPatientID').style.display = "none";
}

function disposeErrorsName() {
	document.getElementById('invalidName').style.display = "none";
}

function disposeErrorsType() {
	document.getElementById('invalidType').style.display = "none";
}

function disposeErrorsDuration() {
	document.getElementById('invalidDuration').style.display = "none";
}

// Get the modal
var modal = document.getElementById('myModal');
var modalDetail = document.getElementById('myModalDetail');

// Get the <span> element that closes the modal
var span = document.getElementById("spanClose");
var spanDetail = document.getElementById("spanCloseDetail");


function closeModal() {
	modal.style.display = "none";
	document.getElementById('personalID').value = "";
	document.getElementById('type').value = "";
	document.getElementById('name').value = "";
	document.getElementById('date').value = "";
	document.getElementById('duration').value = "";
	document.getElementById('symptons').value = "";
	document.getElementById('diagnosis').value = "";
	document.getElementById('therapy').value = "";
}


function closeModalDetails() {
	modalDetail.style.display = "none";
	
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
		document.getElementById('personalID').value = "";
		document.getElementById('type').value = "";
		document.getElementById('name').value = "";
		document.getElementById('date').value = "";
		document.getElementById('duration').value = "";
		document.getElementById('symptons').value = "";
		document.getElementById('diagnosis').value = "";
		document.getElementById('therapy').value = "";
	}

	if (event.target == modalDetail) {
		modalDetail.style.display = "none";
	}

}