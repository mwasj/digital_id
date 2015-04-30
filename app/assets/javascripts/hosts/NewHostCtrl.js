 (function () {
    'use strict';

    angular
        .module('myApp')
        .controller('NewHostCtrl', NewHostCtrl);

    NewHostCtrl.$inject = ['$scope', '$modal', '$log', 'UserService'];

    function NewHostCtrl($scope, $modal, $log, UserService) {
        console.log("NewHostCtrl constructed.");

        $scope.hosts = [];
        $scope.switches = [];
        $scope.arrays = [];
        $scope.digitalIdName = "";
        console.log("The name of this digital id is: " + $scope.digitalIdName);
        $scope.currentComponent = {};

        $scope.hostName = undefined;
        $scope.userName = undefined;
        $scope.password = undefined;

        //Windows Host, Cisco Switch etc.
        $scope.componentType = undefined;
        
        //Host, Switch, Inserv
        $scope.currentDeviceType = undefined;

        $scope.dropdownisopen = undefined;

        $scope.open = function (deviceType, component, idx, e)
        {
            var edit = false;
            var currentComponent = {};

            currentComponent.currentDeviceType = deviceType;
            $scope.currentDeviceType = deviceType;
            currentComponent.commands = [];

            if(component != undefined)
            {
                currentComponent.hostName = component.hostName;
                currentComponent.userName = component.userName;
                currentComponent.password = component.password;
                currentComponent.componentType = component.componentType;
                currentComponent.commands = component.commands;
                currentComponent.id = component.id;

                console.log(component);
                edit = true;
            }

            if (e) {
                e.originalEvent.cancelBubble=true;
            }

            var componentOptions = [];

            switch(deviceType)
            {
                case 'Host':
                    componentOptions = ['Windows', 'Linux'];
                    break;
                case 'Switch':
                    componentOptions = ['Cisco', 'Qlogic', 'Brocade'];
                    break;
                 case 'Array':
                    break;
            }

            var modalInstance = $modal.open({
                templateUrl: '/assets/partials/new-host.html',
                controller: 'ModalInstanceCtrl',
                transclude: true,
                resolve:
                {
                    currentComponent: function () {
                        return currentComponent;
                    },
                    componentOptions: function () {
                        return componentOptions;
                    }
                }
            });

            modalInstance.result.then(function (result)
            {
                var currentObject = result;
                console.log(currentObject);

                if(edit)
                {
                    console.log("Editing array");
                    switch ($scope.currentDeviceType) {
                        case 'Host':
                            $scope.hosts[currentObject.id] = currentObject;
                            break;
                        case 'Switch':
                            $scope.switches[currentObject.id] = currentObject;
                            break;
                        case 'Array':
                            $scope.arrays[currentObject.id] = currentObject;
                            break;
                    }

                }
                else {
                    console.log("Adding to array");
                    switch ($scope.currentDeviceType) {
                        case 'Host':
                            currentObject.id = $scope.hosts.length;
                            $scope.hosts.push(currentObject);
                            break;
                        case 'Switch':
                            currentObject.id = $scope.switches.length;
                            $scope.switches.push(currentObject);
                            break;
                        case 'Array':
                            currentObject.id = $scope.arrays.length;
                            $scope.arrays.push(currentObject);
                            break;
                    }
                }
                console.log("Hosts: " + $scope.hosts);
                console.log("Switches: " + $scope.switches);
                console.log("Arrays: " + $scope.arrays);

                $scope.hostName = undefined;
                $scope.userName = undefined;
                $scope.password = undefined;
                $scope.hostType = undefined;
                $scope.currentDeviceType = undefined;

            }, function (){
                $log.info("Modal dismissed.");
            });
        };

        $scope.buildDigitalID = function ()
        {
            console.log("Building DigitalID");
            var digitalId = {};
            digitalId.hosts = $scope.hosts;
            digitalId.switches = $scope.switches;
            digitalId.arrays = $scope.arrays;

            var modalInstance2 = $modal.open({
                            templateUrl: '/assets/partials/build-updater.html',
                            controller: 'BuildProgressCtrl',
                            backdrop: 'static',
                            keyboard: false,
                            resolve:
                                {
                                    digitalID: function () {
                                        return digitalId;
                                 }
                            }});

}}})();