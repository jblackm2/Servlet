'use strict';

angular.module('myApp.serviceStatus', ['ngRoute'])


.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
  $routeProvider.when('/serviceStatus', {
    templateUrl: 'serviceStatus/serviceStatus.html',
    controller: 'serviceStatusCtrl'
  });
      $httpProvider.defaults.useXDomain = true;
      $httpProvider.defaults.withCredentials = true;
      delete $httpProvider.defaults.headers.common["X-Requested-With"];
      $httpProvider.defaults.headers.common["Accept"] = "application/json, text/plain, */*";
      $httpProvider.defaults.headers.common["Content-Type"] = "application/json";

}])

    .controller('serviceStatusCtrl', ['$scope', '$location', '$http', '$interval', '$window', function($scope, $location, $http, $interval, $window) {

      //Called one time to initially populate the view with data
      $http({
        url:'/Servlet',
        method: "POST",
        data: 'message=' + 'service',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).success(function(response){$scope.status = response}).error(function(data, status){
        $scope.param1 = data;
        //$scope.param2 = data.statusText;
        if($scope.param1 == null){
          $scope.param1 = 404;
        }
        console.log(data + " " + "Error code: " + status);
        $scope.go('errorStatus', $scope.param1 + " " + "Error code: " +  status);
        });

      //Called at a given interval to continuously poll the servlet
        var interval = $interval(function(){$http({
          url:'/Servlet',
          method: "POST",
          data: 'message=' + 'service',
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(response){$scope.status = response; $scope.clearCount()}).error(function(data, status, config, statusText){
          console.log("Data: " + data + " " + "Status: " + status + " C: " + config + " Text: " + statusText);
          $scope.param1 = data;
          //$scope.param2 = data.statusText;
          if($scope.param1 == null){
           $scope.param1 = 404;
          }

          console.log(data + " " + "Error code: " + status);
          $scope.go('errorStatus', $scope.param1 + " " + "Error code: " +  status);
        });}, 10000); //Current interval in milliseconds

        $scope.$on('$destroy',function(){ //Cancels the interval if the view is destroyed, stops the servlet from being overwhelmed
          $interval.cancel(interval);

        });

        $scope.go = function(path, data){  //Will go to error display page
          $location.path(path).search('param', data);
      };

      $scope.getList = function() { //Gets data for environment list
        $scope.time = new Date(); // Placed here because I know this will get called every 10 seconds at the latest
        return $scope.status.Individual_lists[0].Env_list;
      };

      $scope.show=function(environment){ //Called to display the Environment details
        $scope.myValue = "true";
        console.log(environment);
        $scope.env = environment;
      };

      $scope.show2=function(name){ //Called to display Team Details
        console.log(name);
        $scope.clearCount();
        $scope.myValue2 = "true";
        $scope.group = name;
      };

      $scope.show3=function(name){// Called to display specific details about a single server
        console.log(name);
        $scope.myValue3 = "true";
        $scope.id = name;
      };

      $scope.hide = function(){ // Called to hide details for a specific server when changing the environment selected
        $scope.myValue3 = "";
      };

      $scope.getDetail = function(name){ //Getting first level of detail for individual groups within a specific environment
        //$scope.myValue = "true";
        if(name == "DIT"){
          return $scope.status.Individual_lists[0].DIT;
        }
        else if(name == "PERF"){
          return $scope.status.Individual_lists[0].PERF;
        }
        else if(name == "Stage"){
          return $scope.status.Individual_lists[0].Stage;
        }
        else if(name == "PROD"){
          return $scope.status.Individual_lists[0].PROD;
        }

      };

      $scope.getDetail1 = function(group, env){ //Getting list of individual servers within a team subgroup of an environment(DIT:Auth:server name)

        if(env == "DIT"){
          return $scope.status.Individual_lists[0].DIT_list;
        }
        else if(env == "PERF"){
          return $scope.status.Individual_lists[0].PERF_list;
        }
        else if(env == "Stage"){
          return $scope.status.Individual_lists[0].Stage_list;
        }
        else if(env == "PROD"){
          return $scope.status.Individual_lists[0].PROD_list;
        }

      };

      var oldDetails = ''; //Used to make a copy of previous page display, will show if servlet has sent only the PROD results, or everything but PROD.

      $scope.getDetail2 = function(){ //Getting specific details for an individual server
        if($scope.status.Individual_lists[0].Details.length == 522){ //522 because it is the expected length if both PROD and non-PROD details have been included. Need a better method for this.
          oldDetails = angular.copy($scope.status.Individual_lists[0].Details);
          console.log($scope.status.Individual_lists[0].Details.length);
          return $scope.status.Individual_lists[0].Details;
        }

        else{
          console.log($scope.status.Individual_lists[0].Details.length);
          return oldDetails;
        }

      };

      $scope.columnBreak = 6; //Max number of columns

      $scope.columnCount = 0;

      $scope.clearCount = function () {
          $scope.columnCount = 0;
      };

      $scope.count = function () {
        $scope.columnCount+=1;
      };

      $scope.expanded = true;

      $scope.expand = function(){
        console.log($scope.expanded);
        if($scope.expanded == false){
          $scope.expanded = true;
        }
        else if($scope.expanded == true){
          $scope.expanded = false;
        }
      }

      $scope.startNewRow = function (index, count) { //Used to set column during an ng-repeat
        return ((index) % count) === 0;
      };

      $scope.newWindow = function(url) { //Opens a new window with the passed in url from the serviceStatus
        if(url != null){
          console.log(url);
          $window.open("http://"+url, '_blank');
        }

      }

}]);