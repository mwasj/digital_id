(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('CompareCtrl', CompareCtrl);

    CompareCtrl.$inject = ['$scope', '$log'];


    function CompareCtrl($scope, $log) {
        console.log("CompareCtrl constructed.");

        //Check if file APIs are supported.
        if (window.File && window.FileReader && window.FileList && window.Blob) {
            console.log("File APIs supported!");
        } else {
            alert('The File APIs are not fully supported in this browser.');
        }

        $scope.file_changed = function(element, $scope) {

             $scope.$apply(function(scope) {
                 var photofile = element.files[0];
                 var reader = new FileReader();
                 reader.onload = function(e) {

                 };
                 reader.readAsDataURL(photofile);
             });
        };
    }
})();