angular.module('hospitalApp.services').factory('patientService',
		function($http) {
			var service = {
				getPatient : getPatient,
				getDoctor : getDoctor,
				checkUsername : checkUsername,
				updatePatient : updatePatient,
				getLoggedPatient : getLoggedPatient,
				getSchedule : getSchedule,
				getOperationExaminationDetails : getOperationExaminationDetails
			}
			return service;

			function getPatient(id) {
				return $http.get('patients/'+id);
			}
			
			function getLoggedPatient() {
				return $http.get('patients/');
			}
			
			function getDoctor(id) {
				return $http.get('medicalstaff/'+id);
			}
			
			function checkUsername(username) {
				return $http.post('patients/username/', username);
			}
			
			function updatePatient(patient){
				return $http.put('patients/',patient);
			}
			
			function getSchedule() {
				return $http.get('patients/schedule');
			}
			
			function getOperationExaminationDetails(type, id) {
				return $http.get('patients/operationExaminationDetails/' + type + "/" + id);
			}
			
		})