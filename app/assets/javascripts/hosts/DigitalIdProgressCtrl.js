(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('DigitalIdProgressCtrl', DigitalIdProgressCtrl);

    DigitalIdProgressCtrl.$inject = ['$scope', '$modalInstance', 'digitalID', 'UserService'];

    function DigitalIdProgressCtrl($scope, $modalInstance, digitalID, UserService)
    {
        $scope.updates = [];
        $scope.downloadReady = false;
        var sessionName = Math.random().toString(36).substring(7);
        $scope.digitalID = digitalID;
        $scope.downloadUrl = undefined;
        $scope.headerTitle = "Building digital ID ... ";

        console.log("Your username is: " + sessionName);
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket

        var sock = new WS("ws://127.0.0.1:9000/digitalid/socketUpdater?sessionName="+sessionName)

        //Define custom object to hold our updates.
        function Update (id, text, data, status) {
            this.id = id;
            this.text = text;
            this.data = data;
            this.status = status;
        }

        sock.onmessage = function (evt)
        {
            var obj = JSON.parse(evt.data)

            if(obj.type === "analysis")
            {
                var obj2 = JSON.parse(obj.content);

                for(var i = 0; i < obj2.length; i++)
                {
                    $scope.updates.push(obj2[i].hostName);
                    for(var y = 0; y < obj2[i].instructions.length; y++)
                    {
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
                            $scope.updates[i].data = parsedContent.commandResponse.result;
                            $scope.updates[i].status = parsedContent.commandResponse.commandResponseCode
                        }
                    }
                }
                else
                {
                    $scope.updates.push(new Update(parsedContent.id, parsedContent.text, null, null));
                }
            }
            else if(obj.type === "finish")
            {
                $scope.downloadReady = true;
                var parsedContent = JSON.parse(obj.content);
                $scope.downloadUrl = parsedContent.text
                $scope.headerTitle = "Building digital ID ... Complete!";
                console.log(parsedContent);
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