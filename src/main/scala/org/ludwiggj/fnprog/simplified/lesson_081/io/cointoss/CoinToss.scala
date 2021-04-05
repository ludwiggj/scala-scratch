package org.ludwiggj.fnprog.simplified.lesson_081.io.cointoss

import scala.io.StdIn
import scala.util.Random

object CoinToss {

  // impure
  def showPrompt(): Unit = {
    print("\n(h)eads, (t)ails, or (q)uit: ")
  }

  // impure
  private def userInput(): String = StdIn.readLine.trim.toUpperCase

  private def printableFlipResult(flip: String) = flip match {
    case "H" => "Heads"
    case "T" => "Tails"
  }

  // impure
  private def printGameState(gameState: GameState): Unit = {
    println(s"#Flips: ${gameState.numFlips}, #Correct: ${gameState.numCorrect}")
  }

  // impure
  private def printGame(printableResult: String, gameState: GameState): Unit = {
    print(s"Flip was $printableResult.")
    printGameState(gameState)
  }

  // impure
  private def printGameOver(): Unit = println("\n=== GAME OVER ===")

  // impure
  private def tossCoin(r: Random): String = {
    val i = r.nextInt(2)
    i match {
      case 0 => "H"
      case 1 => "T"
    }
  }

  def createNewGameState(userInput: String,
                         coinTossResult: String,
                         gameState: GameState,
                         newNumFlips: Int): GameState = {
    if (coinTossResult == userInput) {
      GameState(newNumFlips, gameState.numCorrect + 1)
    } else {
      GameState(newNumFlips, gameState.numCorrect)
    }
  }

  def mainLoop(gameState: GameState, random: Random): IO[Unit] = for {
    _ <- IO { showPrompt() }
    userInput <- IO { userInput() }
    _ <- if (userInput == "H" || userInput == "T") for {
      // this first line is a hack;
      // a for-expression must begin with a generator
      _ <- IO { println(s"you said $userInput") }
      coinTossResult = tossCoin(random)
      newNumFlips = gameState.numFlips + 1
      newGameState = createNewGameState(
        userInput,
        coinTossResult,
        gameState,
        newNumFlips
      )
      _ <- IO {
        printGame(
          printableFlipResult(coinTossResult),
          newGameState
        )
      }
      _ <- mainLoop(newGameState, random)
    } yield Unit
    else for {
      _ <- IO { println("did not enter H or T") }
      _ <- IO { printGameOver() }
      _ <- IO { printGameState(gameState) }
    } yield Unit
  } yield Unit

  def main(args: Array[String]): Unit = {
    val s = GameState(0, 0)
    val r = new Random()
    mainLoop(s, r).run
  }
}
