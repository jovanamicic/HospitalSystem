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
    
//   Routes and subroutes for manager. 
    .state('manager', {
    	url: '/manager',
    	templateUrl : 'components/manager/menu.html',
    	controller : 'ManagerController',
    	controllerAs: 'managerCtrl'
    })
	.state('manager.profile', {
    	url: '/profile',
    	templateUrl: 'components/manager/profile.html',
    })
	.state('manager.passwordChange', {
    	url: '/passwordChange',
    	templateUrl: 'components/manager/changePassword.html',
    })
    .state('manager.operations', {
    	url: '/operations',
    	templateUrl: 'components/manager/operations.html',
    	controller : 'ManagerOperationsController',
    	controllerAs: 'operationsCtrl'
    })
    
    
     .state('patient', {
    	url: '/patient',
    	templateUrl : 'components/patient/patientMenu.html',
    	controller : 'CommonController',
    	controllerAs: 'commonCtrl'
    })
    .state('patientProfile', {
    	url: '/profile/:id',
    	templateUrl: 'components/patient/profile.html',
    })
    
    
    .state('medicalStaff', {
    	url: '/medicalStaff',
    	templateUrl : 'components/medicalStaff/medicalStaffMenu.html',
    	controller : 'CommonController',
    	controllerAs: 'commonCtrl'
    })
    .state('medicalStaff.patients', {
    	url: '/patients',
    	templateUrl: 'components/medicalStaff/doctorAllPatients.html',
    	controller : 'MedicalStaffPatientsController',
    	controllerAs: 'medicalStaffPatientsCtrl'
    })
    .state('medicalStaff.patientRegistration', {
    	url: '/patientRegistration',
    	templateUrl: 'components/medicalStaff/doctorPatientRegistration.html',
    	controller: 'MedicalStaffPatientRegistrationController',
    	controllerAs: 'patientRegCtrl'
    })
    .state('medicalStaff.schedule', {
    	url: '/medicalStaffSchedule',
    	templateUrl: 'components/medicalStaff/doctorSchedule.html',
    	controller : 'MedicalStaffScheduleController',
    	controllerAs: 'medicalStaffScheduleCtrl'
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