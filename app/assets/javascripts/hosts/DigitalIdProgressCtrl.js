(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('DigitalIdProgressCtrl', DigitalIdProgressCtrl);

    DigitalIdProgressCtrl.$inject = ['$scope', '$modalInstance', 'digitalID', 'UserService'];

    function DigitalIdProgressCtrl($scope, $modalInstance, digitalID, UserService)
    {
        $scope.updates = [];

        var sessionName = Math.random().toString(36).substring(7);
        console.log("Your username is: " + sessionName);
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket

        var sock = new WS("ws://127.0.0.1:9000/digitalid/socketUpdater?sessionName="+sessionName)

        //Define custom object to hold our updates.
        function Update (id, text, data) {
            this.id = id;
            this.text = text;
            this.data = data;
        }

        sock.onmessage = function (evt)
        {
            //console.log(evt)
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
                var parsedContent = JSON.parse(obj.content);

                if(parsedContent.commandResponse != undefined)
                {
                    for(var i = 0; i < $scope.updates.length; i++)
                    {
                        if($scope.updates[i].id === parsedContent.id)
                        {
                            console.log(parsedContent.commandResponse.result);
                            $scope.updates[i].data = parsedContent.commandResponse.result
                        }
                    }
                }
                else
                {
                    $scope.updates.push(new Update(parsedContent.id, parsedContent.text, null));
                }
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

         $scope.cancel = function(){
                $modalInstance.dismiss('cancel');
         }
    };



})();