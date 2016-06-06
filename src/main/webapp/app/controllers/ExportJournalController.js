/**
 * Pay Controller
 */
nusseoAppControllers.controller('ExportJournalCtrl', [ '$scope', '$http',
		'$rootScope', '$interval',
		function($scope, $http, $rootScope, $interval) {
			console.log("Splitter !");
			$scope.udpateJournal = true;
			$scope.timeNow = Date.now();

			$scope.journal = null;
			$scope.updateJournal = function() {
				$http.get('api/journal/export').then(function(response) {
					if ($scope.udpateJournal) {
						$scope.journal = response.data;
					}
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
			}, 5000);
			
			
			
			$scope.showModal = function(id) {
				$scope.udpateJournal = false;
				$('#modalForFiles' + id).modal();
				$('#modalForFiles' + id).on('hidden.bs.modal', function (e) {
					$scope.udpateJournal = true;
				});
			}
			
			
			
		} ]);