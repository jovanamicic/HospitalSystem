angular.module('hospitalApp.services').factory('medicalStaffService',
		function($http) {
			var service = {
				getPatients : getPatients,
				getMyPatients : getMyPatients,
				getPatientsBySearchData : getPatientsBySearchData
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

		})
