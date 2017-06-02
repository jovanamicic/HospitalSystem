var kmjRealEstate = angular.module('hospitalApp.routes', ['ui.router']);
kmjRealEstate.config([ '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/');
    $stateProvider
    
    .state('login', {
    	url: '/login',
    	templateUrl: 'components/login/login.html',
    	controller : 'LoginController',
    	controllerAs: 'loginCtrl'
    })
    
    .state('patentProfile', {
    	url: '/patientProfile',
    	templateUrl: 'components/patient/profile.html',
    })
    
    
    .state('doctorProfile', {
    	url: '/doctorProfile',
    	templateUrl: 'components/doctor/profile.html',
    })
    

    .state('managerProfile', {
    	url: '/managerProfile',
    	templateUrl: 'components/manager/profile.html',
    })
    
//    //rute za usera i njegove podrute
//    .state('userProfile', {
//    	url: '/userProfile',
//    	templateUrl : 'static/js/components/profile/userProfile.html',
//    	controller : 'UserController',
//    	controllerAs: 'userCtrl'
//    })
//    .state('userProfile.userDetails', {
//    	url: '/userDetails', 
//    	templateUrl: 'static/js/components/profile/userDetails.html',
//    })
	
} ]);