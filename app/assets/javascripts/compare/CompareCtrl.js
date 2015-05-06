(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('CompareCtrl', CompareCtrl);

    CompareCtrl.$inject = ['$scope', '$log', 'UserService'];


    function CompareCtrl($scope, $log, UserService) {
        console.log("CompareCtrl constructed.");

        $scope.digitalIds = [];
        $scope.selection = [];

        UserService.listDigitalIDs()
            .then(function(data) {
                console.log(data);
                $scope.digitalIds = data;
            }, function(error) {
                console.log(error);
            });

        $scope.toggleSelection = function toggleSelection(digitalId) {
            var idx = $scope.selection.indexOf(digitalId);

            // is currently selected
            if (idx > -1) {
              $scope.selection.splice(idx, 1);
            }

            // is newly selected
            else {
              $scope.selection.push(digitalId);
            }

            console.log($scope.selection);
        };

        $scope.goCompare = function(){
            UserService.compareDigitalIDs($scope.selection)
                    .then(function(data) {
                        console.log(data);
                        $scope.digitalIds = data;
                    }, function(error) {
                        console.log(error);
                    });
        }
    }
})();