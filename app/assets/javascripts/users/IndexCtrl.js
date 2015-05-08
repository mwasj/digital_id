(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('IndexCtrl', IndexCtrl);

    IndexCtrl.$inject = ['$scope', '$log', 'UserService'];


    function IndexCtrl($scope, $log, UserService)
    {
        console.log("IndexCtrl constructed.");

        $scope.digitalIds = [];

        $scope.cancelEvent = function(e)
        {
            if (e) {
                    e.originalEvent.cancelBubble=true;
                }
        }

        $scope.downloadDigitalId = function(digitalId)
        {

        }

        UserService.listDigitalIDs()
            .then(function(data)
            {
                parseFileList(data);
            }, function(error) {
                console.log(error);
            });

        function parseFileList(arr)
        {
            for(var i = 0; i < arr.length; i++)
            {
                $scope.digitalIds.push(arr[i]);
            }
        }
    }
})();