var currentPage = 0;

function getDoctors(){
	$.ajax({
		type : "GET",
		dataType: "json",
		url : "/medicalstaff/all",
		success : function(data) {
			var comboBox = document.getElementById("recipient");
			for (i = 0; i < data.length; i++) { 
				var option = document.createElement("option");
			    option.text = data[i].name + " " + data[i].surname;
			    option.value = data[i].name + " " + data[i].surname;
			    comboBox.add(option);
			}
		    
		},
		error : function(e) {
			alert('Error: ' + e);
		} 
	});
}


function JSONNewPayment(recipient, account, amount, currency, managerId) {
	return JSON.stringify({
		"recipient" : recipient,
		"account" : account,
		"amount" : amount,
		"currency" : currency,
		"managerId" : managerId
	});
}

function savePayment(){
	recipient = $("#recipient").val();
	account = $("#account").val();
	amount = $("#amount").val();
	currency = $("#currency").val();
	managerId = sessionStorage.getItem("person");
	
	if (recipient == "" || account == "" || amount == "" || currency == ""){
		toastr.error("Niste popunili sva polja. Molimo Vas pokušajte ponovo.");
	}
	else{
		$.ajax({
			type : "POST",
			contentType: "application/json",
			url : "/payments",
			data : JSONNewPayment(recipient, account, amount, currency, managerId),
			success : function() {
				toastr.success("Uplata je uspešno izvršena.");
				setTimeout(function(){
					   window.location.href = "/managerPayments.html";
					}, 2000);
			},
			error : function(e) {
				toastr.error("Dogodila se greška!");
			} 
		});
	}
}


function loadPayments(){
	var table = document.getElementById("paymentsBody");
	var pagination = document.getElementById("managerPaymentsPag");
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "/payments/all?page=" + currentPage,
		success : function(data) {
			fillInTable(data, table);
			
			if (data.totalPages > 1) {
				setupPagination(data.totalPages, data.last, data.first, data.number);
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
	$("#paymentsBody tr").remove(); 
	
	data = data.content;
	
	var list = data == null ? [] : (data instanceof Array ? data
			: [ data ]);
	$.each(list, function(index, payment) {
		var row = table.insertRow(-1);
		row.style.cursor = "pointer";
		row.classList.add('tableHover');

		var dateP = row.insertCell(0);
		var recipient = row.insertCell(1);
		var payer = row.insertCell(2);
		var amount = row.insertCell(3);
		var currency = row.insertCell(4);
		
		recipient.innerHTML = payment.recipient;
		payer.innerHTML = payment.manager.name + " " + payment.manager.surname
		amount.innerHTML = payment.amount;
		currency.innerHTML = payment.currency;
		
		var date = new Date(payment.date);
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
		
		dateP.innerHTML = date;
	});
}


function setupPagination(totalPages, isLast, isFirst, tmpPage) {
	
	currentPage = tmpPage;
	
	var pagination = document.getElementById("managerPaymentsPag");
	
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
			
			getPagePayments(i - 1);
			
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
				
				getPagePayments(i);
				
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
			
			getPagePayments(i + 1);
			
		};
	};
	aNext.onclick = getNextPage(currentPage);
	
	pagination.appendChild(aNext);

	if (isLast) {
		aNext.classList.add("disabled");
	}
}


function getPagePayments(pageNum) {
	console.log('a')
	currentPage = pageNum;
	loadPayments();
}

