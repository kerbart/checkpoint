/**
 * Pay Controller
 */
nusseoAppControllers.controller('SplitterCtrl', [
		'$scope',
		'$http',
		'$rootScope',
		'$interval',
		function($scope, $http, $rootScope, $interval) {
			console.log("Splitter !");

			$scope.current = null;
			$scope.olds = null;
			$scope.fileToSplit = null;
			$scope.outputFolder = null;
			
			$scope.updateCurrentWorker = function() {
				$http.get('api/splitter/split/worker/current').then(
						function(response) {
							$scope.current = response.data;
						});
			};
			

			$scope.updateOldWorkers = function() {
				$http.get('api/splitter/split/worker/old').then(
						function(response) {
							$scope.olds = response.data;
						});
			};

			
			$scope.splitFile  = function() {
				if (this.current) {
					alert("Please wait : a splitter worker is aldreay in progress...");
					return ;
				}
				
				if (!$scope.fileToSplit || !$scope.outputFolder ) {
					alert("Please choose a file and type a folder name...");
					return ;
				}
				
				
				$http.get('api/splitter/split/' + $scope.fileToSplit + '/' + $scope.outputFolder).then(
						function(response) {
							$scope.olds = response.data;
						});
				
				
				
				
			};
			
			
			$scope.updateCurrentWorker();
			$scope.updateOldWorkers();
			
			
			 $scope.listFilesToSplit = function() {
				   $http.get('api/splitter/list/').then(function(response) {
						$scope.listFilesToSplit = response.data;
				   });   
			   } 
			   $scope.listFilesToSplit();
			
			// $scope.refreshAll = function() {
			// $scope.exportedProductsQuery();
			// $scope.updatedProductsQuery();
			// $scope.updateFileInfo();
			//		  
			// $scope.queueSizeQuery();
			// }
			//	   
			//	   
			$interval(
			function() {
				$scope.updateCurrentWorker();
				$scope.updateOldWorkers();
			 },
			 2000);
		} ]);