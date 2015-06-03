(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('CompareCtrl', CompareCtrl);

    CompareCtrl.$inject = ['$scope', '$log', 'DigitalIdService', '$parse', '$modal'];

    function CompareCtrl($scope, $log, DigitalIdService, $parse, $modal)
    {
        console.log("CompareCtrl constructed.");

        //Define custom object to hold the content retrieved when a digitalid is opened
        function ContentDto (id, fileName, displayString, content)
        {
            this.id = id;
            this.fileName = fileName;
            this.displayString = displayString;
            this.content = content;
        }

        function ContainerDTO (fileName, id)
        {
                this.fileName = fileName;
                this.id = id;
        }

        function ComparisonDTO (beforeArray, afterArray)
        {
            this.beforeArray = beforeArray;
            this.afterArray = afterArray;
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
        $scope.afterSelection = [];
        $scope.comparePossible = false;

        DigitalIdService.listDigitalIDs()
            .then(function(data) {
                console.log(data);
                $scope.digitalIds = data;
            }, function(error) {
                console.log(error);
            });

        /**/
        $scope.modifySelection = function modifySelection(contentDto, e, before)
        {
            if (e) {
                e.originalEvent.cancelBubble=true;
            }

            if(before)
            {
                $scope.beforeSelection = toggleSelection(contentDto, $scope.beforeSelection);
            }
            else
            {
                $scope.afterSelection = toggleSelection(contentDto, $scope.afterSelection);
            }

            console.log($scope.beforeSelection);
            console.log($scope.afterSelection);

            $scope.comparePossible = ($scope.beforeSelection.length === $scope.afterSelection.length) && ($scope.beforeSelection.length > 0 && $scope.afterSelection.length > 0)
        };

        function toggleSelection(contentDto, currentSelection)
        {
            var idx = -1;

            for(var i = 0; i < currentSelection.length; i++)
            {
                if(currentSelection[i].id === contentDto.id)
                {
                    idx = i;
                }
            }

            if (idx === -1)
            {
                currentSelection.push(new ContainerDTO(contentDto.fileName, contentDto.id));
            }
            else
            {
                currentSelection.splice(idx, 1);
            }

            return currentSelection;
        }

        $scope.loadDigitalIdInformation = function(digitalId)
        {
            $scope.clickedDigitalId = digitalId;
        }

        $scope.goCompare = function()
        {
            var comparisonDTO = new ComparisonDTO($scope.beforeSelection, $scope.afterSelection);

            $scope.generationInProgress = true;

            DigitalIdService.compareDigitalIDs(comparisonDTO)

                    .then(function(data)
                    {
                        $scope.generationInProgress = false;
                        console.log(data);

                         var modalInstance = $modal.open({
                                templateUrl: '/assets/partials/compare_results.html',
                                controller: 'CompareResultsCtrl',
                                backdrop: 'static',
                                windowClass: 'custom-modal',
                                resolve:
                                {
                                    resultsArray: function () {
                                        return data;
                                    }
                                }
                            });
                    }, function(error) {
                        console.log(error);
                    });

        }

        $scope.stopCallback = function(event, ui, digitalid)
        {
            console.log("Stop Callback called: ");
            console.log(ui);

            console.log(digitalid.url);


            console.log($scope.digitalid_before_array);
        }

        /*Populates the digitalid_before_array array with information retrieved from the server.
        This information appears in the preview section.*/
        $scope.dropBeforeCallback = function(event, ui, digitalid)
        {
            $scope.digitalid_before_array.length = 0;
            $scope.digitalid_before_array = downloadPreview(digitalid);
        }

        /*As above, but for the digitalid_after_array*/
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
                         retrievedData.push(new ContentDto(data.accordions[i].id, data.accordions[i].fileName, data.accordions[i].displayString, data.accordions[i].content));
                    }

                }, function(error) {
                    console.log(error);
                });

            return retrievedData;
        }




    }


})();