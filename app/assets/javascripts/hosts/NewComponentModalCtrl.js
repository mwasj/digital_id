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
        function Command (command, interval) {
            this.command = command;
        }

        $scope.hostName = currentComponent.hostName;
        $scope.userName = currentComponent.userName;
        $scope.password = currentComponent.password;
        $scope.componentType = currentComponent.componentType;
        $scope.actions = currentComponent.actions;
        $scope.currentDeviceType = currentComponent.currentDeviceType;
        $scope.commands = currentComponent.commands;

        //Temporary TODO - delete.
        $scope.commands.push(new Command("mpclaim -s -d 5",0));
        $scope.componentType = componentOptions[0];

        //Temporary to save time.
        $scope.hostName = "dl380pg8-73";
        $scope.userName = "Administrator";
        $scope.password = "ssmssm";

        $scope.componentLabel = $scope.componentType == undefined ? "Select " + currentComponent.currentDeviceType + " Type":  $scope.componentType;

        $scope.componentOptions = componentOptions;

        $scope.ok = function ()
        {

            currentComponent.hostName = $scope.hostName;
            currentComponent.userName = $scope.userName;
            currentComponent.password =  $scope.password;
            currentComponent.componentType = $scope.componentType;

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


        $scope.createEmptyCommand = function (){
            console.log("createEmptyCommand called");
            $scope.commands.push(new Command("",0));
        }
    };
})();