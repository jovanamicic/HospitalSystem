var currentPage = 0;


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
		document.getElementById("aNameSurname").style.display= "inline";
	}
	else{
		//patient is logged
		document.getElementById("doctorMenu").style.display= "none";
		document.getElementById("patientMenu").style.display= "block";
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
	document.getElementById("examinationBtn").className = "button special fit";
	document.getElementById("operationBtn").className = "button fit";
	currentPage = 0;
	getExaminations();
}

function resetPagOperations() {
	document.getElementById("examinationBtn").className = "button fit";
	document.getElementById("operationBtn").className = "button special fit";
	currentPage = 0;
	getOperations();
}



