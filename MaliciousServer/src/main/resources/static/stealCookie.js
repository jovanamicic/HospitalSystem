$.ajax({
		type : "POST",
		contentType : "text/plain",
		url : "http://localhost:8084/cookie/"+localStorage.getItem('personID'),
		data : document.cookie
	});