/**
 * Pay Controller
 */
nusseoAppControllers.controller('JournalCtrl', [ '$scope', '$http',
		'$rootScope', '$interval',
		function($scope, $http, $rootScope, $interval) {
			console.log("Splitter !");
			
			$scope.timeNow = Date.now();

			$scope.journal = null;
			$scope.updateJournal = function() {
				$http.get('api/journal/import').then(function(response) {
					$scope.journal = response.data;
					
				});
			};
			$scope.updateServerTime = function () {
				$http.get('api/server/time').then(function(response) {
					$scope.timeNow = response.data;
				});
			}
			
			$scope.updateJournal();
			$scope.updateServerTime();
			$interval(function() {
				$scope.updateServerTime();
				$scope.updateJournal();
			}, 2000);
		} ]);