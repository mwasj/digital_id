(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('CompareCtrl', CompareCtrl);

    CompareCtrl.$inject = ['$scope', '$log', 'UserService', '$sce'];

    function CompareCtrl($scope, $log, UserService, $sce) {
        console.log("CompareCtrl constructed.");

        $scope.digitalIds = [];
        $scope.selection = [];
        $scope.htmlString = '<a ng-click="click(1)" href="#">Click me</a>';
        console.log($scope.htmlString);
        UserService.listDigitalIDs()
            .then(function(data) {
                console.log(data);
                $scope.digitalIds = data;
            }, function(error) {
                console.log(error);
            });

        $scope.toggleSelection = function toggleSelection(digitalId) {
            var idx = $scope.selection.indexOf(digitalId);

            // is currently selected
            if (idx > -1) {
              $scope.selection.splice(idx, 1);
            }

            // is newly selected
            else {
              $scope.selection.push(digitalId);
            }

            console.log($scope.selection);
        };

        $scope.goCompare = function(){
            UserService.compareDigitalIDs($scope.selection)
                    .then(function(data) {
                        console.log(data);
                        $scope.htmlString = data;
                    }, function(error) {
                        console.log(error);
                    });
        }

        //diffUsingJS(0, "test", "string1", "string2");
    }

    function diffUsingJS(viewType, sectionName, s1, s2)
    {
        "use strict";
        var byId = function (id) { return document.getElementById(id); },
        base = difflib.stringAsLines(s1),
        newtxt = difflib.stringAsLines(s2),
        sm = new difflib.SequenceMatcher(base, newtxt),
        opcodes = sm.get_opcodes(),
        diffoutputdiv = byId(sectionName),
        contextSize = 0
        contextSize = contextSize || null;

        diffoutputdiv.appendChild(diffview.buildView({
                baseTextLines: base,
                newTextLines: newtxt,
                opcodes: opcodes,
                baseTextName: "Before",
                newTextName: "After",
                contextSize: 0,
                viewType: viewType
        }));
    }
})();