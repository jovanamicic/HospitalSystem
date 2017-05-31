function JSONLogin(username, password) {
	return JSON.stringify({
		"username" : username,
		"password" : password
	});
}

function login(){
	var username = $('#username').val();
	var password = $("#password").val();
	if(username == ""){
		toastr.error("Molimo Vas unesite korisničko ime!");
		return false;
	}
	else if(password == ""){
		toastr.error("Molimo Vas unesite lozinku!");
		return false;
	}
	else{
		$.ajax({
			type : "POST",
			contentType: "application/json",
			url : "/persons/login",
			data : JSONLogin(username, password),
			success : function(data) {
					sessionStorage.setItem('person', data.id);
					var url = "";
					if (data.role.toLowerCase().indexOf("manager") != -1) 
						url = "managerProfile.html";
					else if(data.role == "medical staff")
						url = "doctorAllPatients.html";
					else if(data.role == "patient")
						url = "patientProfile.html";
					
					setTimeout(function(){
						   window.location.href = url;
						}, 1000);
			},
			error : function(e) {
				toastr.error("Pogrešno korisničko ime ili lozinka. Molimo Vas pokušajte ponovo!");
				$('#username').val("");
				$('#password').val("");
			} 
		});
	}
}