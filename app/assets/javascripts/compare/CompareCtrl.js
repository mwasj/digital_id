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
        function ContentDto (id, displayString, content)
        {
            this.id = id;
            this.displayString = displayString;
            this.content = content;
        }

        function ContainerDTO (fileName, id)
        {
                this.fileName = fileName;
                this.id = id;
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
        $scope.clickedDigitalId = undefined;
        $scope.beforeSelection = [];
        $scope.afterSelection = []';

        DigitalIdService.listDigitalIDs()
            .then(function(data) {
                console.log(data);
                $scope.digitalIds = data;
            }, function(error) {
                console.log(error);
            });

        $scope.toggleSelection = function toggleSelection(digitalId, e, before)
        {
            if (e) {
                e.originalEvent.cancelBubble=true;
            }

            var idx = -1;

            if(before)
            {
                $scope.beforeSelection.splice(idx, 1);


                for(var i = 0; i < $scope.beforeSelection.length; i++)
                {
                    if($scope.beforeSelection[i].)
                }
            }
            else
            {
                $scope.afterSelection.splice(idx, 1);
            }


            var idx = before ? $scope.beforeSelection.indexOf(digitalId) : $scope.afterSelection.indexOf(digitalId);

            // is currently selected
            if (idx > -1)
            {
                if(before)
                {
                    $scope.beforeSelection.splice(idx, 1);
                }
                else
                {
                    $scope.afterSelection.splice(idx, 1);
                }

            }

            // is newly selected
            else
            {
                if(before)
                {
                    $scope.beforeSelection.push();
                }
                else
                {
                    $scope.afterSelection.splice();
                }
            }

            console.log($scope.beforeSelection);
            console.log($scope.afterSelection);
        };

        $scope.loadDigitalIdInformation = function(digitalId)
        {
            $scope.clickedDigitalId = digitalId;
        }

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
            console.log(ui);

            console.log(digitalid.url);


            console.log($scope.digitalid_before_array);
        }

        $scope.dropBeforeCallback = function(event, ui, digitalid)
        {
            $scope.digitalid_before_array.length = 0;
            $scope.digitalid_before_array = downloadPreview(digitalid);
        }

        $scope.dropAfterCallback = function(event, ui, digitalid)
        {
            $scope.digitalid_after_array.length = 0
            $scope.digitalid_after_array = downloadPreview(digitalid);
        }

        function downloadPreview(digitalid)
        {
            var retrievedData = [];
            var omg = []
            omg.push(digitalid.url);
            DigitalIdService.openDigitalID(omg)

                .then(function(data)
                {
                    for(var i = 0; i < data.accordions.length; i++)
                    {
                         retrievedData.push(new ContentDto(data.accordions[i].id, data.accordions[i].displayString, data.accordions[i].content));
                    }

                }, function(error) {
                    console.log(error);
                });

            return retrievedData;
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