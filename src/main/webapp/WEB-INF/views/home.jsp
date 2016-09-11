<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Hangman Game</title>

		<spring:url value="/resources/vendors/bootstrap/css/bootstrap.min.css" var="bootstrapCSS" />
		<spring:url value="/resources/vendors/bootstrap/css/bootstrap-theme.min.css" var="bootstrapThemeCSS" />
		<spring:url value="/resources/vendors/jquery/jquery-ui/jquery-ui.min.css" var="jqueryUICSS" />
		<spring:url value="/resources/core/css/main.css" var="mainCSS" />
		<link href="${bootstrapCSS}" rel="stylesheet" />
		<link href="${bootstrapThemeCSS}" rel="stylesheet" />
		<link href="${jqueryUICSS}" rel="stylesheet" />
		<link href="${mainCSS}" rel="stylesheet" />
	</head>
	<body>
		<div class="container disableSection" id="loginWindow">
			<div class="row">
				<div class="form-group">
					<h1 style="">Welcome to the Hangman Game</h1>
					<p>Enter name to and click begin</p>
					
					<p>
						<label for="inputUserName">Name:</label>
						<input class="form-control input-lg" id="inputUserName" type="text" />
					</p>
					<p><a class="btn btn-default" href="javascript:void(0);" role="button" id="beginButton" >Begin</a></p>
				</div>
			</div>
		</div>
		<div class="container disableSection" id="mainGameWindow">
			<div class="row" id="gameButtons">
				<div class="btn-group btn-group-justified" role="group" aria-label="...">
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-default" id="sNGBtn" title="Start a new game with the same username">Start New Game</button>
					</div>
				</div>
			</div>
			<div class="row">
				<span style="text-align: center" id="userName"></span>
			</div>
			<div class="row" id="hangmanInGameDetails">
				<span style="float:left;">Remaining Attempts: <span class="label label-default" id="hangmanRemainingAttempts">7</span></span>
			</div>
			<div class="row" id="hangmanGame">
				<div class="col-md-8" id="hangmanImage"></div>
				<div class="col-md-4" id="hangmanKeyboard"></div>
			</div>
			<div class="row" id="hangmanWords"></div>
		</div>
		
		
		
		<spring:url value="/resources/core/js/app.js" var="appJs" />
		<spring:url value="/resources/vendors/bootstrap/js/bootstrap.min.js" var="bootstrapJs" />
		<spring:url value="/resources/vendors/jquery/jquery-3.1.0.min.js" var="jquery" />
		<spring:url value="/resources/vendors/jquery/jquery-ui/jquery-ui.min.js" var="jqueryUI" />
		<script src="${jquery}"></script>
		<script src="${jqueryUI}"></script>
		<script src="${appJs}"></script>
		<script src="${bootstrapJs}"></script>
	</body>
</html>
