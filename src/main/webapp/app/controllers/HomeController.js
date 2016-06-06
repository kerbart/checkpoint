/**
 * Pay Controller
 */
nusseoAppControllers.controller('HomeCtrl', [ '$scope', '$http',
	'$rootScope', '$interval',
	function($scope, $http, $rootScope, $interval) {
	    console.log("Home !");	    
	   $scope.files = [];
	   
	   $scope.updatedProducts = 0;
	   $scope.exportedProducts = 0;
	   $scope.imagesFound = 0;
	   $scope.imagesNotFound = 0;
	   $scope.queueSize = 0;
	   $scope.exportFileResult = {};
	   $scope.importHachetteResponse = "";
	   $scope.importHachetteProcessing = false;
	   $scope.listFilesToSplit = [];
	   $scope.fileToSplit = "";
	   
	   $scope.updateFileInfo = function() {
		   $http.get('api/status/full').then(function(response) {
				 $scope.files = []; 
					for (var info in response.data) {
							$scope.files.push(response.data[info]);
					}; 			 
					console.log("Les données sont ", $scope.files);
		   });
	   }
	   
	   
	   $scope.splitFile = function() {
		   $http.post('api/splitter/split/', $scope.fileToSplit ).then(function(response) {
				 $scope.files = []; 
					for (var info in response.data) {
							$scope.files.push(response.data[info]);
					}; 			 
					console.log("Les données sont ", $scope.files);
		   });
	   }
	   
	   
	   $scope.listFilesToSplit = function() {
		   $http.get('api/splitter/list/').then(function(response) {
				$scope.listFilesToSplit = response.data;
		   });   
	   }
	   $scope.listFilesToSplit();
	   
	   $scope.updatedProductsQuery = function() {
		   $http.get('api/status/products/updated/count').then(function(response) {
						$scope.updatedProducts = response.data;
		   });
	   }
	   
	   $scope.exportedProductsQuery = function() {
		   $http.get('api/status/products/exported/count').then(function(response) {
						$scope.exportedProducts = response.data;	
		   });
	   }
	   
	   $scope.getImagesFound = function() {
		   $http.get('api/images/found').then(function(response) {
						$scope.imagesFound = response.data;	
		   });
	   }
	   
	   $scope.getImagesNotFound = function() {
		   $http.get('api/images/notfound').then(function(response) {
						$scope.imagesNotFound = response.data;	
		   });
	   }
	   
	   $scope.queueSizeQuery = function() {
		   $http.get('api/status/products/inqueue').then(function(response) {
						$scope.queueSize = response.data;	
		   });
	   }
	   
	   
	   $scope.exportFile = function() {
		   console.log("Lancement de l'export du fichier");
		   $http.post('api/export').then(function(response) {
						$scope.exportFileResult = response.data;
		   });
	   }
	   
	   $scope.importHachette = function() {
		   $scope.importHachetteProcessing = true ;

		   console.log("Lancement de l'import du fichier Hachette");
		   $http.get('api/download/hachette/process').then(function(response) {
				$scope.importHachetteResponse = response.data;
				 $scope.importHachetteProcessing = false;
		   });
	   }
	   
	   $scope.getXmlProduct = function() {
		   $http.get('api/product/' + $scope.selectedEan + '/xml').then(function(response) {
				$scope.xmlProduct = vkbeautify.xml(response.data);
				 
		   });
	   }
	   

	   $scope.getXmlProductSanitized = function() {
		   $http.get('api/product/' + $scope.selectedEan + '/xml/sanitized').then(function(response) {
			   $scope.xmlProductSanitized = vkbeautify.xml(response.data);
		   });
	   }
	   

	   $scope.getProductImage = function() {
		   $http.get('api/product/' + $scope.selectedEan + '/image').then(function(response) {
			   $scope.productImage = response.data;
		   });
	   }
	   
	   $scope.loadProduct = function() {
		   $scope.getXmlProduct();
		   $scope.getXmlProductSanitized();
		   $scope.getProductImage();
	   }
	   
	   

	   
	   
	
		$scope.refreshAll = function() {
			 $scope.exportedProductsQuery();
			 $scope.updatedProductsQuery();
			 $scope.updateFileInfo();
		  
		   $scope.queueSizeQuery(); 
		} 
	   
	   
	   $interval( 
				 function() {
					 $scope.refreshAll();
				 },
		4000);
	} ]);