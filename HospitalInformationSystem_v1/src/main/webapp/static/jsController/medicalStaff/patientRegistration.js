function getDoctors(){
	$.ajax({
		type : "GET",
		dataType: "json",
		url : "/medicalstaff/all",
		success : function(data) {
			var comboBox = document.getElementById("doctor");
			for (i = 0; i < data.length; i++) { 
				var option = document.createElement("option");
			    option.text = data[i].name + " " + data[i].surname;
			    option.value = data[i].id;
			    comboBox.add(option);
			}
		    
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}



function JSONRegistration(name,surname,pID,birthday,email,gender,doctor,country,city,zipCode,street,number) {
	return JSON.stringify({
		"name" : name,
		"surname" : surname,
		"personalID" : pID,
		"birthday" : birthday,
		"email" : email,
		"gender" : gender,
		"doctor" : doctor,
		"country" : country,
		"city" : city,
		"zipCode" : zipCode,
		"street" : street,
		"number" : number
	});
}

function addPatient(){
	var name = $('#name').val();
	var surname = $('#surname').val();
	var pID = $('#personalID').val();
	var birthday = $('#birthday').val();
	var email = $('#email').val();
	var gender = $('#gender').val();
	var doctor = $('#doctor').val();
	var country = $('#country').val();
	var city = $('#city').val();
	var zipCode = $('#zipCode').val();
	var street = $('#street').val();
	var number = $('#number').val();
	
	if (name == "" || surname == "" || pID == ""){
		toastr.error("Niste popunili sva obavezna polja!");
	}
	
	else {
	$.ajax({
		type : "POST",
		contentType: "application/json",
		url : "/patients",
		data : JSONRegistration(name,surname,pID,birthday,email,gender,doctor,country,city,zipCode,street,number),
		success : function(data) {
			 window.location.href = "patientProfile.html?id=" + data.id;
		},
		error : function(e) {
			console.log("Error " + e);
		} 
	});
	}
}


function checkEmail(){
	var email = $('#email').val();
	$.ajax({
		type : "POST",
		contentType: "application/json",
		url : "/patients/email",
		data : email,
		error : function(e) {
			toastr.error("Email adresa se već koristi! Unesite drugu adresu.");
			$('#email').val("");
		} 
	});
}

function checkPID(){
	var personalID = $('#personalID').val();
	$.ajax({
		type : "POST",
		contentType: "application/json",
		url : "/patients/personalID",
		data : personalID,
		error : function(e) {
			toastr.error("JMBG se već koristi!");
			$('#personalID').val("");
		} 
	});
}

function checkBirthday(){
	var birthday = $('#birthday').val();
	$.ajax({
		type : "POST",
		contentType: "application/json",
		url : "/patients/birthday",
		data : birthday,
		error : function(e) {
			toastr.error("Uneli ste neispravan datum!");
			$('#birthday').val("");
		} 
	});
}