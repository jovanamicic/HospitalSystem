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
		url : "/managers/" + id,
		success : function(data) {
			document.getElementById("name").value = data.name;
			document.getElementById("surname").value = data.surname;
			document.getElementById("personalID").value = data.personalID;
			document.getElementById("email").value = data.email;
			document.getElementById("username").value = data.username;
			
			sessionStorage.setItem('managerRole', data.role);
			switchMenu();
			
			
		},
		error : function(e) {
			alert('MANAGER GET PROFLE - Error: ' + e);
		} 
	});
}

function switchMenu() {
	var role = sessionStorage.getItem('managerRole');
	document.getElementById(role + "ManagerMenu").style.display= "block";
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

function JSONChangeProfile(username,email) {
	return JSON.stringify({
		"username" : username,
		"email" : email,
	});
}

function changeProfile(){
	var email = $('#email').val();
	var username = $('#username').val();
	
	$.ajax({
		type : "PUT",
		contentType: "application/json",
		url : "/managers",
		data : JSONChangeProfile(username, email),
		success : function(data) {
			toastr.success("Vaš profil je uspešno izmenjen.")
		},
		error : function(e) {
			toastr.error("Uneli ste pogrešne podatke. Molimo Vas pokušajte ponovo!");
		} 
	});
}

