angular.module('hospitalApp.services').factory('patientService',
		function($http) {
			var service = {
				getPatient : getPatient,
				getDoctor : getDoctor,
				checkUsername : checkUsername,
				updatePatient : updatePatient,
				getLoggedPatient : getLoggedPatient,
				changePassword : changePassword,
				getSchedule : getSchedule,
				getOperationExaminationDetails : getOperationExaminationDetails,
				saveExamination : saveExamination,
				getExaminationsPage : getExaminationsPage,
				getMyExaminationsPage : getMyExaminationsPage,
				getOperationsPage : getOperationsPage,
				getMyOperationsPage : getMyOperationsPage
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
			
			function changePassword(password){
				return $http.put('persons/password', password);
			}
			
			function getSchedule() {
				return $http.get('patients/schedule');
			}
				 			
			function getOperationExaminationDetails(type, id) {
				return $http.get('patients/operationExaminationDetails/' + type + "/" + id);
			}
			
			function saveExamination(e){
				return $http.post('/examinations', e);
			}
			
			function getExaminationsPage(page, id) {
				return $http.get('/examinations/patients/' + id +'?page=' + page)
			}
			
			function getMyExaminationsPage(page) {
				return $http.get('/examinations/my?page=' + page)
			}
			
			function getOperationsPage(page, id) {
				return $http.get('/operations/patient/' + id +'?page=' + page)
			}
			
			function getMyOperationsPage(page) {
				return $http.get('/operations/my?page=' + page)
			}
		})
