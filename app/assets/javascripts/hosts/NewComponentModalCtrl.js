/**
 * Represents the controller responsible for handling of modal window for adding new components.
 */
(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('NewComponentModalCtrl', NewComponentModalCtrl);

    NewComponentModalCtrl.$inject = ['$scope', '$modalInstance', 'currentComponent', 'componentOptions'];

    /**
     * @namespace ProfileController
     */
    function NewComponentModalCtrl($scope, $modalInstance, currentComponent, componentOptions)
    {
        //Define custom object to hold our commands.
        function Command (command, interval, comparable) {
            this.command = command;
            this.interval = interval;
            this.comparable = comparable;
        }

        $scope.hostName = currentComponent.hostName;
        $scope.userName = currentComponent.userName;
        $scope.password = currentComponent.password;
        $scope.componentType = currentComponent.componentType;
        $scope.actions = currentComponent.actions;
        $scope.currentDeviceType = currentComponent.currentDeviceType;
        $scope.commands = currentComponent.commands;

        //Temporary TODO - delete.
        //$scope.commands.push(new Command("systeminfo",0));
        //$scope.componentType = componentOptions[0];

        //Temporary to save time.
        //$scope.hostName = "dl380pg8-74";
        //$scope.userName = "Administrator";
        //$scope.password = "ssmssm";

        $scope.componentLabel = $scope.componentType == undefined ? "Select " + currentComponent.currentDeviceType + " Type":  $scope.componentType;

        $scope.componentOptions = componentOptions;

        $scope.ok = function ()
        {
            currentComponent.hostName = $scope.hostName;
            currentComponent.userName = $scope.userName;
            currentComponent.password =  $scope.password;
            currentComponent.componentType = $scope.componentType;
            currentComponent.commands = $scope.commands;
            console.log("New component window, ok clicked.");
            $modalInstance.close(currentComponent);
        };

        $scope.cancel = function () {
            console.log("New component window, cancel clicked.");
            $modalInstance.dismiss('cancel');
        };

        $scope.setAction = function(action)
        {
            $scope.componentType = action;
            $scope.componentLabel = action;
        };


        $scope.createEmptyCommand = function ()
        {
            console.log("createEmptyCommand called");
            $scope.commands.push(new Command("",0, false));
        }

        $scope.removeCommand = function (command)
        {
            for(var i = 0; i < $scope.commands.length; i++)
            {
                if(command === $scope.commands[i])
                {
                    console.log("Command removed.");
                    $scope.commands.splice(i, 1);
                }
            }
        }

        $scope.toggleSelection = function (command)
        {
             for(var i = 0; i < $scope.commands.length; i++)
             {
                 if(command === $scope.commands[i])
                 {
                     $scope.commands[i].comparable = ! $scope.commands[i].comparable;
                     console.log($scope.commands[i]);
                 }
             }
        }
    };
})();