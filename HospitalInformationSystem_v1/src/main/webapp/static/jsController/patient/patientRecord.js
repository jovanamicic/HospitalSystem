var currentPage = 0;

function JSONExaminationOperation(personalId, type, name, date, doctorId, duration) {
	return JSON.stringify({
		"personalId" : personalId,
		"type" : type,
		"name" : name,
		"date" : date,
		"doctorId" : doctorId,
		"duration" : duration
	});
}

function getParam(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}

function switchMenu(){
	var id = getParam("id");
	if (id){
		//doctor is logged
		document.getElementById("doctorMenu").style.display= "block";
		document.getElementById("patientMenu").style.display= "none";
		document.getElementById("buttons").style.display= "block";
		document.getElementById("aNameSurname").style.display= "inline";
	}
	else{
		//patient is logged
		document.getElementById("doctorMenu").style.display= "none";
		document.getElementById("patientMenu").style.display= "block";
		document.getElementById("buttons").style.display= "none";
		document.getElementById("aNameSurname").style.display= "none";
	}
}

function getProfile(){
	var id = getParam("id");
	if (!id)
		id = sessionStorage.getItem('person');
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/patients/" + id,
		success : function(data) {
			document.getElementById("nameSurname").innerHTML= data.name + " "+data.surname;
			$("#patientName").val(data.name + " "+data.surname);
			$("#personalID").val(data.personalID);
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}

function goToPatientProfile(){
	var id = getParam("id");
	window.location.href = "/patientProfile.html?id=" + id;
}

function startExamination(){
	document.getElementById("examinationDiv").style.display= "block";
	document.getElementById("buttons").style.display= "none";
}

function JSONExamination(patientID,symptoms, diagnosis, therapy) {
	return JSON.stringify({
		"patientID" : patientID,
		"symptoms" : symptoms,
		"diagnosis" : diagnosis,
		"therapy" : therapy
	});
}

function saveExamination(){
	var patientID = getParam("id");
	var symptoms = $('#symptoms').val();
    var diagnosis = $('#diagnosis').val();
    var therapy = $('#therapy').val();
    
    if (symptoms == "" || diagnosis == "" || therapy == ""){
    	toastr.error("Niste popunili sva polja!");
    	}
    else {
	$.ajax({
		type : "POST",
		contentType: "application/json",
		url : "/examinations",
		data : JSONExamination(patientID, symptoms, diagnosis, therapy),
		success : function(data) {
			toastr.info("Pregled je sačuvan!");
			document.getElementById("examinationDiv").style.display= "none";
			document.getElementById("buttons").style.display= "block";
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
    }
}

function getExaminations(){
	var patientID = getParam("id");
	
	if (!patientID)
		patientID = sessionStorage.getItem('person');
	
	var table = document.getElementById("patientRecordBody");
	var pagination = document.getElementById("patientRecordPag");
	
	$.ajax({
		type : "GET",
		url : "/examinations/patients/"+ patientID + "?page=" + currentPage,
		success : function(data) {
			
			fillInTable(data, table, "Examination");
			
			if (data.totalPages > 1) {
				setupPagination(data.totalPages, data.last, data.first, data.number, "examinations");
				pagination.style.display = "block";
			}
			else
				pagination.style.display = "none";
			
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}

function getOperations(){
	var patientID = getParam("id");
	
	if (!patientID)
		patientID = sessionStorage.getItem('person');
	
	var table = document.getElementById("patientRecordBody");
	var pagination = document.getElementById("patientRecordPag");
	
	$.ajax({
		type : "GET",
		url : "/operations/patient/"+ patientID + "?page=" + currentPage,
		success : function(data) {
			
			fillInTable(data, table, "Operation");
			
			if (data.totalPages > 1) {
				setupPagination(data.totalPages, data.last, data.first, data.number, "operations");
				pagination.style.display = "block";
			}
			else
				pagination.style.display = "none";
			
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}

/**
 * Function that opens modal for set up operation or examination
 * @returns
 */
function openModal(element){
	if(element.id == "Pregled")
		document.getElementById("durationDiv").style.display = "none";
	else
		document.getElementById("durationDiv").style.display = "block";
	
	$("#type").val(element.id);
	
	modal.style.display = "block";
	
}


/**
 * Function that saves new operation or examination
 * 
 * @returns saved operation or examination
 */
function save() {
	
	if($('#name')[0].checkValidity()){
		if($('#date')[0].checkValidity()){
			if(checkDate($('#date').val())){
				var duration;
				if (document.getElementById("duration").style.display != "block") {
					duration = $('#duration').val();
				}
				
				var personalID = $('#personalID').val();
				var type = $('#type').val();
				var name = $('#name').val();
				var date = $('#date').val();
				var doctorId = sessionStorage.getItem("person");
				var url;
				
		
				if (type == "Operacija")
					url = "/operations/scheduleOperation";
				else
					url = "/examinations/scheduleExamination";
		
				$.ajax({
							type : "POST",
							contentType : "application/json",
							url : url,
							data : JSONExaminationOperation(personalID, type, name, date,
									doctorId, duration),
							success : function(data) {
								modal.style.display = "none";
								
								//clean modal
								$('#type').val("");
								$('#name').val("");
								$('#date').val("");
								
								if (type == "operacija")
									toastr.info("Operacija je zakazana za datum " + date);
								else
									toastr.info("Pregled je zakazan za datum " + date);
							},
							error : function(e) {
								toastr.error("Došlo je do greške prilikom zakazivanja. Pokušajte ponovo.");
							}
						});
			}
			else {
				$('#date').focus();
				document.getElementById("invalidDate").style.display = "inline";
				$('#invalidDate').text("Datum zakazivanja mora biti veći od današnjeg.");
				$('#date').val("");
			}
		}
		else {
			$('#date').focus();
			document.getElementById("invalidDate").style.display = "inline";
			$('#invalidDate').text("Format unosa dd-mm-gggg");
			$('#date').val("");
		}
	}
	else {
		$('#name').focus();
		document.getElementById("invalidName").style.display = "inline";
		$('#invalidName').text("Obavezno polje.");
	}
	
}


/**
 * Function that disposes span errors on focus
 * @returns
 */
function disposeErrors(element){
	document.getElementById(element.id).nextSibling.nextSibling.style.display = "none";
}

/**
 * Function that checks is date of operation/examination in past
 */
function checkDate(date){
	var now = new Date();
	var date = date.split("-");
	date = new Date(date[1] + "-" + date[0] + "-" + date[2]);
	if (date < now) {
		  return false;
	}
	return true;
}

//Get the modal
var modal = document.getElementById('myModal');

//Get the <span> element that closes the modal
var span = document.getElementById("spanClose");

//When the user clicks on <span> (x), close the modal
span.onclick = function() {
	modal.style.display = "none";
}

//When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}


function fillInTable(data, table, type) {
	
	data = data.content;
	
	document.getElementById("patientRecordTable").style.display= "";
	
	$("#patientRecordBody tr").remove(); 
	
	var list = data == null ? [] : (data instanceof Array ? data
			: [ data ]);
	$.each(list, function(index, item) {
		var row = table.insertRow(-1);
		row.style.cursor = "pointer";
		row.classList.add('tableHover');

		var dateCell = row.insertCell(0);
		var nameCell = row.insertCell(1);
		var doctorCell = row.insertCell(2);
		var idCell = row.insertCell(3);
		
		nameCell.innerHTML = item.name;
		
		if (type == "Examination")
			doctorCell.innerHTML = item.doctor.name + " " + item.doctor.surname;
		else if (type == "Operation")
			doctorCell.innerHTML = item.headDoctor.name + " " + item.headDoctor.surname;
		else
			doctor.innerHTML = "-"
				
		var date = new Date(item.date);
		var dd = date.getDate();
		var mm = date.getMonth() + 1;

		var yyyy = date.getFullYear();
		if(dd<10){
		    dd='0'+dd;
		} 
		if(mm<10){
		    mm='0'+mm;
		} 
		var date = dd+'-'+mm+'-'+yyyy;
		
		dateCell.innerHTML = date;
		
		idCell.innerHTML = item.id;
		idCell.style.visibility = 'hidden';

		var createClickHandler = function(row) {
			return function() {
				var cell = row.getElementsByTagName("td")[3];
				var id = cell.innerHTML;
				
				window.location.href ="/manager" + type + "Preview.html?id=" + id;
			};
		};

		row.onclick = createClickHandler(row);
	});
}

function setupPagination(totalPages, isLast, isFirst, tmpPage, type) {
	
	currentPage = tmpPage;
	
	var pagination = document.getElementById("patientRecordPag");
	
	while (pagination.firstChild) {
	    pagination.removeChild(pagination.firstChild);
	}
	
	var aPrev = document.createElement('a');
	var linkText = document.createTextNode('«');
	aPrev.appendChild(linkText);
	aPrev.title = "prethodna stranica";
	aPrev.href = "#";
	aPrev.classList.add("button");
	
	//on prev click
	var getPrevPage = function(i) {
		return function() {
			
			if (type == "examinations")
				getPageExaminations(i - 1);
			else
				getPageOperations(i - 1);
			
		};
	};
	aPrev.onclick = getPrevPage(currentPage);

	if (isFirst) {
		aPrev.classList.add("disabled");
	}
	
	pagination.appendChild(aPrev);
	
	for (var i = 1; i <= totalPages; i++) {
		var a = document.createElement('a');
		var linkText = document.createTextNode(i);
		a.appendChild(linkText);
		a.title = "stranica " + i;
		a.href = "#";
		a.id = "page" + i + "Link";
		a.classList.add("page");
		
		if (i == tmpPage + 1)
			a.classList.add("active");
		
		//on page change
		var getPage = function(i) {
			return function() {
				
				if (type == "examinations")
					getPageExaminations(i);
				else
					getPageOperations(i);
				
			};
		};
		a.onclick = getPage(i-1);
		
		pagination.appendChild(a);
	}
	
	var aNext = document.createElement('a');
	var linkText = document.createTextNode('»');
	aNext.appendChild(linkText);
	aNext.title = "sledeća stranica";
	aNext.href = "#";
	aNext.classList.add("button");
	
	//on next click
	var getNextPage = function(i) {
		return function() {
			
			if (type == "examinations")
				getPageExaminations(i + 1);
			else
				getPageOperations(i + 1);
			
		};
	};
	aNext.onclick = getNextPage(currentPage);
	
	if (isLast) {
		aNext.classList.add("disabled");
	}
	
	pagination.appendChild(aNext);
	
}


function getPageExaminations(pageNum) {
	currentPage = pageNum;
	getExaminations();
}

function getPageOperations(pageNum) {
	currentPage = pageNum;
	getOperations();
}

function resetPagExaminations() {
	currentPage = 0;
	getExaminations();
}

function resetPagOperations() {
	currentPage = 0;
	getOperations();
}

