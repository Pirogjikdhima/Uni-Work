const restartButton = document.getElementById('restart-button');
const switchModeButton = document.getElementById('switch-mode-button'); 
const player1 = document.getElementById('player-1');
const player2 = document.getElementById('player-2');
const tie = document.getElementById('tie');
const result1 = document.getElementById('result-1');
const result2 = document.getElementById('result-2');
const result3 = document.getElementById('result-3');
const board = document.querySelector('.game-board');

let currentPlayer = '×';
let cells = board.children;
let isComputer = false; 

result1.textContent = 0;
result2.textContent = 0;
result3.textContent = 0;

restartButton.hidden = true;
switchModeButton.hidden = true;


window.onload = function () {
    initializeGame();
};

function initializeGame() {
    chooseMode();
    setupSwitchModeButton();
}

function chooseMode() {
    const mode = prompt("Choose mode:\n1. Player vs Player\n2. Player vs Computer", "1");
    if (mode === "2") {
        isComputer = true;
        player1.textContent = "You (X)";
        player2.textContent = "AI (O)";
    } else {
        isComputer = false;
        setNames();
    }
}

function setNames() {
    let user1 = prompt("Enter Player 1 name", "Player X");
    let user2 = prompt("Enter Player 2 name", "Player O");

    player1.textContent = user1 || "Player X";
    player2.textContent = user2 || "Player O";
}

function checkWin() {
    const winConditions = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6],
    ];

    for (const [a, b, c] of winConditions) {
        if (cells[a].innerHTML === cells[b].innerHTML && cells[a].innerHTML === cells[c].innerHTML && cells[a].innerHTML !== '') {
            
            cells[a].classList.add('win');
            cells[b].classList.add('win');
            cells[c].classList.add('win');
           
            updateScore(cells[a].innerHTML);
            restartButton.hidden = false;
            switchModeButton.hidden = false;
            return true;
        }
    }

    if (Array.from(cells).every(cell => cell.innerHTML !== '')) {
        updateScore('draw');
        restartButton.hidden = false;
        switchModeButton.hidden = false;
        return false;
    }
}

function updateScore(winner) {
    if (winner === '×') {
        result1.textContent = parseInt(result1.textContent) + 1;
    } else if (winner === 'O') {
        result3.textContent = parseInt(result3.textContent) + 1;    
    } else {
        result2.textContent = parseInt(result2.textContent) + 1;
    }
}

function makeMove(cell) {
    if (isEmpty(cell)) {
        mark(cell, currentPlayer);
        if (!checkWin()) {
            currentPlayer = togglePlayer(currentPlayer);
            if (isComputer && currentPlayer === 'O') {
                computerMove();
            }
        }
    }
}

function isEmpty(cell) {
    return cell.innerHTML === '';
}

function mark(cell, symbol) {
    cell.innerHTML = symbol;
    cell.classList.add(symbol);
}

function togglePlayer(player) {
    return player === '×' ? 'O' : '×';
}

function computerMove() {
    const emptyCells = Array.from(cells).filter(cell => isEmpty(cell));
    if (emptyCells.length > 0) {
        const randomCell = emptyCells[Math.floor(Math.random() * emptyCells.length)];
        setTimeout(() => {
            mark(randomCell, 'O');
            if (!checkWin()) {
                currentPlayer = togglePlayer(currentPlayer);
            }
        }, 500);
    }
}

function setupSwitchModeButton() {
    switchModeButton.addEventListener('click', () => {
        resetBoard();
        resetScore();
        chooseMode();
    });
}

function resetBoard() {
    Array.from(cells).forEach(cell => {
        cell.innerHTML = '';
        cell.className = 'cell';
    });

    currentPlayer = '×';
    restartButton.hidden = true;
}
function resetScore() {
    result1.textContent = 0;
    result2.textContent = 0;
    result3.textContent = 0;
}
restartButton.addEventListener('click', () => {
    resetBoard();
    switchModeButton.hidden = true;
});
