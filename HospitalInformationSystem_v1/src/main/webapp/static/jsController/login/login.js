function JSONLogin(username, password) {
	return JSON.stringify({
		"username" : username,
		"password" : password
	});
}

function login(){
	var username = $('#username').val();
	var password = $("#password").val();
		$.ajax({
			type : "POST",
			contentType: "application/json",
			url : "/persons/login",
			data : JSONLogin(username, password),
			success : function(data) {
					sessionStorage.setItem('person', data.id);
					localStorage.setItem('personID', data.id); //because of sharing storage between tabs
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