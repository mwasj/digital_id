(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('IndexCtrl', IndexCtrl);

    IndexCtrl.$inject = ['$scope', '$log', 'UserService'];


    function IndexCtrl($scope, $log, UserService) {
        console.log("IndexCtrl constructed.");

        $scope.digitalIds = [];

        UserService.listDigitalIDs()
            .then(function(data) {
                console.log(data);
                $scope.digitalIds = data;
            }, function(error) {
                console.log(error);
            });
    }
})();