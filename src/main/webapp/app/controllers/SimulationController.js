/**
 * Pay Controller
 */
nusseoAppControllers.controller('SimulationCtrl', [ '$scope', '$http',
	'$rootScope', '$interval',
	function($scope, $http, $rootScope, $interval) {
	    console.log("Simulation !");
	    $scope.lastResults = [];
	    $scope.sample = "<SupportingResource><ResourceContentType>01</ResourceContentType><ContentAudience>00</ContentAudience><ResourceMode>03</ResourceMode><ResourceVersion><ResourceForm>02</ResourceForm><ResourceLink>http://images.immateriel.fr/covers/9782890747623.png</ResourceLink><ContentDate><ContentDateRole>17</ContentDateRole><DateFormat>00</DateFormat><Date>20121020</Date></ContentDate></ResourceVersion><RecordReference>immateriel.fr-O142061immateriel.fr-O141837immateriel.fr-O141579</RecordReference></SupportingResource>";
	    $scope.sendSample = function() {
	    	 $http.post('rest/api/addNewSample',
	 			    'sample=' +  $scope.sample

	 		    ).then(function(response) {
	 		    	console.log("Response ok", response);
	 		    	$scope.checkLatestResults();
	 		    });
	    }
	    
	    
	    $scope.checkLatestResults = function() {
	    	$http.get('api/last5picutres').then(function(response) {
	 		    	$scope.lastResults = response.data;
	 		});
	    }
	    
	    
	    $interval(
	    		function () {
	    			$scope.checkLatestResults ()
	    		},
	    		2000
	    );	    		

	    
}]);