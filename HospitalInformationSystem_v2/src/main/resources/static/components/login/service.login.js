angular.module('hospitalApp.services').factory('loginService',
		function($http) {
			var service = {
				loginUser : loginUser
			}
			return service;
			
			function loginUser(user) {
				return $http.post("/persons/login", user)
			}

		})
