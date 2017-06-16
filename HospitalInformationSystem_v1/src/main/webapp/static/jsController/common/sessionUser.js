function JSONPersonID(personID) {
	return JSON.stringify({
		"personID" : personID
	});
}


function getLoggedUser() {
	var personID = sessionStorage.getItem('person');
	if (personID == null)
		personID = localStorage.getItem('personID');
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/persons/getPerson/" + personID,
		success : function(data) {
			document.getElementById("personName").innerHTML = data.name + " " + data.surname;
			
			if (data.photo)
				document.getElementById("personPhoto").src = data.photo;
			else
				document.getElementById("personPhoto").src = "static/images/avatar.png"
			
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}