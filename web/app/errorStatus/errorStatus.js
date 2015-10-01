/**
 * Created by blackmju on 9/14/2015.
 */
'use strict';

angular.module('myApp.errorStatus', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/errorStatus', {
            templateUrl: 'errorStatus/errorStatus.html',
            controller: 'errorStatusCtrl'

        });

    }])

    .controller('errorStatusCtrl', ['$scope', '$location','$sce', function($scope, $location, $sce) {

        $scope.status = $location.search().param; //Gets the error information that was passed along
        $scope.test = $sce.trustAsHtml($scope.status); //Sets the data to be encoded as HTML if possible
    }]);
