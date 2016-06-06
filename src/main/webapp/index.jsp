<!DOCTYPE html>
<%@page import="java.util.Date"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="fr" ng-app="nusseoApp">


<meta charset="UTF-8">
<title>Nusseo CDN</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="apple-touch-icon" href="touch/apple-touch-icon.png">
<link rel="apple-touch-startup-image" href="touch/apple-touch-startup-image-320x460.png">

<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap-horizon.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/fonts.css"> 

<link rel='stylesheet' type='text/css'
	href='https://fonts.googleapis.com/css?family=Montserrat'>
<link
	href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,300,700,900,900italic,700italic,600,400italic,600italic,300italic,200italic,200'
	rel='stylesheet' type='text/css'>

<script src="node_modules/angular/angular.min.js"></script>
<script
	src="node_modules/angular-animate/angular-animate.min.js"></script> 
<script
	src="node_modules/angular-sanitize/angular-sanitize.min.js"></script>
<script
	src="node_modules/angular-route/angular-route.min.js"></script>
<script src="node_modules/lodash/lodash.custom.min.js"></script>

<script src="app/app.js?<%=new Date()%>"></script>

<script src="app/controllers/HomeController.js?<%=new Date()%>"></script>
<script src="app/controllers/ExportJournalController.js?<%=new Date()%>"></script>
<script src="app/controllers/JournalController.js?<%=new Date()%>"></script>

<script src="node_modules/jquery/dist/jquery.min.js"></script>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/highcharts-more.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<script src="app/vkbeautify.js" ></script>


<link rel="icon" type="image/png" href="favicon.png" />

<body>
	<div id="main_wrapper" ng-view >
		
	</div>
	<script src="node_modules/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>