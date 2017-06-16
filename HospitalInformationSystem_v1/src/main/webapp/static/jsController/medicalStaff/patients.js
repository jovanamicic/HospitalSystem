var currentPage = 0;

function JSONDoctorsID(id) {
	return JSON.stringify({
		"id" : id
	});
}

$('#patientsBody tr').hover(function()
    {
        $(this).find('td').addClass('rowHover');
    }, function()
    {
        $(this).find('td').removeClass('rowHover');
    });

 

function getAllPatients() {
	var table = document.getElementById("patientsBody");
	var pagination = document.getElementById("doctorPatientsPag");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/patients/all?page=" + currentPage,
		success : function(data) {
			fillInTable(data, table);
			
			if (data.totalPages > 1) {
				setupPagination(data.totalPages, data.last, data.first, data.number, "all");
				pagination.style.display = "block";
			}
			else
				pagination.style.display = "none";
				
			document.getElementById("myP").className = "button fit";
			document.getElementById("allP").className = "button special fit";
			document.getElementById("noMyPatients").style.display= "none";
		},
		error : function(e) {
			alert('Error: ' + e);
		}
	});
}

function getMyPatients() {
	var table = document.getElementById("patientsBody");
	var id = sessionStorage.getItem("person");
	var pagination = document.getElementById("doctorPatientsPag");
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/patients/my?page=" + currentPage,
		data : JSONDoctorsID(id),
		success : function(data) {
			document.getElementById("myP").className = "button special fit";
			document.getElementById("allP").className = "button fit";
			
			if (data.totalElements != 0){
				document.getElementById("patientsTable").style.display= "block";
				document.getElementById("noMyPatients").style.display= "none";
				fillInTable(data, table);
				
				if (data.totalPages > 1) {
					setupPagination(data.totalPages, data.last, data.first, data.number, "all");
					pagination.style.display = "block";
				}
				else
					pagination.style.display = "none";	
			}
			else {
				document.getElementById("patientsTable").style.display= "none";
				document.getElementById("noMyPatients").style.display= "block";
				pagination.style.display = "none";	
			}
		},
		error : function(e) {
			alert('Error: ' + e);
		}
	});
}

function fillInTable(data, table) {
	
	data = data.content;
	
	document.getElementById("patientsTable").style.display= "";
	document.getElementById("noPatients").style.display= "none";
	
	$("#patientsBody tr").remove(); 
	
	var list = data == null ? [] : (data instanceof Array ? data
			: [ data ]);
	$.each(list, function(index, patient) {
		var row = table.insertRow(-1);
		row.style.cursor = "pointer";
		row.classList.add('tableHover');

		var name = row.insertCell(0);
		var surname = row.insertCell(1);
		var gender = row.insertCell(2);
		var birthday = row.insertCell(3);
		var doctor = row.insertCell(4);
		var id = row.insertCell(5);
		
		name.innerHTML = patient.name;
		surname.innerHTML = patient.surname;
		gender.innerHTML = patient.gender;
		
		var date = new Date(patient.birthday);
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
		
		birthday.innerHTML = date;
		if(patient.chosenDoctor != null)
			doctor.innerHTML = patient.chosenDoctor.name + " " +  patient.chosenDoctor.surname;
		
		id.innerHTML = patient.id;
		id.style.visibility = 'hidden';

		var createClickHandler = function(row) {
			return function() {
				var cell = row.getElementsByTagName("td")[5];
				var id = cell.innerHTML;
				window.location.href ="/patientProfile.html?id=" + id;
			};
		};

		row.onclick = createClickHandler(row);
	});
}

function choosePatients(obj) {
	  if($(obj).is(":checked")){
	    getMyPatients();
	  }
	  else{
		  getAllPatients();
	  }
	}


function searchPatients(){
	var table = document.getElementById("patientsBody");
	var searchData = $('#search').val();
	var pagination = document.getElementById("doctorPatientsPag");
	
	if (searchData != ""){
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/patients/search/" + searchData+ "?page=" + currentPage,
			success : function(data) {
				if (data.totalElements != 0){
					document.getElementById("patientsTable").style.display= "block";
					document.getElementById("noPatients").style.display= "none";
					document.getElementById("noMyPatients").style.display= "none";
					
					fillInTable(data, table);
					
					if (data.totalPages > 1) {
						setupPagination(data.totalPages, data.last, data.first, data.number, "search");
						pagination.style.display = "block";
					}
					else
						pagination.style.display = "none";	
					
				}
				else {
					document.getElementById("patientsTable").style.display= "none";
					document.getElementById("noPatients").style.display= "block";
					document.getElementById("noMyPatients").style.display= "none";
					pagination.style.display = "none";	
				}
			},
			error : function(e) {
				alert('Error: ' + e);
			}
		});
	}
	else {
		getAllPatients();
	}
}
	
function setupPagination(totalPages, isLast, isFirst, tmpPage, type) {
		
	currentPage = tmpPage;
	
	var pagination = document.getElementById("doctorPatientsPag");
	
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
			
			if (type == "my")
				getPageMyPatients(i - 1);
			else if (type == "all")
				getPageAllPatients(i - 1);
			else
				getPageSearchPatients(i - 1);
			
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
				
				if (type == "my")
					getPageMyPatients(i);
				else if (type == "all")
					getPageAllPatients(i);
				else
					getPageSearchPatients(i);
				
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
			
			if (type == "my")
				getPageMyPatients(i + 1);
			else if (type == "all")
				getPageAllPatients(i + 1);
			else
				getPageSearchPatients(i + 1);
			
		};
	};
	aNext.onclick = getNextPage(currentPage);
	
	if (isLast) {
		aNext.classList.add("disabled");
	}
	
	pagination.appendChild(aNext);
	
}


function getPageNewPatients(pageNum) {
	currentPage = pageNum;
	getNewPatients();
}

function getPageAllPatients(pageNum) {
	currentPage = pageNum;
	getAllPatients();
}

function getPageSearchPatients(pageNum) {
	currentPage = pageNum;
	searchPatients();
}

function resetPagMy() {
	currentPage = 0;
	getMyPatients();
}

function resetPagAll() {
	currentPage = 0;
	getAllPatients();
}

function resetPagSearch() {
	currentPage = 0;
	searchPatients();
}
