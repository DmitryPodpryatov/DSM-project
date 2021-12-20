window.onload = function () {
    showPlayerId();
    listGames();
}

function hostGame() {
    const playerId = localStorage.getItem("playerId");

    function callback(response) {
        localStorage.setItem("gameId", response)

        // redirect(`/api/game/${response}?playerId=${playerId}`);
        redirect(`/games/${response}?playerId=${playerId}`);
    }

    const request = new XMLHttpRequest();
    makeRequest(request, callback);

    request.open("POST", `/api/game?playerId=${playerId}`, true);
    request.send(null);
}

function showPlayerId() {
    const p = document.getElementById("playerId")
    const playerId = localStorage.getItem("playerId");

    p.textContent = `Player ID: ${playerId}`;
}

function listGames() {
    function callback(response) {
        const table = document.getElementById("games-table");
        const json = JSON.parse(response);

        const games = Object.keys(json);
        const hosts = Object.values(json);

        for (let i = 0; i < games.length; ++i) {
            let tableRow = document.createElement("tbody");

            let tableHostData = document.createElement("td");
            tableHostData.id = `td-host-${i}`;

            let tableGameData = document.createElement("td");
            tableGameData.id = `td-game-${i}`;

            let tableButtonData = document.createElement("td");
            tableButtonData.id = `td-btn-${i}`;

            tableHostData.innerHTML = hosts[i];
            tableGameData.innerHTML = games[i];

            tableButtonData = document.createElement("button");
            tableButtonData.textContent = "Join";
            tableButtonData.onclick = function () {
                const gameId = document.getElementById(`td-game-${i}`).textContent;
                const host = document.getElementById(`td-host-${i}`).textContent
                const playerId = localStorage.getItem("playerId");

                joinGame(gameId, host, playerId);
            };

            tableRow.appendChild(tableHostData);
            tableRow.appendChild(tableGameData);
            tableRow.appendChild(tableButtonData);
            table.appendChild(tableRow);
        }
    }

    const request = new XMLHttpRequest();
    makeRequest(request, callback);

    request.open("GET", "http://0.0.0.0:8082/api/games", true);
    request.send(null);
}

function joinGame(gameId, host, playerId) {
    function callback(response, hostIP) {
        localStorage.setItem("gameId", response);

        setupGame(gameId, hostIP);
        redirect(`/games/${response}?playerId=${playerId}`);
    }

    const hostIP = getPlayerIP(host);
    const request = new XMLHttpRequest();

    console.log(`HTTP request to POST /join by ${hostIP} ${gameId}:${playerId}`)

    request.open("POST", `http://${hostIP}/api/game/${gameId}/join?playerId=${playerId}`, false);
    request.send(null);

    callback(request.response, hostIP);
}

function setupGame(gameId, hostIP) {
    const body = getGame(gameId, hostIP);
    const request = new XMLHttpRequest();

    console.log(`HTTP request to POST /setup by host=${hostIP}, gameId=${gameId}`)
    console.log(body)

    request.open("POST", "/api/game/setup", false);
    request.send(body);
}

function getGame(gameId, hostIP) {
    const request = new XMLHttpRequest();

    console.log(`HTTP request to GET /game by hostIP=${hostIP}, gameId=${gameId}`)

    request.open("GET", `http://${hostIP}/api/game/${gameId}`, false);
    request.send(null);

    return JSON.parse(request.response);
}

function getPlayerIP(playerId) {
    function callback(response) {
        const json = JSON.parse(response);

        return json[playerId]
    }

    const request = new XMLHttpRequest();

    request.open("GET", "http://0.0.0.0:8082/api/players", false);
    request.send(null);

    return callback(request.response)
}
