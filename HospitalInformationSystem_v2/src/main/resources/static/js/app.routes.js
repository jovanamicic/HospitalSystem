var kmjRealEstate = angular.module('hospitalApp.routes', ['ui.router']);
kmjRealEstate.config([ '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/login');
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
    	templateUrl : 'components/manager/managerMenu.html',
    	controller : 'CommonController',
    	controllerAs: 'commonCtrl'
    })
	.state('manager.profile', {
    	url: '/profile',
    	templateUrl: 'components/manager/profile.html',
    	controller : 'ManagerController',
    	controllerAs: 'managerCtrl'
    })
	.state('manager.passwordChange', {
    	url: '/passwordChange',
    	templateUrl: 'components/manager/changePassword.html',
    	controller : 'ManagerController',
    	controllerAs: 'managerCtrl'
    })
	.state('manager.payments', {
    	url: '/payments',
    	templateUrl: 'components/manager/payments.html',
    	controller : 'PaymentsController',
    	controllerAs: 'paymentsCtrl'
    })
    
	.state('manager.newPayment', {
    	url: '/newPayment',
    	templateUrl: 'components/manager/newPayment.html',
    	controller : 'NewPaymentController',
    	controllerAs: 'newPaymentCtrl'
    })
    .state('manager.operations', {
    	url: '/operations',
    	templateUrl: 'components/manager/operations.html',
    	controller : 'ManagerOperationsController',
    	controllerAs: 'operationsCtrl'
    })
    .state('manager.operation', {
    	url: '/operation/:id',
    	params: {
    		id: null,
            isManager: false
        },
    	templateUrl: 'components/operation/operationPreview.html',
    	controller : 'OperationController',
    	controllerAs: 'operationCtrl'
    })
    .state('manager.examinations', {
    	url: '/examinations',
    	templateUrl: 'components/manager/examinations.html',
    	controller : 'ManagerExaminationsController',
    	controllerAs: 'examinationsCtrl'
    })
    .state('manager.examination', {
    	url: '/examination/:id',
    	params: {
    		id: null,
            isManager: false
        },
    	templateUrl: 'components/examination/examinationPreview.html',
    	controller : 'ExaminationController',
    	controllerAs: 'examinationCtrl'
    })
    
    //PATIENT
     .state('patient', {
    	url: '/patient',
    	templateUrl : 'components/patient/patientMenu.html',
    	controller : 'CommonController',
    	controllerAs: 'commonCtrl'
    })
   
    .state('patient.profile', {
    	url: '/profile',
    	templateUrl: 'components/patient/patientProfile.html',
    	controller: "PatientProfileController",
    	controllerAs: 'patientProfileCtrl'
    })
     .state('patient.changePassword', {
    	url: '/changePassword',
    	templateUrl: 'components/patient/patientChangePassword.html',
    	controller: "PatientProfileController",
    	controllerAs: 'patientProfileCtrl'
    })
    .state('patient.record', {
    	url: '/record',
    	templateUrl: 'components/patient/patientRecord.html',
    	controller: "PatientRecordController",
    	controllerAs: 'patientRecordCtrl'
    })
    .state('patient.schedule', {
    	url: '/schedule',
    	templateUrl: 'components/patient/patientSchedule.html',
    	controller: "PatientScheduleController",
    	controllerAs: 'patientScheduleCtrl'
    })
    .state('patient.operation', {
    	url: '/operation/:id',
    	params: {
    		id: null,
            isManager: false
        },
    	templateUrl: 'components/operation/operationPreview.html',
    	controller : 'OperationController',
    	controllerAs: 'operationCtrl'
    })
     .state('patient.examination', {
    	url: '/examination/:id',
    	params: {
    		id: null,
            isManager: false
        },
    	templateUrl: 'components/examination/examinationPreview.html',
    	controller : 'ExaminationController',
    	controllerAs: 'examinationCtrl'
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
    	templateUrl: 'components/patient/patientProfile.html',
    	controller: "PatientProfileController",
    	controllerAs: 'patientProfileCtrl'
    })
    .state('medicalStaff.patientRecord', {
    	url: '/patient/record/:id',
    	templateUrl: 'components/patient/patientRecord.html',
    	controller: "PatientRecordController",
    	controllerAs: 'patientRecordCtrl'
    })
    .state('medicalStaff.operation', {
    	url: '/operation/:id',
    	params: {
    		id: null,
            isManager: false
        },
    	templateUrl: 'components/operation/operationPreview.html',
    	controller : 'OperationController',
    	controllerAs: 'operationCtrl'
    })
     .state('medicalStaff.examination', {
    	url: '/examination/:id',
    	params: {
    		id: null,
            isManager: false
        },
    	templateUrl: 'components/examination/examinationPreview.html',
    	controller : 'ExaminationController',
    	controllerAs: 'examinationCtrl'
    })

} ]);