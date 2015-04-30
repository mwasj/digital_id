/**
 * Created by wasinski on 26/03/2015.
 */
(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    ModalInstanceCtrl.$inject = ['$scope', '$modalInstance', 'currentComponent', 'componentOptions'];

    /**
     * @namespace ProfileController
     */
    function ModalInstanceCtrl($scope, $modalInstance, currentComponent, componentOptions)
    {
        //Define custom object to hold our actions.
        function Command (id, command, iterations, interval) {
            this.id = id;
            this.command = command;
            this.iterations = iterations;
            this.interval = interval
        }

        $scope.hostName = currentComponent.hostName;
        $scope.userName = currentComponent.userName;
        $scope.password = currentComponent.password;
        $scope.componentType = currentComponent.componentType;
        $scope.actions = currentComponent.actions;
        $scope.currentDeviceType = currentComponent.currentDeviceType;
        $scope.commands = currentComponent.commands;

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

            console.log("From modal: " + currentComponent.id);
            $modalInstance.close(currentComponent);
        };

        $scope.cancel = function () {
            console.log("cancel clicked");
            $modalInstance.dismiss('cancel');
        };

        $scope.setAction = function(action)
        {
            $scope.componentType = action;
            $scope.componentLabel = action;
        };

        /*sendMessage((JSON.stringify({
            text: "example message: " + new Date().getDate()}
        )))*/

        $scope.createEmptyCommand = function (){
            console.log("createEmptyCommand called");
            $scope.commands.push(new Command($scope.commands.length, "",0,0))
        }
    };
})();