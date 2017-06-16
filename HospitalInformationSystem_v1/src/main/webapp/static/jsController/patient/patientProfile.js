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

function getProfile(){
	var id = getParam("id");
	if (!id)
		id = sessionStorage.getItem('person');
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/patients/" + id,
		success : function(data) {
			document.getElementById("name").value = data.name;
			document.getElementById("surname").value = data.surname;
			document.getElementById("personalID").value = data.personalID;
			document.getElementById("birthday").value = data.birthday;
			document.getElementById("gender").value = data.gender;
			if (data.country != null)
				document.getElementById("country").value = data.country;
			document.getElementById("city").value = data.city;
			document.getElementById("zipCode").value = data.zipCode;
			document.getElementById("street").value = data.street;
			document.getElementById("number").value = data.number;
			document.getElementById("email").value = data.email;
			document.getElementById("username").value = data.username;
			
			document.getElementById("patientName").value = data.name + " " + data.surname;
			document.getElementById("personalID").value = data.personalID;
			
			var doctor = data.doctor;
			if (doctor != 0){
				$.ajax({
					type : "GET",
					contentType: "application/json",
					url : "/medicalstaff/" + doctor,
					success : function(data) {
						document.getElementById("doctor").value = data.name + " " + data.surname;
						if(doctor == sessionStorage.getItem('person')){
							document.getElementById("recordBtn").style.display= "block";
						}
						else{
							document.getElementById("recordBtn").style.display= "none";
						}
					},
					error : function(e) {
						alert('Error: ' + e);
					}});
			}
			else {
				document.getElementById("doctor").value = "Nema izabranog lekara.";
			}
			
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}

function switchMenu(){
	var id = getParam("id");
	if (id){
		//doctor is logged
		document.getElementById("doctorMenu").style.display= "block";
		document.getElementById("patientMenu").style.display= "none";
		document.getElementById("saveProfile").style.display= "none";
		document.getElementById("scheduleExaminationBtn").style.display= "block";
		document.getElementById("examinationBtn").style.display= "block";
		document.getElementById("scheduleOperationBtn").style.display= "block";
		
		
		
	}
	else{
		//patient is logged
		document.getElementById("doctorMenu").style.display= "none";
		document.getElementById("patientMenu").style.display= "block";
		document.getElementById("saveProfile").style.display= "block";
		document.getElementById("recordBtn").style.display= "block";
		document.getElementById("scheduleExaminationBtn").style.display= "none";
		document.getElementById("examinationBtn").style.display= "none";
		document.getElementById("scheduleOperationBtn").style.display= "none";
	}
}

function JSONChangePassword(oldPassword, newPassword) {
	return JSON.stringify({
		"oldPassword" : oldPassword,
		"newPassword" : newPassword
	});
}

function changePassword(){
	var oldPassword = $("#oldPassword").val();
	var newPassword = $("#newPassword").val();
	var newRPassword = $("#newRPassword").val();
	
	if (newPassword == newRPassword && oldPassword != "" && newPassword != "" && newRPassword != ""){
		$.ajax({
			type : "PUT",
			contentType: "application/json",
			url : "/persons/password",
			data : JSONChangePassword(oldPassword, newPassword),
			success : function() {
				toastr.success("Vaša lozinka je uspešno izmenjena.")
			},
			error : function(e) {
				toastr.error("Uneli ste pogrešnu staru lozinku. Molimo Vas pokušajte ponovo!");
				$('#oldPassword').val("");
				$('#newPassword').val("");
				$('#newRPassword').val("");
			} 
		});
	}
	else{
		toastr.error("Dogodila se greška, molimo Vas pokušajte ponovo!");
	}
}

function JSONChangeProfile(username,email,country,city,zipCode,street,number) {
	return JSON.stringify({
		"username" : username,
		"email" : email,
		"country" : country,
		"city" : city,
		"zipCode" : zipCode,
		"street" : street,
		"number" : number
	});
}

function changeProfile(){
	var email = $('#email').val();
	var username = $('#username').val();
	var country = $('#country').val();
	var city = $('#city').val();
	var zipCode = $('#zipCode').val();
	var street = $('#street').val();
	var number = $('#number').val();
	
	if (username == "" || email == ""){
		toastr.error("Unesite sva obavezna polja.")
	}
	else {
	$.ajax({
		type : "PUT",
		contentType: "application/json",
		url : "/patients",
		data : JSONChangeProfile(username,email,country,city,zipCode,street,number),
		success : function(data) {
			toastr.success("Vaš profil je uspešno izmenjen.")
		},
		error : function(e) {
			toastr.error("Uneli ste pogrešne podatke. Molimo Vas pokušajte ponovo!");
		} 
	});
	}
}

function patientRecord(){
	var id = getParam("id");
	window.location.href = "patientRecord.html?id="+id;
}

function checkUsername(){
	var username = $('#username').val();
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/patients/username/", 
		data : username,
		error : function(e) {
			toastr.error("Korisničko ime se već koristi! Unesite drugo.");
		} 
	});
}

function checkEmail(){
	var email = $('#email').val();
	if (email != ""){
		$.ajax({
			type : "POST",
			contentType: "application/json",
			url : "/patients/email",
			data : email,
			error : function(e) {
				toastr.error("Email adresa se već koristi! Unesite drugu adresu.");
			} 
		});
	}
}

function startExamination(){
	document.getElementById("examinationDiv").style.display= "block";
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
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
    }
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
	
	var modal = document.getElementById('myModal').style.display = "block";
	
}


/**
 * Function that saves new operation or examination
 * 
 * @returns saved operation or examination
 */
function save() {
	
	var ok = checkInputs();
	if (ok) {
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
						var modal = document.getElementById('myModal');
						modal.style.display = "none";

						if (type == "operacija")
							toastr.info("Operacija je zakazana za datum "
									+ date);
						else
							toastr.info("Pregled je zakazan za datum " + date);

						// clean modal
						cleanModal();
					},
					error : function(e) {
						cleanModal();
						toastr
								.error("Došlo je do greške prilikom zakazivanja. Pokušajte ponovo.");
					}
				});
	}
}


function checkInputs() {
	if ($('#nameOpEx').val() == "" || $('#nameOpEx').val() == null) {
		document.getElementById('invalidName').style.display = "inline";
		document.getElementById('invalidName').innerHTML = "Obavezno polje.";
		return false;
	} 
	else if ($('#type').val() == "Operacija") {
		if ($('#duration').val() == "" || $('#duration').val() == null) {
			document.getElementById('invalidDuration').style.display = "inline";
			document.getElementById('invalidDuration').innerHTML = "Obavezno polje.";
			return false;
		}
	}
	if ($('#date').val() == "" || $('#date').val() == null) {
		document.getElementById('invalidDate').style.display = "inline";
		document.getElementById('invalidDate').innerHTML = "Format unosa dd-mm-gggg";
		document.getElementById('date').value = "";
		return false;
	}
	else if ($('#date')[0].checkValidity() == false) {
		document.getElementById('invalidDate').style.display = "inline";
		document.getElementById('invalidDate').innerHTML = "Format unosa dd-mm-gggg";
		document.getElementById('date').value = "";
		return false;
	}
	else if (checkDate($('#date').val()) == false) {
		document.getElementById('invalidDate').style.display = "inline";
		document.getElementById('invalidDate').innerHTML = "Datum zakazivanja mora biti veći od današnjeg.";
		document.getElementById('date').value = "";
		return false;
	}
}


function cleanModal() {
	$('#type').val("");
	$('#nameOpEx').val("");
	$('#date').val("");
	$('#duration').val("");
	
	document.getElementById("invalidName").style.display = "none";
	document.getElementById("invalidDate").style.display = "none";
	document.getElementById("duration").style.display == "none"
}


/**
 * Function that disposes span errors on focus
 * @returns
 */
function disposeErrors(element){
	var a =document.getElementById(element.id).nextSibling.nextSibling;
	if(document.getElementById(element.id).nextSibling.nextSibling != null)
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

//When the user clicks on <span> (x), close the modal
function spanClose() {
	var modal = document.getElementById('myModal');
	modal.style.display = "none";
	cleanModal();
}

//When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	var modal = document.getElementById('myModal');
	if (event.target == modal) {
		modal.style.display = "none";
		
		$('#type').val("");
		$('#nameOpEx').val("");
		$('#date').val("");
		$('#duration').val("");
		
		document.getElementById("invalidName").style.display = "none";
		document.getElementById("invalidDate").style.display = "none";
		document.getElementById("duration").style.display == "none"
	}
}