var app = angular
		.module(
				'hospitalApp',
				[ 'hospitalApp.controllers', 'hospitalApp.services',
						'hospitalApp.routes', 'ui.router', 'LocalStorageModule' ])
		.config(
				function($httpProvider) {
					$httpProvider.interceptors
							.push([
									'$q',
									'$location',
									'localStorageService',
									function($q, $location, localStorageService) {
										return {
											'request' : function(config) {
												config.headers = config.headers
														|| {};
												if (localStorage
														.getItem('token')) {
													config.headers['X-Auth-Token'] = localStorage
															.getItem('token');
												}
												return config;
											},
											'responseError' : function(response) {
												if (response.status === 401
														|| response.status === 403) {
													$location.path('/');
												}
												return $q.reject(response);
											}
										};
									} ]);
				});

// var apiRoot = 'http://localhost:8080/api/'