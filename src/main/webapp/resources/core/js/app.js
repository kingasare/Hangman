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

	$beginButton.click(beginGame);
	init();

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
					debugger;
					//gameSettings.hangMan
					if(inGameSession != null) {
						$mainGameWindow.removeClass("disableSection");
						$loginWindow.addClass("disableSection");
						updateFullInGameSession(inGameSession);
					}else {
						$loginWindow.removeClass("disableSection");
					}
				})
				.fail(function( jqXHR, textStatus ) {
					debugger;
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
		generateKeyboard(inGameSession.sucessfulLetters);
	}

	function updateFailInGameSession(inGameSession){
		//Update just the image
		updateRemainingAttempt(inGameSession.numberOfRemainingAttempts);
		updateHangmanImage(inGameSession.hangmanImgPath);
		if(inGameSession.currentGameState == "LOST_STATE"){
			$hangmanLetters.unbind("click");
			// display error message
		}
	}

	function updateSuccessInGameSession(inGameSession){
		updateHangManWord(inGameSession.dummyWord);
		if(inGameSession.currentGameState == "WIN_STATE"){
			$hangmanLetters.unbind("click");
			alert("Well Done");
		}
	}

	function updateRemainingAttempt(attempts){
		$hangmanRemainingAttempts.text(attempts);
	}
	
	function updateUser(user){
		$userName.html("Welcome <b>" + user.toUpperCase() + "</b>");
	}

	function updateHangManWord(word){
		var htmlWord = "";
		for(i = 0; i < word.length; i++){
			if(word[i] != "_"){
				htmlWord += "<span class='word'>" + word[i] + "</span>";
			}else {
				htmlWord += "<span class='word'>&#160;</span>";
			}
		}
		$hangmanWords.empty().append(htmlWord);
	}

	function generateKeyboard(restrictedLetters){
		var alphabet = "abcdefghijklmnopqrstuvwxyz";
		var htmlWord = "";

		for(x = 0; x < alphabet.length; x++){
			var letter = alphabet.charAt(x);
			if(!((restrictedLetters != null) 
					&& (restrictedLetters.length > 0) 
					&& restrictedLetters.includes(letter))){
				htmlWord += "<span>" + letter + "</span>"
			}
		}

		$hangmanKeyboard.empty().append(htmlWord);
		$hangmanLetters = $("#hangmanKeyboard > span");
		$hangmanLetters.click(submitAttempt);
	}
	function updateHangmanImage(url){
		debugger;
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