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
			document.getElementById("country").value = data.country;
			document.getElementById("city").value = data.city;
			document.getElementById("zipCode").value = data.zipCode;
			document.getElementById("street").value = data.street;
			document.getElementById("number").value = data.number;
			document.getElementById("email").value = data.email;
			document.getElementById("username").value = data.username;
			
			$.ajax({
				type : "GET",
				contentType: "application/json",
				url : "/medicalstaff/" + data.doctor,
				success : function(data) {
					document.getElementById("doctor").value = data.name + " " + data.surname;
				},
				error : function(e) {
					alert('Error: ' + e);
				}});
			
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
		document.getElementById("buttons").style.display= "none";
		document.getElementById("recordBtn").style.display= "block";
	}
	else{
		//patient is logged
		document.getElementById("doctorMenu").style.display= "none";
		document.getElementById("patientMenu").style.display= "block";
		document.getElementById("buttons").style.display= "block";
		document.getElementById("recordBtn").style.display= "none";
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