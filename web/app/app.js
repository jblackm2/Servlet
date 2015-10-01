'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.hostStatus',
  'myApp.serviceStatus',
  'myApp.errorStatus',
  'myApp.version'
]).
config(['$routeProvider',"$httpProvider", function($routeProvider, $httpProvider) {
  $routeProvider.otherwise({redirectTo: '/hostStatus'});
      $httpProvider.defaults.useXDomain = true;
      $httpProvider.defaults.withCredentials = true;
      delete $httpProvider.defaults.headers.common["X-Requested-With"];
      $httpProvider.defaults.headers.common["Accept"] = "application/json";
      $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
}]);
