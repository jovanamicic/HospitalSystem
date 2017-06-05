angular.module('hospitalApp').filter('dateFilter', function($filter) {
  return function(input) {
    return $filter('date')(input,'dd.MM.yyyy HH:mm'); //just use embedded angular date filter
  };
});