package org.ludwiggj.fnprog.simplified.lesson_085.state.monad

// run represents an operation on the current state that will produce an output and a new state
case class State[S, A](run: S => (S, A)) {
  // See https://stackoverflow.com/questions/34407212/scala-and-state-monad
  def flatMap[B](g: A => State[S, B]): State[S, B] = State(run = { s0: S =>
    // Run based on current (input) state
    println("Run 1 (before)")
    val (s1: S, a: A) = this.run(s0)
    println(s"Run 1  (after): $a")
    // Run on the next state
    val newState: (S, B) = g(a).run(s1)
    println(s"Run 2  (after): ${newState._2}")
    newState
  })

  def map[B](f: A => B): State[S, B] = flatMap(a => State.lift(f(a)))

  // Alternative 1
//    def map[B](f: A => B): State[S, B] = State { s0: S =>
//      val (s1: S, a: A) = this.run(s0)
//      val b: B = f(a)
//      val st: State[S, B] = State(run = (s: S) => (s, b))
//      val res: (S, B) = st.run(s1)
//      res
//    }

  // Alternative 2 (simpler)
//  def map[B](f: A => B): State[S, B] = State { s0: S =>
//    val (s1: S, a: A) = this.run(s0)
//    (s1, f(a))
//  }
}

object State {
  def lift[S, A](v: A): State[S, A] = State(run = s => (s, v))
}