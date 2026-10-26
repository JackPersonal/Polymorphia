// $(window).on("resize", function () {
//     drawGame(document.getElementById("gameDropDown").value);
// });

const HTTP_CODE = {
    CREATED: 201,
    OK: 200,
    NO_CONTENT: 204,
    BAD_REQUEST: 400,
    NOT_FOUND: 404
};

function loadDefaultData() {
    populateGameDropDown();
    populateCommandChoices([]);
    document.getElementById("commandChoice").addEventListener("dblclick", submitCommandAndPlayTurn);
}

function getGameUrlPrefix() {
    const gameDropDown = document.getElementById("gameDropDown");
    if (!gameDropDown.value) {
        if (gameDropDown.options.length === 0) {
            console.log("No games found");
            return;
        }
        gameDropDown.value = gameDropDown.options[gameDropDown.options.length - 1].value;
    }
    return "api/game/" + gameDropDown.value;
}

function getGame() {
    const url = getGameUrlPrefix();
    if (!url) {
        console.log("No game selected");
        return;
    }
    const xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === HTTP_CODE.OK) {
            const game = JSON.parse(xhr.responseText);
            drawGame(game);
            setStatusMessage(game.statusMessage);
        } else {
            setStatusMessage(" Error getting game: " + xhr.responseText + " " + url);
            populateGameDropDown();
        }
    };
    xhr.send();
}

function saveGame() {
    const url = `${getGameUrlPrefix()}/save`;
    if (!url) {
        console.log("No game selected");
        return;
    }
    const xhr = new XMLHttpRequest();
    xhr.open("PUT", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === HTTP_CODE.OK) {
            const message = xhr.responseText;
            setStatusMessage(message);
        } else {
            setStatusMessage(" Error saving game: " + xhr.responseText + " " + url);
            populateGameDropDown();
        }
    };
    xhr.send();
}

function setStatusMessage(message) {
    const statusElement = document.getElementById("statusMessage");
    statusElement.textContent = message;
}

function getPlayersName() {
    return document.getElementById("playerName").value;
}

function clearStatusMessage() {
    setStatusMessage("");
}

function setGameDropDownValue(gameId) {
    const gameDropDown = document.getElementById("gameDropDown");
    gameDropDown.value = gameId;
}

function populateGameDropDown() {
    const url = "api/games";
    const xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === HTTP_CODE.OK) {
            const gameIds = JSON.parse(xhr.responseText);
            const gameDropDown = document.getElementById("gameDropDown");
            gameDropDown.innerHTML = "";
            gameIds.forEach(gameId => {
                const option = document.createElement("option");
                option.value = gameId;
                option.text = gameId;
                gameDropDown.add(option);
            });
            if (gameIds.length > 0) {
                gameDropDown.value = gameIds[gameIds.length - 1];
            }
            getGame();
        }
    };
    xhr.send();
}

function clearCommandChoices() {
    const commandChoice = document.getElementById("commandChoice");
    commandChoice.innerHTML = "";
}

function submitCommandAndPlayTurn() {
    submitCommand();
    playTurn();
}

function submitCommand() {
    const commandChoice = document.getElementById("commandChoice");
    const command = commandChoice.options[commandChoice.selectedIndex].text;
    let url = `${getGameUrlPrefix()}/${getPlayersName()}/submitCommand/${encodeURIComponent(command)}`;

    const xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === HTTP_CODE.CREATED) {
            console.log("Command submitted successfully");
            clearCommandChoices();
        } else {
            setStatusMessage(" Error submitting command: " + xhr.responseText + " " + url);
        }
    };
    xhr.send();
}

function getCommandChoices() {
    const url = `${getGameUrlPrefix()}/${getPlayersName()}/availableCommands`;
    const xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === HTTP_CODE.OK) {
            console.log("Command choices successfully retrieved");
            const commandChoices = JSON.parse(xhr.responseText);
            populateCommandChoices(commandChoices);
        } else {
            setStatusMessage(" Error getting command choices: " + xhr.responseText + " " + url);
        }
    };
    xhr.send();
}

function playTurn() {
    let url = getGameUrlPrefix() + "/playTurn"
    const xhr = new XMLHttpRequest();
    xhr.open("PUT", url, true);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === HTTP_CODE.OK) {
            console.log("Turn played successfully");
            getGame();
            drawGame(JSON.parse(xhr.responseText));
            getCommandChoices();
        } else {
            setStatusMessage("Error playing turn with at URL " + url);
        }
    };
    xhr.send();
}

function populateCommandChoices(choices) {
    const commandChoice = document.getElementById("commandChoice");
    commandChoice.innerHTML = "";
    choices.forEach(choice => {
        const option = document.createElement("option");
        option.value = choice;
        option.text = choice;
        commandChoice.add(option);
    });
    commandChoice.disable = choices.size == 0;
    commandChoice.value = null;
}

function getGameParameters() {
    const playerName = document.getElementById("playerName").value;
    const numRooms = document.getElementById("numRooms").value;
    const numAdventurers = document.getElementById("numAdventurers").value;
    const numKnights = document.getElementById("numKnights").value;
    const numCowards = document.getElementById("numCowards").value;
    const numGluttons = document.getElementById("numGluttons").value;
    const numCreatures = document.getElementById("numCreatures").value;
    const numDemons = document.getElementById("numDemons").value;
    const numFood = document.getElementById("numFood").value;
    const numArmor = document.getElementById("numArmor").value;
    return {
        playerName: playerName,
        numRooms: numRooms,
        numAdventurers: numAdventurers,
        numKnights: numKnights,
        numCowards: numCowards,
        numGluttons: numGluttons,
        numCreatures: numCreatures,
        numDemons: numDemons,
        numFood: numFood,
        numArmor: numArmor
    };
}

function createGame() {
    const url = "api/game/create";
    const xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 201) {
            const game = JSON.parse(xhr.responseText);
            populateGameDropDown();
            document.getElementById("newGameStatus").textContent = "Game " + game.name + " created";
            setGameDropDownValue(game.name);
            getGame();
        }
    };
    const gameParameters = JSON.stringify(getGameParameters());
    xhr.send(gameParameters);
}

var turnIntervalId = null;

function updateAutomatically() {
    const playAutomatically = document.getElementById("playAutomatically").checked;
    const secondBetweenTurns = document.getElementById("secondBetweenTurns").value;
    if (turnIntervalId !== null) {
        window.clearInterval(turnIntervalId);
    }
    if (playAutomatically) {
        turnIntervalId = window.setInterval(playTurn, secondBetweenTurns * 1000);
    } else {
        if (turnIntervalId !== null) {
            window.clearInterval(turnIntervalId);
            turnIntervalId = null;
        }
    }
}

