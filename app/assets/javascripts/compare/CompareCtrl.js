(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('CompareCtrl', CompareCtrl);

    CompareCtrl.$inject = ['$scope', '$log', 'DigitalIdService', '$parse'];

    function CompareCtrl($scope, $log, DigitalIdService, $parse)
    {
        console.log("CompareCtrl constructed.");

        //Define custom object to hold the content retrieved when a digitalid is opened
        function ContentDto (displayString, content)
        {
            this.displayString = displayString;
            this.content = content;
        }

        $scope.digitalIds = [];
        $scope.selection = [];
        $scope.generationInProgress = false;
        $scope.htmlString = '';
        $scope.digitalIdBefore = [];
        $scope.digitalid_before = undefined;
        $scope.digitalid_before_array = [];
        $scope.digitalid_after = undefined;
        $scope.digitalid_after_array = [];

        DigitalIdService.listDigitalIDs()
            .then(function(data) {
                console.log(data);
                $scope.digitalIds = data;
            }, function(error) {
                console.log(error);
            });

        $scope.toggleSelection = function toggleSelection(digitalId, e)
        {
            if (e) {
                e.originalEvent.cancelBubble=true;
            }

            var idx = $scope.selection.indexOf(digitalId.url);

            // is currently selected
            if (idx > -1) {
              $scope.selection.splice(idx, 1);
            }

            // is newly selected
            else {
              $scope.selection.push(digitalId.url);
            }

            console.log($scope.selection);
        };

        $scope.goCompare = function()
        {
            console.log($scope.digitalid_before);
            console.log($scope.digitalid_after);
            /*$scope.generationInProgress = true;

            UserService.compareDigitalIDs($scope.selection)

                    .then(function(data)
                    {
                        $scope.generationInProgress = false;
                        console.log(data.accordionHtml)
                        $scope.htmlString = data.accordionHtml;
                        console.log(data.accordions.length);
                        for(var i = 0; i < data.accordions.length; i++)
                        {
                            if(data.accordions[i].content2 != undefined)
                            {
                                diffUsingJS(0, data.accordions[i].divName, data.accordions[i].content1, data.accordions[i].content2);
                            }
                            else
                            {
                                addIncompatible(data.accordions[i].divName, data.accordions[i].content1);
                            }

                        }
                    }, function(error) {
                        console.log(error);
                    });*/

        }

        $scope.stopCallback = function(event, ui, digitalid)
        {
            console.log("Stop Callback called: ");
            console.log(digitalid.url);
                        var omg = []
                        omg.push(digitalid.url);
                        DigitalIdService.openDigitalID(omg)

                            .then(function(data)
                            {
                                console.log(data.accordionHtml);
                                for(var i = 0; i < data.accordions.length; i++)
                                {
                                     console.log(data.accordions[i]);
                                     $scope.digitalid_before_array.push(new ContentDto(data.accordions[i].displayString, data.accordions[i].content));
                                }
                            }, function(error) {
                                console.log(error);
                            });

            console.log($scope.digitalid_before_array);
        }

        $scope.startCallback = function(event, ui, item)
        {
            console.log("Start Callback called: ");
            console.log(item);
        }

        $scope.displayDigitalId = function(digitalid)
        {
            console.log(digitalid.url);
            var omg = []
            omg.push(digitalid.url);
            UserService.openDigitalID(omg)

                .then(function(data)
                {
                    $scope.htmlString = data.accordionHtml;
                    console.log(data.accordionHtml);
                    /*for(var i = 0; i < data.accordions.length; i++)
                    {



                         if(data.accordions[i].content1 !== '')
                         {
                             console.log("data: " + data.accordions[i].content1);
                             addIncompatible(data.accordions[i].divName, data.accordions[i].content1);
                         }



                    }*/
                }, function(error) {
                    console.log(error);
                });
        }

        function diffUsingJS(viewType, sectionName, s1, s2)
        {
            var base = difflib.stringAsLines(s1);
            var newtxt = difflib.stringAsLines(s2);

            var no_white_spaces1 = [];
            var no_white_spaces2 = [];

            for(var i = 0; i < base.length; i++)
            {
                no_white_spaces1.push(base[i].replace(/\s/g, ""));
            }

            for(var i = 0; i < newtxt.length; i++)
            {
                no_white_spaces2.push(newtxt[i].replace(/\s/g, ""));
            }

            var sm = new difflib.SequenceMatcher(no_white_spaces1, no_white_spaces2);
            var opcodes = sm.get_opcodes();
            var contextSize = 0;
            $scope[sectionName] = diffview.buildView({
                      baseTextLines: base,
                      newTextLines: newtxt,
                      opcodes: opcodes,
                      baseTextName: "Before",
                      newTextName: "After",
                      contextSize: 0,
                      viewType: viewType
              });

        }

        function addIncompatible(divName, note)
        {
            $scope[divName] = note;
        }
    }


})();