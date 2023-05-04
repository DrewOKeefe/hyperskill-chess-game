package chess

import kotlin.system.exitProcess

val board = MutableList(8) { MutableList(8) {" "} }
var player = ""
var p1 = ""
var p2 = ""
var enPassant = false
var col1 = 0
var row1 = 0
var col2 = 0
var row2 = 0
var playerPiece = ""
var enemyPiece = ""
var homeRow = 0
var color = ""
var stalemate = false

fun getPlayers() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    p1 = readln()
    println("Second Player's name:")
    p2 = readln()
    player = p1
}

fun setBoard() {
    for (i in 0..7) board[6][i] = "B"
    for (i in 0..7) board[1][i] = "W"
}

fun printBoard() {
    val divider = "  +---+---+---+---+---+---+---+---+"
    for (row in 8 downTo 1) {
        println(divider)
        print("$row |")
        for (col in 0..7) {
            print(" ${board[row - 1][col]} |")
        }
        println()
    }
    println(divider)
    println("   a   b   c   d   e   f   g   h")
}

fun playChess() {
    val regex = "[a-h][1-8][a-h][1-8]".toRegex()

    while (true) {
        println("$player's turn:")
        if (player == p1) {
            color = "white"
            playerPiece = "W"
            enemyPiece = "B"
            homeRow = 1
        } else {
            color = "black"
            playerPiece = "B"
            enemyPiece = "W"
            homeRow = 6
        }
        val input = readln()
        when {
            input == "exit" -> {
                println("Bye!")
                exitProcess(0)
            }
            regex.matches(input) -> {
                col1 = input[0].code - 97
                row1 = input[1].code - 49
                col2 = input[2].code - 97
                row2 = input[3].code - 49
                validMoveCheck()
            }
            else -> println("Invalid Input")
        }
    }
}

fun captureCheck() {
    if (board[row2][col2] == enemyPiece) {
        enPassant = false
        movePiece(playerPiece)
    } else if (board[row2][col2] == " " && board[row1][col2] == enemyPiece && enPassant) {
        moveEnPassant(playerPiece)
    } else {
        println("Invalid Input")
        checkWin()
        playChess()
    }
}

fun moveEnPassant(piece: String) {
    board[row1][col1] = " "
    board[row1][col2] = " "
    board[row2][col2] = piece
    player = if (player == p1) p2 else p1
    enPassant = false
    printBoard()
    checkWin()
    playChess()
}

fun movePiece(piece: String) {
    board[row1][col1] = " "
    board[row2][col2] = piece
    printBoard()
    checkWin()
    player = if (player == p1) p2 else p1
    playChess()
}

fun validMoveCheck() {
    if (board[row1][col1] != playerPiece) {
        println("No $color pawn at ${col2 + 97}${row2 + 49}")
        playChess()
    }
    when {
        board[row2][col2] == " " && row1 == homeRow && col1 == col2 &&
                (if (player == p1) row2 == row1 + 2 else row2 == row1 - 2) -> {
            enPassant = true
            movePiece(playerPiece)
        }
        board[row2][col2] == " " && col1 == col2 &&
                (if (player == p1) row2 == row1 + 1 else row2 == row1 - 1) -> {
            enPassant = false
            movePiece(playerPiece)
        }
        (col2 == col1 - 1 || col2 == col1 + 1) &&
                (if (player == p1) row2 == row1 + 1 else row2 == row1 - 1) -> {
            captureCheck()
        }
        else -> {
            println("Invalid Input")
            playChess()
        }
    }
}

fun checkWin() {
    var blackCount = 0
    var whiteCount = 0
    for (row in 0..7) {
        for (col in 0..7) {
            if (board[row][col] == "B") blackCount++
            if (board[row][col] == "W") whiteCount++
        }
    }
    if (blackCount > whiteCount && whiteCount == 0) {
        println("Black Wins!\nBye!")
        exitProcess(0)
    }
    if (whiteCount > blackCount && blackCount == 0) {
        println("White Wins!\nBye!")
        exitProcess(0)

    }
    if (row2 == 0) {
        println("Black Wins!\nBye!")
        exitProcess(0)
    }
    if (row2 == 7) {
        println("White Wins!\nBye!")
        exitProcess(0)
    }
    for (row in 0..7) {
        for (col in 0..7) {
            if (
                (board[row][col] == enemyPiece && (if (player == p1) board[row - 1][col] != playerPiece else board[row + 1][col] != playerPiece)) || whiteCount == blackCount) {
                stalemate = false
                return
            }
            if (board[row][col] == enemyPiece && (if (player == p1) board[row - 1][col] == playerPiece else board[row + 1][col] == playerPiece)) {
                stalemate = true
            }
        }
    }
    if (stalemate) {
        println("Stalemate!\nBye!")
        exitProcess(0)
    }
}

fun main() {
    getPlayers()
    setBoard()
    printBoard()
    playChess()
}