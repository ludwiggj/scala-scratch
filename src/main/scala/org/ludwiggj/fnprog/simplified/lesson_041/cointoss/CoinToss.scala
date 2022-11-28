package org.ludwiggj.fnprog.simplified.lesson_041.cointoss

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.Random

object CoinToss {
  def showPrompt(): Unit = {
    print("\n(h)eads, (t)ails, or (q)uit: ")
  }

  def getUserInput: String = StdIn.readLine().trim.toUpperCase

  def printableFlipResult(flip: String): String = flip match {
    case "H" => "Heads"
    case "T" => "Tails"
  }

  def printGameState(gameState: GameState): Unit = {
    println(s"#Flips: ${gameState.numFlips}, #Correct: ${gameState.numCorrect}")
  }

  def printGameState(printableResult: String, gameState: GameState): Unit = {
    print(s"Flip was $printableResult.")
    printGameState(gameState)
  }

  def printGameOver(): Unit = println("\n=== GAME OVER ===")

  // returns "H" for heads, "T" for tails
  def tossCoin(r: Random): String = {
    val i = r.nextInt(2)
    i match {
      case 0 => "H"
      case 1 => "T"
    }
  }

  @tailrec
  def mainLoop(s: GameState, r: Random): Unit = {
    showPrompt()
    val guess = getUserInput
    guess match {
      case "H" | "T" =>
        val coinState = tossCoin(r)
        val newGameState = if (guess == coinState) {
          s.copy(numCorrect = s.numCorrect + 1, numFlips = s.numFlips + 1)
        } else {
          s.copy(numFlips = s.numFlips + 1)
        }
        printGameState(coinState, newGameState)
        mainLoop(newGameState, r)

      case _ =>
        // Assume q was typed...
        printGameOver()
        printGameState(s)
    }
  }

  def main(args: Array[String]): Unit = {
    val s = GameState(0, 0)
    val r = new Random()
    mainLoop(s, r)
  }
}