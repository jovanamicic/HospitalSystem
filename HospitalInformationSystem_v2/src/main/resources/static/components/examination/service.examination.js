angular.module('hospitalApp.services').factory('examinationService', function($http) {
	
	var service = {
			loadExamination : loadExamination,
			getTimes : getTimes,
			updateExamination : updateExamination
	}
	return service;
	
	function loadExamination(id) {
		return $http.get('examinations/' + id);
	}
	
	function getTimes(date, roomId) {
		return $http.get('/roomSchedules/available/' + roomId + '/' + date);
	}
	
	function updateExamination(examination) {
		return $http.put('examinations/saveTime', examination);
	}

})