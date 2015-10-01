'use strict';

angular.module('myApp.hostStatus', ['ngRoute'])

.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
  $routeProvider.when('/hostStatus', {
    templateUrl: 'hostStatus/hostStatus.html',
    controller: 'hostStatusCtrl'
  });
        $httpProvider.defaults.useXDomain = true;
        $httpProvider.defaults.withCredentials = true;
        delete $httpProvider.defaults.headers.common["X-Requested-With"];
        $httpProvider.defaults.headers.common["Accept"] = "application/json";
        $httpProvider.defaults.headers.common["Content-Type"] = "application/json";

}])

.controller('hostStatusCtrl', ['$scope', '$location', '$http', '$interval', function($scope, $location, $http, $interval) {

        //Called once to initially populate the view with data from the servlet
        $http({
            //url:'http://localhost:8080/Servlet',
            url:'/Servlet',
            method: "POST",
            data: 'message=' + 'host',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(response){$scope.host = response}).error(function(data, status){
            $scope.param1 = data.status;
            //$scope.param2 = data.statusText;
            if(param1 == null){
                $scope.param1 = 404;
            }

            $scope.go('errorStatus', $scope.param1 + " " + "Error code: " +  status);
        });

        // Called continuously on a set interval to poll the servlet
        var interval = $interval(function(){$http({
            url:'/Servlet',
            method: "POST",
            data: 'message=' + 'host',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(response){$scope.host = response;$scope.clearCount()}).error(function(data, status){
            $scope.param1 = data.status;
            //$scope.param2 = data.statusText;
            if($scope.param1 == null){
                $scope.param1 = 404;
            }

            $scope.go('errorStatus', $scope.param1 + " " + "Error code: " +  status);
        });}, 10000); // Interval set in milliseconds

        $scope.$on('$destroy',function(){ //Cancels the interval if the view is destroyed, stops the servlet from being overwhelmed
            $interval.cancel(interval);
        });

    $scope.go = function(path, data){ //Goes to the errror status page
        $location.path(path).search('param', data);
    };

    $scope.getList = function() { //Gets the data of the individual environments from the JSON object send by the servlet
        $scope.time = new Date(); // Placed here because I know this will get called every 10 seconds at the latest
        return $scope.host.Individual_lists[0].Env_list;
    };

    $scope.show=function(environment){ //Show the individual teams for the selected environment
        console.log(environment);
        $scope.myValue = "true";
        $scope.env = environment;
    };

    $scope.show2=function(name){ //Show the individual servers for the selected team
        console.log(name);
        $scope.clearCount();
        $scope.myValue2 = "true";
        $scope.group = name;

    };

    $scope.getDetail1 = function(name){ //Gets the data of individual teams specific to the previously selected environment
        if(name == "DIT"){
            var len = $scope.host.Individual_lists[0].DIT.length;
            console.log("1: " + len);
            return $scope.host.Individual_lists[0].DIT;
        }
        else if(name == "PERF"){
            var len = $scope.host.Individual_lists[0].PERF.length;
            console.log("2: " + len);
            return $scope.host.Individual_lists[0].PERF;
        }
        else if(name == "Stage"){
            var len = $scope.host.Individual_lists[0].Stage.length;
            console.log("3: " + len);
            return $scope.host.Individual_lists[0].Stage;
        }
        else if(name == "PROD"){
            var len = $scope.host.Individual_lists[0].PROD.length;
            console.log("4: " + len);
            return $scope.host.Individual_lists[0].PROD;
        }

    };

    $scope.getDetail2 = function(group, env){ //Get data for the individual servers based on the previously selected group and environment
        if(env == "DIT"){
            var len = $scope.host.Individual_lists[0].DIT_list.length;
            console.log(len);
            return $scope.host.Individual_lists[0].DIT_list;
        }
        else if(env == "PERF"){
            var len = $scope.host.Individual_lists[0].PERF_list.length;
            console.log(len);
            return $scope.host.Individual_lists[0].PERF_list;
        }
        else if(env == "Stage"){
            var len = $scope.host.Individual_lists[0].Stage_list.length;
            console.log(len);
            return $scope.host.Individual_lists[0].Stage_list;
        }
        else if(env == "PROD"){
            var len = $scope.host.Individual_lists[0].PROD_list.length;
            console.log(len);
            return $scope.host.Individual_lists[0].PROD_list;
        }

    };

    $scope.columnCount = 0;

    $scope.clearCount = function () {
        $scope.columnCount = 0;
    };
    $scope.count = function () {
        $scope.columnCount+=1;
    };

    $scope.range = function(n) {
        return new Array(n);
    };

    $scope.columnBreak = 3; //Max number of columns

    $scope.startNewRow = function (index, count) { //Used to create columns in the view
        return ((index) % count) === 0;
    };

}]);

