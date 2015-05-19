/**
 * Represents the controller responsible for handling of modal window for adding new components.
 */
(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('NewComponentModalCtrl', NewComponentModalCtrl);

    NewComponentModalCtrl.$inject = ['$scope', '$modalInstance', 'currentComponent', 'componentOptions'];

    function NewComponentModalCtrl($scope, $modalInstance, currentComponent, componentOptions)
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

        $scope.hostName = currentComponent.hostName;
        $scope.userName = currentComponent.userName;
        $scope.password = currentComponent.password;
        $scope.componentType = currentComponent.componentType;
        $scope.actions = currentComponent.actions;
        $scope.currentDeviceType = currentComponent.currentDeviceType;
        $scope.actions = currentComponent.actions;
        $scope.predefinedActionLabel = "Add a predefined action";

        $scope.componentLabel = $scope.componentType == undefined ? "Select " + currentComponent.currentDeviceType + " Type":  $scope.componentType;

        $scope.componentOptions = componentOptions;

        $scope.ok = function ()
        {
            currentComponent.hostName = $scope.hostName;
            currentComponent.userName = $scope.userName;
            currentComponent.password =  $scope.password;
            currentComponent.componentType = $scope.componentType;
            currentComponent.actions = $scope.actions;
            currentComponent.predefinedActions= $scope.predefinedActions;
            $modalInstance.close(currentComponent);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.setAction = function(action)
        {
            $scope.componentType = action;
            $scope.componentLabel = action;
        };

        $scope.addAction = function (action)
        {
            $scope.actions.push(action)
        }

        $scope.addUserDefinedAction = function()
        {
            var commands = [];
            commands.push(new Command("",0,false));
            var action = new Action(0, "", commands);
            $scope.actions.push(action);
        }

        $scope.removeAction = function (action)
        {
            for(var i = 0; i < $scope.actions.length; i++)
            {
                if(action === $scope.actions[i])
                {
                    $scope.actions.splice(i, 1);
                }
            }
        }

        $scope.toggleSelection = function (action)
        {
             for(var i = 0; i < $scope.actions.length; i++)
             {
                 if(action === $scope.actions[i])
                 {
                     $scope.actions[i].commands[0].comparable = !$scope.actions[i].commands[0].comparable;
                 }
             }
        }
    };
})();