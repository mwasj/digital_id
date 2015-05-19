 (function () {
    'use strict';

    angular
        .module('myApp')
        .controller('NewHostCtrl', NewHostCtrl);

    NewHostCtrl.$inject = ['$scope', '$modal', '$log', 'UserService'];

    function NewHostCtrl($scope, $modal, $log, UserService)
    {

        //Define custom object to hold user defined commands.
        function Command (commandString, interval, comparable)
        {
            this.commandString = commandString;
            this.interval = interval;
            this.comparable = comparable;
        }

        //Define custom object to hold predefined actions.
        function Action (actionIndex, description, commands)
        {
            this.actionIndex = actionIndex;
            this.description = description;
            this.commands = commands;
        }

        console.log("NewHostCtrl constructed.");

        $scope.hosts = [];
        $scope.switches = [];
        $scope.arrays = [];
        $scope.digitalIdName = "";
        $scope.digitalIdAuthor = "";
        $scope.currentComponent = {};

        $scope.hostName = undefined;
        $scope.userName = undefined;
        $scope.password = undefined;

        //Windows Host, Cisco Switch etc.
        $scope.componentType = undefined;
        
        //Host, Switch, Inserv
        $scope.currentDeviceType = undefined;

        $scope.dropdownisopen = undefined;

        //Temporary, for testing purposes.
        $scope.actions = [];
        var commands = [];
        $scope.actions.push(new Action(1, "Run sg3 Utils", commands))

        $scope.open = function (deviceType, component, idx, e)
        {
            var edit = false;
            var currentComponent = {};

            currentComponent.currentDeviceType = deviceType;
            $scope.currentDeviceType = deviceType;
            currentComponent.actions = $scope.actions;


            if(component != undefined)
            {
                currentComponent.hostName = component.hostName;
                currentComponent.userName = component.userName;
                currentComponent.password = component.password;
                currentComponent.componentType = component.componentType;
                currentComponent.actions = component.actions;
                currentComponent.id = component.id;

                edit = true;
            }

            //Cancel bubble.
            if (e)
            {
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
                controller: 'NewComponentModalCtrl',
                backdrop: 'static',
                keyboard: false,
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

            $scope.removeArray = function (array, e)
            {
                    if (e) {
                        e.originalEvent.cancelBubble=true;
                    }

                    for(var i = 0; i < $scope.arrays.length; i++)
                    {
                        if(array === $scope.arrays[i])
                        {
                            $scope.arrays.splice(i, 1);
                        }
                    }
            }

            $scope.removeHost = function (host, e)
            {
                    if (e) {
                        e.originalEvent.cancelBubble=true;
                    }

                    for(var i = 0; i < $scope.hosts.length; i++)
                    {
                        if(host === $scope.hosts[i])
                        {
                            $scope.hosts.splice(i, 1);
                        }
                    }
            }

            $scope.removeSwitch = function (s, e)
            {
                    if (e) {
                        e.originalEvent.cancelBubble=true;
                    }

                    for(var i = 0; i < $scope.switches.length; i++)
                    {
                        if(s === $scope.switches[i])
                        {
                            $scope.switches.splice(i, 1);
                        }
                    }
            }
        };

        //Called after Build Digital ID button is clicked.
        $scope.buildDigitalID = function ()
        {
            console.log("Building DigitalID");

            var digitalId = {};
            digitalId.hosts = $scope.hosts;
            digitalId.switches = $scope.switches;
            digitalId.arrays = $scope.arrays;
            digitalId.digitalIdName = $scope.digitalIdName;
            digitalId.digitalIdAuthor = $scope.digitalIdAuthor;

            var progressWindow = $modal.open({
                    templateUrl: '/assets/partials/build-updater.html',
                    controller: 'DigitalIdProgressCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    windowClass: 'large-Modal',
                    resolve:
                        {
                            digitalID: function () {
                                return digitalId;
                            }
                        }
                    });
        }



}})();