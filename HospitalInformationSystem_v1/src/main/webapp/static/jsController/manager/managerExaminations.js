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


$('#examinationsBody tr').hover(function()
    {
        $(this).find('td').addClass('rowHover');
    }, function()
    {
        $(this).find('td').removeClass('rowHover');
    });


function getNewExaminations() {
	var table = document.getElementById("examinationsBody");
	var pagination = document.getElementById("managerExaminationsPag");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/examinations/newExaminations?page=" + currentPage,
		success : function(data) {
			fillInTable(data, table);
			if (data.totalPages > 1) {
				setupPagination(data.totalPages, data.last, data.first, data.number, "new");
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

function getAllExaminations() {
	var table = document.getElementById("examinationsBody");
	var pagination = document.getElementById("managerExaminationsPag");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/examinations/all?page=" + currentPage,
		success : function(data) {
			fillInTable(data, table);
			if (data.totalPages > 1) {
				setupPagination(data.totalPages, data.last, data.first, data.number, "all");
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

function fillInTable(data, table) {
	$("#examinationsBody tr").remove(); 
	data = data.content
	var list = data == null ? [] : (data instanceof Array ? data
			: [ data ]);
	$.each(list, function(index, examination) {
		var row = table.insertRow(-1);
		row.style.cursor = "pointer";
		row.classList.add('tableHover');

		var dateCell = row.insertCell(0);
		var nameCell = row.insertCell(1);
		var doctorCell = row.insertCell(2);
		var idCell = row.insertCell(3);
		
		nameCell.innerHTML = examination.name;
		doctorCell.innerHTML = examination.doctor.name + " " + examination.doctor.surname;
		
		var date = new Date(examination.date);
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
		
		idCell.innerHTML = examination.id;
		idCell.style.visibility = 'hidden';

		var createClickHandler = function(row) {
			return function() {
				var cell = row.getElementsByTagName("td")[3];
				var id = cell.innerHTML;
				
				window.location.href ="/managerExaminationPreview.html?id=" + id;
			};
		};

		row.onclick = createClickHandler(row);
	});
}
  

function chooseExaminations(obj) {
	  if($(obj).is(":checked")){
	    getNewExaminations();
	  }
	  else{
		  getAllExaminations();
	  }
	}

function displayExamination() {
	var id = getParam("id")
	
	document.getElementById("examinationTime").innerHTML = "";
	
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/examinations/" + id,
		success : function(data) {
			document.getElementById("examinationName").innerHTML = data.name;
			
			// data about examination
			var date = new Date(data.date);
			var dd = date.getDate();
			var mm = date.getMonth() + 1;
			var time = date.getHours() + ":" 
			
			var mins = date.getMinutes();
			if (mins < 10)
				mins = '0' + mins;
			time += mins
			
			var yyyy = date.getFullYear();
			if(dd<10){
			    dd='0'+dd;
			} 
			if(mm<10){
			    mm='0'+mm;
			} 
			date = dd+'-'+mm+'-'+yyyy;
			
			document.getElementById("examinationDate").innerHTML = date;
			document.getElementById("examinationDiagnosis").innerHTML = data.diagnosis;
			document.getElementById("examinationSympthoms").innerHTML = data.symptons;
			
			if (!data.newEx) {
				var txt = document.createElement('label');
				
				txt.innerHTML = time + " časova.";
				txt.classList.add("bold-italic");
				txt.title = "Vreme";
				
				document.getElementById("examinationTime").appendChild(txt);
			} else {
				var txt = document.createElement('label');
				
				txt.innerHTML = "Nije izabrano vreme.";
				txt.classList.add("bold-italic");
				document.getElementById("examinationTime").appendChild(txt);
				
				var btn = document.createElement('a');
				
				btn.innerHTML = "Dodaj vreme";
				btn.style.marginTop = "10%"
				btn.classList.add('button', 'special', 'fit');
				
				btn.addEventListener('click', function(){
				    openModal(data);
				});
				
				document.getElementById("examinationTime").appendChild(btn);
			}
			
			// data about patient
				
			date = new Date(data.patient.birthday);
			dd = date.getDate();
			mm = date.getMonth() + 1;

			yyyy = date.getFullYear();
			if(dd<10){
			    dd='0'+dd;
			} 
			if(mm<10){
			    mm='0'+mm;
			} 
			date = dd+'-'+mm+'-'+yyyy;
			
			document.getElementById("patientName").innerHTML = data.patient.name + " " + data.patient.surname;
			document.getElementById("patientBirthday").innerHTML = date;
			
			
			var nodeGender = document.getElementById("patientGender");
			nodeGender.removeAttribute("class")
			if (data.patient.gender == "muško") 
				nodeGender.classList.add("fa-mars");
			else
				nodeGender.classList.add("fa-venus");
			nodeGender.innerHTML = data.patient.gender;
			
		},
		
		error : function(e) {
			alert('GET EXAMINATION - Error: ' + e);
		} 
	});
}

//Get the modal
var modal = document.getElementById('modalOp');
// Get the <span> element that closes the modal

var span = document.getElementById("spanCloseOp");
// When the user clicks on <span> (x), close the modal

span.onclick = function() {
	modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}


function openModal() {
	//clear options from select
	clearSelectValues();
	
	console.log("open modal")
	
	//display all times
	var select = document.getElementById("timeSelect");
	for (var i = 8; i < 23; i++) {
		var option = document.createElement("option");
		
		if ( i < 10)
			option.text = "0" + i + ":00" 
		else
			option.text = i + ":00"
		
		select.add(option);
	}
	
	modal.style.display = "block";
}

function clearSelectValues() {
	
	var allTimes = document.getElementById("timeSelect");
	for(var i = allTimes.options.length - 1 ; i >= 0 ; i--)
        allTimes.remove(i);
	
}

function saveExamination() {
	var id = getParam("id");
	
	var date = document.getElementById("examinationDate").innerHTML + " " +
			   document.getElementById("timeSelect").value;
	
	$.ajax({
		type : "PUT",
		contentType : "application/json",
		url : "/examinations/saveTime",
		data : JSONExaminationUpdate(id, date),
		success : function() {
			modal.style.display = "none";
			displayExamination();
		}
	});
}

function JSONExaminationUpdate(id, date) {
	return JSON.stringify({
		"examinationId" : id,
		"date" : date
	});
}

function checkValidity() {
	var btn = ""
}


function setupPagination(totalPages, isLast, isFirst, tmpPage, type) {
	
	currentPage = tmpPage;
	
	var pagination = document.getElementById("managerExaminationsPag");
	
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
			
			if (type == "new")
				getPageNewExaminations(i - 1);
			else
				getPageAllExaminations(i - 1);
			
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
				
				if (type == "new")
					getPageNewExaminations(i);
				else
					getPageAllExaminations(i);
				
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
			
			if (type == "new")
				getPageNewExaminations(i + 1);
			else
				getPageAllExaminations(i + 1);
			
		};
	};
	aNext.onclick = getNextPage(currentPage);
	
	pagination.appendChild(aNext);

	if (isLast) {
		aNext.classList.add("disabled");
	}
}


function getPageNewExaminations(pageNum) {
	currentPage = pageNum;
	getNewExaminations();
}

function getPageAllExaminations(pageNum) {
	currentPage = pageNum;
	getAllExaminations();
}

function resetPagNew() {
	document.getElementById("newEx").className = "button special fit";
	document.getElementById("allEx").className = "button fit";
	currentPage = 0;
	getNewExaminations();
}

function resetPagAll() {
	document.getElementById("newEx").className = "button fit";
	document.getElementById("allEx").className = "button special fit";
	currentPage = 0;
	getAllExaminations();
}
