angular.module('hospitalApp.services').factory('loginService',
		function($http) {
			var service = {
				loginPerson : loginPerson,
				getPersonRole : getPersonRole
			}
			return service;

			function loginPerson(person) {
				return $http.post('login', person)
			};
			
			function getPersonRole() {
				return $http.get('persons/roleByToken')
			}

		})
