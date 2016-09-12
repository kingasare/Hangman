/**
 * 
 */

$(function(){
	var $userNameText = $("#inputUserName");
	var $beginButton = $("#beginButton");
	var $loginWindow = $("#loginWindow");
	var $mainGameWindow = $("#mainGameWindow");
	var $hangmanImage = $("#hangmanImage");
	var $startNewGameBtn = $("#sNGBtn");
	var $userName = $("#userName");
	var $hangmanKeyboard = $("#hangmanKeyboard");
	var $hangmanLetters = $("#hangmanKeyboard > span");
	var $hangmanRemainingAttempts = $("#hangmanRemainingAttempts");
	var $hangmanWords = $("#hangmanWords");
	var $hangmanUsedAttempts = $("#hangmanUsedAttempts");

	$beginButton.click(beginGame);
	$startNewGameBtn.click(startNewGame);
	init();

	//TODO: Since we are not passing any information to server,
	// Could handle this operation when serving the jsp page in HomeController.
	function init(){
		$.ajax({
			url: "game/retrieveExistingGame",
			cache: false,
			method: "POST",
			dataType: "json",
		})
		.done(function(inGameSession) {
			if(inGameSession != null) {
				$mainGameWindow.removeClass("disableSection");
				updateFullInGameSession(inGameSession);
			}else{
				$loginWindow.removeClass("disableSection");
			}
		})
		.fail(function( jqXHR, textStatus ) {
			if(jqXHR.status == 403){
				$loginWindow.removeClass("disableSection");
			}else {
				alert( "Request failed: " + textStatus );
			}
		});
	}

	function startNewGame(){
		$mainGameWindow.addClass("disableSection");
		$loginWindow.removeClass("disableSection");
	}

	function beginGame(){
		var _username = $userNameText.val().trim();
		if(_username || _username.length != 0 ){
			$.ajax({
				url: "game/beginGame",
				cache: false,
				method: "POST",
				dataType: "json",
				data: {username : _username},
				})
				.done(function(inGameSession) {
					if(inGameSession != null) {
						$mainGameWindow.removeClass("disableSection");
						$loginWindow.addClass("disableSection");
						updateFullInGameSession(inGameSession);
					}else {
						$loginWindow.removeClass("disableSection");
					}
				})
				.fail(function( jqXHR, textStatus ) {
					alert( "Request failed: " + textStatus );
				});
		}else{
			alert("Enter a UserName");
		}
	}

	function updateFullInGameSession(inGameSession){
		//Update everything
		updateRemainingAttempt(inGameSession.numberOfRemainingAttempts);
		updateHangmanImage(inGameSession.hangmanImgPath);
		updateHangManWord(inGameSession.dummyWord);
		updateUser(inGameSession.user.userName);
		generateKeyboard(inGameSession.successfulLetters);
		displayUnsuccessfulLetters(inGameSession.unSuccessfulLetters);
		if(!isPlayable(inGameSession.currentGameState)){
			$hangmanLetters.unbind("click");
		}
	}

	function updateFailInGameSession(inGameSession){
		updateRemainingAttempt(inGameSession.numberOfRemainingAttempts);
		displayUnsuccessfulLetters(inGameSession.unSuccessfulLetters);
		updateHangmanImage(inGameSession.hangmanImgPath);
		if(!isPlayable(inGameSession.currentGameState)){
			$hangmanLetters.unbind("click");
			// display error message
			alert("Sorry you lost the game");
		}
	}

	function isPlayable(currentGameState){
		return currentGameState === "PLAYABLE_STATE";
	}

	function updateSuccessInGameSession(inGameSession){
		updateHangManWord(inGameSession.dummyWord);
		if(inGameSession.currentGameState == "WIN_STATE"){
			$hangmanLetters.unbind("click");
			alert("Congratulation you won the game");
		}
	}

	function updateRemainingAttempt(attempts){
		$hangmanRemainingAttempts.text(attempts);
	}
	
	function updateUser(user){
		$userName.html("Welcome <b>" + user.toUpperCase() + "</b>");
	}

	function updateHangManWord(word){
		if(word != null && word.length > 0){
			var htmlText = "";
			for(i = 0; i < word.length; i++){
				if(word[i] != "_"){
					htmlText += "<span class='word'>" + word[i] + "</span>";
				}else {
					htmlText += "<span class='word'>&#160;</span>";
				}
			}
			$hangmanWords.empty().append(htmlText);
		}
	}

	function generateKeyboard(restrictedLetters){
		var alphabet = "abcdefghijklmnopqrstuvwxyz";
		var htmlText = "";

		for(x = 0; x < alphabet.length; x++){
			var letter = alphabet.charAt(x);
			if(!((restrictedLetters != null) 
					&& (restrictedLetters.length > 0) 
					&& restrictedLetters.includes(letter))){
				htmlText += "<span>" + letter + "</span>"
			}
		}

		$hangmanKeyboard.empty().append(htmlText);
		$hangmanLetters = $("#hangmanKeyboard > span");
		$hangmanLetters.click(submitAttempt);
	}

	function updateHangmanImage(url){
		image = new Image();
		image.src = url + "?" + Math.random();
		image.onload = function () {
			$hangmanImage.empty().append(image);
		};

		image.onerror = function () {
			$hangmanImage.empty().html('Error Occured <br /> Unable to retrieve Hangman Image');
		}

		$hangmanImage.empty().html('Loading...');
	}

	function displayUnsuccessfulLetters(unsuccessfulLetters){
		if(unsuccessfulLetters != null && unsuccessfulLetters.length > 0 ){
			var htmlText = "";
			for(y = 0; y < unsuccessfulLetters.length; y++){
				htmlText += "<span>" + unsuccessfulLetters[y] + "</span>";
			}

			$hangmanUsedAttempts.empty().append(htmlText);
		}else{
			$hangmanUsedAttempts.empty();
		}
	}

	function submitAttempt(){
		var instance = this;
		var _letter = instance.innerText;

		$.ajax({
			url: "game/submitAttempt",
			cache: false,
			method: "POST",
			dataType: "json",
			data: {letter : _letter},
		})
		.done(function(inGameSession) {
			if(inGameSession.successfulAttempt){
				$(instance).hide("slow");
				updateSuccessInGameSession(inGameSession);
			} else {
				updateFailInGameSession(inGameSession);
			}
		})
		.fail(function( jqXHR, textStatus ) {
			alert( "Request failed: " + textStatus );
		});
	}
});