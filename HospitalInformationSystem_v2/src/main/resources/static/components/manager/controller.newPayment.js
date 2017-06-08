angular.module('hospitalApp.controllers').controller('NewPaymentController',
		NewPaymentController)

NewPaymentController.$inject = [ '$location', '$stateParams',
		'managerService', 'localStorageService', '$http', '$scope', '$state'];

function NewPaymentController($location, $stateParams, managerService, localStorageService, $http, $scope, $state) {

	var vm = this;
	
	vm.newPayment = {
			account: '',
			currency: '-',
			recipient: '-',
			amount: ''
	}

	vm.getAllRecipients = function() {
		managerService.getAllDoctors()
		.then(function(result) {
			var data = result.data;
			
			vm.recipients = []
			vm.recipients.push({
				'name' : "- Primalac -",
				'value' :  "-"
			});
			
			for (var i = 0; i < data.length; i++) { 
			    var text = data[i].name + " " + data[i].surname;
			    var option = {
						'name' : text,
						'value' : text
					}
					vm.recipients.push(option);
			}
			vm.newPayment.recipient = vm.recipients[0].value;
		})
		.catch(function() {
			vm.errorMessage = "Error loading all doctors.";
		});
	}
	vm.getAllRecipients();
	
	var loadCurrencies = function() {
		vm.currencies = []
		
		vm.currencies.push({
			'name' : '- Valuta -',
			'value' : '-'
		});
		
		var data = ['RSD', 'EUR']
		for (var i = 0; i < data.length; i++) { 
		    var option = {
					'name' : data[i],
					'value' : data[i]
				}
			vm.currencies.push(option);
		}
		vm.newPayment.currency = vm.currencies[0].value;
	}
	loadCurrencies();
	
	vm.savePayment = function() {
		
		if (vm.newPayment.recipient == "-") {
			toastr.warning("Niste uneli primaoca!");
			return;
		}
		
		if (vm.newPayment.account == "" || vm.newPayment.account == null) {
			toastr.warning("Niste uneli broj računa!");
			return;
		}
		
		if (vm.newPayment.amount == "" || vm.newPayment.account == null) {
			toastr.warning("Niste uneli iznos!");
			return;
		}
		
		if (vm.newPayment.currency == "-") {
			toastr.warning("Niste uneli valutu!");
			return;
		}
		
		managerService.saveNewPayment(vm.newPayment)
		.then(function() {
			toastr.success("Nova uplata je snimljena.")
			
			vm.newPayment = {
				account: '',
				currency: '-',
				recipient: '-',
				amount: ''
			}
		})
		.catch(function() {
			vm.errorMessage = "Error saving new payment.";
		});
	}

}
