angular.module('hospitalApp.services').factory('medicalStaffService',
		function($http) {
			var service = {
				getPatients : getPatients,
				getMyPatients : getMyPatients,
				getPatientsBySearchData : getPatientsBySearchData,
				saveOperation : saveOperation,
				saveExamination : saveExamination,
				getSchedule : getSchedule
				getPatientsBySearchData : getPatientsBySearchData,
				checkEmail : checkEmail,
				checkBirthday : checkBirthday,
				getDoctors : getDoctors,
				addPatient : addPatient
			}
			return service;

			function getPatients() {
				return $http.get('patients/all')
			}
			
			function getMyPatients() {
				return $http.get('patients/my')
			}
			
			function getPatientsBySearchData(searchData) {
				return $http.get('patients/search/' + searchData)
			}
			
			function saveOperation(operation) {
				return $http.post('operations/scheduleOperation', operation)
			}
			
			function saveExamination(examination) {
				return $http.post('examinations/scheduleExamination', examination)
			}
			
			function getSchedule() {
				return $http.get('medicalStaff/schedule')
			}

		})
