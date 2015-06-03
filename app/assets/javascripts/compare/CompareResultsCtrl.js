(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('CompareResultsCtrl', CompareResultsCtrl);

    CompareResultsCtrl.$inject = ['$scope', 'resultsArray', '$sce'];

    function CompareResultsCtrl($scope, resultsArray, $sce)
    {
        $scope.resultsArray = resultsArray;
        console.log("CompareResultsCtrl constructed");

        for(var i = 0; i < resultsArray.length; i++)
        {
            diffUsingJS(0, resultsArray[i]);
        }


        function diffUsingJS(viewType, element)
        {
            var before = difflib.stringAsLines(element.beforeContent);
            var after = difflib.stringAsLines(element.afterContent);

            var no_white_spaces1 = [];
            var no_white_spaces2 = [];

            for(var i = 0; i < before.length; i++)
            {
                no_white_spaces1.push(before[i].replace(/\s/g, ""));
            }

            for(var i = 0; i < after.length; i++)
            {
                no_white_spaces2.push(after[i].replace(/\s/g, ""));
            }

            var sm = new difflib.SequenceMatcher(no_white_spaces1, no_white_spaces2);
            var opcodes = sm.get_opcodes();
            var contextSize = 0;

            element.tableContent = diffview.buildView({
                      baseTextLines: before,
                      newTextLines: after,
                      opcodes: opcodes,
                      baseTextName: "Before",
                      newTextName: "After",
                      contextSize: 0,
                      viewType: viewType
              });
        }
    }
})();