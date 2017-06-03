angular.module('hospitalApp.services').factory('commonService',
		function($http) {
			var service = {
				getPersonByToken : getPersonByToken
			}
			return service;

			function getPersonByToken() {
				return $http.get('persons/personByToken')
			};
			
		})
