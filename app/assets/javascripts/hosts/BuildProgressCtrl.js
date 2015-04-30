(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('BuildProgressCtrl', BuildProgressCtrl);

    BuildProgressCtrl.$inject = ['$scope', '$modalInstance', 'digitalID', 'UserService'];

    function BuildProgressCtrl($scope, $modalInstance, digitalID, UserService)
    {
        $scope.updates = [];

        var sessionName = Math.random().toString(36).substring(7);
        console.log("Your username is: " + sessionName);
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket

                var sock = new WS("ws://127.0.0.1:9000/digitalid/socketUpdater?sessionName="+sessionName)

                sock.onmessage = function (evt)
                {
                    console.log(evt)
                    var obj = JSON.parse(evt.data)

                    if(obj.type === "analysis")
                    {
                        console.log(obj.content);
                        var obj2 = JSON.parse(obj.content);

                        for(var i = 0; i < obj2.length; i++)
                        {
                            console.log(obj2[i].hostName);
                            $scope.updates.push(obj2[i].hostName);
                            for(var y = 0; y < obj2[i].instructions.length; y++)
                            {
                                console.log(obj2[i].instructions[y].command);
                                $scope.updates.push(obj2[i].instructions[y].command);
                            }
                        }
                    }
                    else if(obj.type === "progressUpdate")
                    {
                        $scope.updates.push(obj.content);
                    }
                    $scope.$apply();
                }

        function sendMessage(msg){
                    // Wait until the state of the socket is not ready and send the message when it is...
                    waitForSocketConnection(sock, function(){
                        console.log("Message has been sent.");
                        sock.send(msg);
                    });
                }

        function postUpdate(){

        }

        // Make the function wait until the connection is made...
                function waitForSocketConnection(socket, callback){
                    setTimeout(
                        function () {
                            if (socket.readyState === 1) {
                                console.log("Websocket connection made")
                                if(callback != null){
                                    callback();
                                }
                                return;

                            } else {
                                console.log("wait for connection... " + socket.readyState)
                                waitForSocketConnection(socket, callback);
                            }

                        }, 5); // wait 5 milisecond for the connection...
                }

        UserService.buildDigitalID(digitalID, sessionName);
    };


})();