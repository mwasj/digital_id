(function() {

     angular
        .module('myApp')
        .directive('dynamic', function ($compile) {
             console.log("directive called");
              return {
                restrict: 'A',
                replace: true,
                link: function (scope, ele, attrs)
                {
                    scope.$watch(attrs.dynamic, function(html)
                    {
                        ele.html(html);
                        $compile(ele.contents())(scope);
                    });
                }
              };
              }
         );

})();