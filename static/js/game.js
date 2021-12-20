window.onload = function () {
    showGame();
}

function showGame() {
    const gameId = localStorage.getItem("gameId");
    const playerId = localStorage.getItem("playerId");

    const game = getGame(gameId, location.host);
    const board = game["board"]["cells"];
    const players = game["players"];

    showPlayers(players);
    showBoard(board, players);
}

function showPlayers(players) {
    const p = document.getElementById("players")
    const playerId = localStorage.getItem("playerId");

    const colors = new Set([
        "amber-text",
        "red-text",
        "indigo-text",
        "cyan-text",
        "green-text"
    ])
    const positions = []
    const value = playerId

    p.textContent = `Player ID: ${value}`;
}

function showBoard(board) {
    const boardTable = document.getElementById("game-table");

    for (let row = 0; row < board.length; ++row) {
        let boardRow = document.createElement("tbody");

        for (let col = 0; col < board[row].length; ++col) {
            let boardCell = document.createElement("td");
            const cell = board[row][col]

            boardCell.className = cellToIcon(cell)
            boardRow.appendChild(boardCell);
        }

        boardTable.appendChild(boardRow);
    }
}

function cellToIcon(cell) {
    const arrowPrefix = "bi bi-arrow-"
    const start = "bi-arrow-bar-right"
    const finish = "bi-box-arrow-in-right"

    const right = "right"
    const left = "left"
    const up = "up"
    const down = "down"

    switch (cell) {
        case "S":
            return start;
        case "F":
            return finish;
        case "U":
            return arrowPrefix + up;
        case "D":
            return arrowPrefix + down;
        case "L":
            return arrowPrefix + left;
        case "R":
            return arrowPrefix + right;
        default:
            console.log(`Invalid Cell Value ${cell}`)
    }
}
