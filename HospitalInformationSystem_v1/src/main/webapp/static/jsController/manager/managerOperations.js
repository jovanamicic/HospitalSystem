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

$('#operationsBody tr').hover(function()
    {
        $(this).find('td').addClass('rowHover');
    }, function()
    {
        $(this).find('td').removeClass('rowHover');
    });


function getNewOperations() {
	var table = document.getElementById("operationsBody");
	var pagination = document.getElementById("managerOperationsPag");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/operations/newOperations?page=" + currentPage,
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

function getAllOperations() {
	var table = document.getElementById("operationsBody");
	var pagination = document.getElementById("managerOperationsPag");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/operations/all?page=" + currentPage,
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
	$("#operationsBody tr").remove(); 
	data = data.content
	var list = data == null ? [] : (data instanceof Array ? data
			: [ data ]);
	$.each(list, function(index, operation) {
		var row = table.insertRow(-1);
		row.style.cursor = "pointer";
		row.classList.add('tableHover');

		var dateCell = row.insertCell(0);
		var nameCell = row.insertCell(1);
		var doctorCell = row.insertCell(2);
		var idCell = row.insertCell(3);
		
		nameCell.innerHTML = operation.name;
		doctorCell.innerHTML = operation.headDoctor.name + " " + operation.headDoctor.surname;
		
		var date = new Date(operation.date);
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
		
		idCell.innerHTML = operation.id;
		idCell.style.visibility = 'hidden';

		var createClickHandler = function(row) {
			return function() {
				var cell = row.getElementsByTagName("td")[3];
				var id = cell.innerHTML;
				
				window.location.href ="/managerOperationPreview.html?id=" + id;
			};
		};

		row.onclick = createClickHandler(row);
	});
}
  

function chooseOperations(obj) {
	  if($(obj).is(":checked")){
	    getNewOperations();
	  }
	  else{
		  getAllOperations();
	  }
	}

function displayOperation() {
	var id = getParam("id")
	
	document.getElementById("operationTimeLocation").innerHTML = "";
	
	$.ajax({
		type : "GET",
		contentType: "application/json",
		url : "/operations/" + id,
		success : function(data) {
			document.getElementById("operationName").innerHTML = data.name;
			
			// data about operation
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
			
			document.getElementById("operationDate").innerHTML = date;
			document.getElementById("operationDuration").innerHTML = data.duration + " sat";
			
			if (data.room) {
				var txt = document.createElement('label');
				
				var roomID = data.room.id;
				var roomName = data.room.name;
				txt.innerHTML = time + " časova, " + roomName + ".";
				txt.classList.add("bold-italic");
				txt.title = "Vreme i sala";
				
				document.getElementById("operationTimeLocation").appendChild(txt);
			} else {
				var txt = document.createElement('label');
				
				txt.innerHTML = "Nije izabrana sala.";
				txt.classList.add("bold-italic");
				document.getElementById("operationTimeLocation").appendChild(txt);
				
				var btn = document.createElement('a');
				
				btn.innerHTML = "Dodaj vreme i salu";
				btn.style.marginTop = "10%"
				btn.classList.add('button', 'special', 'fit');
				
				btn.addEventListener('click', function(){
				    openModal(data);
				});
				
				document.getElementById("operationTimeLocation").appendChild(btn);
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
			alert('GET OPERATION - Error: ' + e);
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


function openModal(data) {
	//clear options from select
	clearSelectValues();
	
	//display all rooms
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/rooms/all",
		success : function(data) {
			fillRooms(data.content);
		},
		error : function(e) {
			alert('Error: ' + e);
		}
	});
	
}

function clearSelectValues() {
	
	var allRooms = document.getElementById("roomSelect");
	for(i = allRooms.options.length - 1 ; i >= 0 ; i--)
        allRooms.remove(i);
	
	var allTimes = document.getElementById("timeSelect");
	for(i = allTimes.options.length - 1 ; i >= 0 ; i--)
        allTimes.remove(i);
	
	allTimes.disabled = true
	
}

function fillRooms(data) {
	
	var allRooms = document.getElementById("roomSelect");
	var allTimes = document.getElementById("timeSelect");
	
	var option = document.createElement("option");
	
	option.text = "- Sala -";
	allRooms.add(option)
	
	var option = document.createElement("option");
	option.text = "- Vreme -";
	allTimes.add(option)
	
	var list = data == null ? [] : (data instanceof Array ? data
			: [ data ]);
	$.each(list, function(index, room) {
		
		var option = document.createElement("option");
		option.text = room.name;
		option.value = room.id;
		
		allRooms.add(option);
	});
	modal.style.display = "block";
}

function fillTimes(id) {
	
	var select = document.getElementById("timeSelect");
	select.value = "- Vreme -";
	
	if (id.indexOf("-") != -1)
		select.disabled = true;
	else {
		select.disabled = false;
	
		var date = document.getElementById("operationDate").innerHTML;
		
		//display times when room is available
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/roomSchedules/available/" + id + "/" + date,
			success : function(data) {
				for (var i = 1; i < data.length; i++) {
					var option = document.createElement("option");
					option.text = data[i]
					select.add(option);
				}
			},
			error : function(e) {
				alert('Error: ' + e);
			}
		});
		
	}
}

function saveOperation() {
	var id = getParam("id");
	
	var roomId = document.getElementById("roomSelect").value;

	var date = document.getElementById("operationDate").innerHTML + " " +
			   document.getElementById("timeSelect").value;
	
	$.ajax({
		type : "PUT",
		contentType : "application/json",
		url : "/operations/saveTimeAndRoom",
		data : JSONOperationUpdate(id, roomId, date),
		success : function() {
			modal.style.display = "none";
			displayOperation();
		}
	});
}

function JSONOperationUpdate(id, roomId, date) {
	return JSON.stringify({
		"operationId" : id,
		"roomId" : roomId,
		"date" : date
	});
}

function checkValidity() {
	var btn = ""
}



function setupPagination(totalPages, isLast, isFirst, tmpPage, type) {
	
	currentPage = tmpPage;
	var pagination = document.getElementById("managerOperationsPag");
	
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
				getPageNewOperations(i - 1);
			else
				getPageAllOperations(i - 1);
			
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
					getPageNewOperations(i);
				else
					getPageAllOperations(i);
				
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
				getPageNewOperations(i + 1);
			else
				getPageAllOperations(i + 1);
			
		};
	};
	aNext.onclick = getNextPage(currentPage);
	
	if (isLast) {
		aNext.classList.add("disabled");
	}
	
	pagination.appendChild(aNext);
	
}


function getPageNewOperations(pageNum) {
	currentPage = pageNum;
	getNewOperations();
}

function getPageAllOperations(pageNum) {
	currentPage = pageNum;
	getAllOperations();
}

function resetPagNew() {
	currentPage = 0;
	getNewOperations();
}

function resetPagAll() {
	currentPage = 0;
	getAllOperations();
}