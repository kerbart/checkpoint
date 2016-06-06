console.log("Configuring Nusseo App...");

var nusseoApp = angular.module('nusseoApp', [ 'ngAnimate', 'ngRoute',
		'ngSanitize', 'nusseoAppControllers' ]);

nusseoApp
		.config([
				'$httpProvider',
				function($httpProvider) {
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
				} ]);

nusseoApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'app/partials/home.html',
		controller : 'HomeCtrl'
	}).otherwise({
		redirectTo : '/home'
	});
} ]);

var nusseoAppControllers = angular.module('nusseoAppControllers', []);

console.log("Configured !");