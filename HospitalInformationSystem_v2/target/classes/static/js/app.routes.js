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
    
    //MANAGER
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
    
    //PATIENT
     .state('patient', {
    	url: '/patient',
    	templateUrl : 'components/patient/patientMenu.html',
    	controller : 'CommonController',
    	controllerAs: 'commonCtrl'
    })
   
    .state('patient.patientProfile', {
    	url: '/profile',
    	templateUrl: 'components/patient/profile.html',
    	controller: "PatientProfileController",
    	controllerAs: 'patientProfileCtrl'
    	
    })

    //MEDICAL STAFF
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
    .state('medicalStaff.patientProfile', {
    	url: '/patient/profile/:id',
    	templateUrl: 'components/patient/profile.html',
    	controller: "PatientProfileController",
    	controllerAs: 'patientProfileCtrl'
    })
    .state('medicalStaff.patientRecord', {
    	url: '/patient/record/:id',
    	templateUrl: 'components/patient/record.html',
    	controller: "PatientRecordController",
    	controllerAs: 'patientRecordCtrl'
    })

} ]);