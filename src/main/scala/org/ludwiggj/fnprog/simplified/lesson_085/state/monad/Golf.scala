package org.ludwiggj.fnprog.simplified.lesson_085.state.monad

object Golf {

  object Game {

    case class GolfState(distance: Int)

    // Given a state and an input, how to produce next state
    def swing(distance: Int): State[GolfState, Int] = State(run = {
      previousState: GolfState =>
        val newAmount = previousState.distance + distance
        (GolfState(newAmount), newAmount)
    })

    def playDesugared(): Unit = {
      val stateWithNewDistance: State[GolfState, Int] =
        swing(distance = 20).flatMap((x: Int) => {
          println(s"x = $x")
          swing(distance = 15).flatMap((y: Int) => {
            println(s"y = $y")
            swing(distance = 0).map(
              (totalDistance: Int) => totalDistance
            )
          })
        })

      // initialize a `GolfState`
      val beginningState = GolfState(0)

      // run/execute the effect.
      // `run` is like `unsafeRunSync` in the Cats `IO` monad.
      val result: (GolfState, Int) = stateWithNewDistance.run(beginningState)
      println(s"GolfState: ${result._1}") // GolfState(35)
      println(s"Total Distance: ${result._2}") // 35
    }

    def play(): Unit = {
      val stateWithNewDistance: State[GolfState, Int] = for {
        _ <- swing(20)
        _ <- swing(15)
        totalDistance <- swing(0)
      } yield totalDistance

      // initialize a `GolfState`
      val beginningState = GolfState(0)

      // run/execute the effect.
      // `run` is like `unsafeRunSync` in the Cats `IO` monad.
      val result: (GolfState, Int) = stateWithNewDistance.run(beginningState)
      println(s"GolfState: ${result._1}") // GolfState(35)
      println(s"Total Distance: ${result._2}") // 35
    }
  }

  object Tournament {

    case class GolfState(distances: List[Int]) {
      def pos: Int = distances.sum
    }

    def prepareSwing(distance: Int): State[GolfState, Int] = State { prevState: GolfState =>
      val newState = GolfState(distance +: prevState.distances)
      (newState, newState.pos)
    }

    def executeSwing(currentPos: Int,
                     nextSwing: State[GolfState, Int],
                     currentState: GolfState = GolfState(Nil)): (GolfState, Int) = {
      println(s"Current position $currentPos")
      val (gs, newPos): (GolfState, Int) = nextSwing.run(currentState)
      println(s"Shot! New position: $newPos")
      (gs, newPos)
    }

    def prepareAndExecuteSwing(distance: Int, currentState: GolfState = GolfState(Nil)): GolfState = {
      val nextSwing: State[GolfState, Int] = prepareSwing(distance)
      println(s"About to shoot $distance...")
      val (gs, pos) = executeSwing(currentState.pos, nextSwing, currentState)
      println(s"shot! distance: $gs, position: $pos\n\n")
      gs
    }

    object ThreeShotGame {
      def play(): Unit = {
        val firstSwing: State[GolfState, Int] = prepareSwing(20)
        val (gs, pos) = executeSwing(0, firstSwing)
        println(s"gs: $gs, pos: $pos\n\n")

        val secondSwing: State[GolfState, Int] = prepareSwing(47)
        val (gs2, pos2) = executeSwing(pos, secondSwing, gs)
        println(s"gs: $gs2, pos: $pos2\n\n")

        val thirdSwing: State[GolfState, Int] = prepareSwing(31)
        val (gs3, pos3) = executeSwing(pos2, thirdSwing, gs2)
        println(s"gs: $gs3, pos: $pos3\n\n")
      }
    }

    object FourShotGame {
      def play(): Unit = {
        val gs1 = prepareAndExecuteSwing(58)
        val gs2 = prepareAndExecuteSwing(114, gs1)
        val gs3 = prepareAndExecuteSwing(71, gs2)
        prepareAndExecuteSwing(13, gs3)
      }
    }

    object ThreeShotGameWithMap {
      def play(): Unit = {
        val firstSwing: State[GolfState, Int] = prepareSwing(20).map(_ * 2)
        println("About to shoot 20.map (doubled)...")
        val (gs, pos) = executeSwing(0, firstSwing)
        println(s"gs: $gs, pos: $pos\n\n")

        val secondSwing: State[GolfState, Int] = prepareSwing(47).map(_ * 3)
        println("About to shoot 47.map (trebled)...")
        val (gs2, pos2) = executeSwing(pos, secondSwing, gs)
        println(s"gs: $gs2, pos: $pos2\n\n")

        val thirdSwing: State[GolfState, Int] = prepareSwing(13).map(_ * 10)
        println("About to shoot 13 (* 10)...")
        val (gs3, pos3) = executeSwing(pos2, thirdSwing, gs2)
        println(s"gs: $gs3, pos: $pos3\n\n")
      }
    }

    object TwoShotGameFlatmapped {
      def play(): Unit = {
        val firstSwing: State[GolfState, Int] = prepareSwing(84)
        val secondSwing: State[GolfState, Int] = prepareSwing(16)

        val swingOneSwingTwo = firstSwing.flatMap(a => {
          println(s"Received $a")
          secondSwing
        })

        val (gs1, pos1) = executeSwing(0, swingOneSwingTwo)
        println(s"gs: $gs1, pos: $pos1\n\n")

        val swingTwoSwingOne = secondSwing.flatMap(_ => {
          firstSwing
        })

        val (gs2, pos2) = executeSwing(0, swingTwoSwingOne)
        println(s"gs: $gs2, pos: $pos2\n\n")

        val doubleFirstSwing = firstSwing.flatMap(shot => prepareSwing(shot))
        val (gs3, pos3) = executeSwing(0, doubleFirstSwing)
        println(s"gs: $gs3, pos: $pos3\n\n")

        val doubleSecondSwing = secondSwing.flatMap(_ => secondSwing)
        val (gs4, pos4) = executeSwing(0, doubleSecondSwing)
        println(s"gs: $gs4, pos: $pos4\n\n")
      }
    }

    object ForSwings {
      def play(): Unit = {
        val game: State[GolfState, Int] = for {
          _ <- prepareSwing(20)
          _ <- prepareSwing(15)
          totalDistance <- prepareSwing(0)
        } yield totalDistance

        println(s"Total distance = ${game.run(GolfState(Nil))}")
      }
    }

  }

  def main(args: Array[String]): Unit = {
    // Game.play()
    Game.playDesugared()
    //    Tournament.ThreeShotGame.play()
    //    Tournament.FourShotGame.play()
    //    Tournament.ThreeShotGameWithMap.play()
    //    Tournament.TwoShotGameFlatmapped.play()
    //    Tournament.ForSwings.play()
  }
}