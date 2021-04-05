package org.ludwiggj.fnprog.simplified.lesson_091.stateT.monads

case class StateT[M[_], S, A](run: S => M[(S, A)]) {
  def flatMap[B](g: A => StateT[M, S, B])(implicit M: Monad[M]): StateT[M, S, B] = StateT { (s0: S) =>
    val v1: M[(S, A)] = run(s0)

    // Monad flatMap signature is:
    // def flatMap[(S, A), (S, B)](ma: M[(S, A)])(f: (S, A) => M[(S, B)]): M[(S, B)]
    val v2: M[(S, B)] = M.flatMap(v1) {
      case (s1, a) =>
        val v3: M[(S, B)] = g(a).run(s1)
        v3
    }

    v2
  }

  def map[B](f: A => B)(implicit M: Monad[M]): StateT[M, S, B] = flatMap(a => StateT.point(f(a)))
}

object StateT {
  // Monad lift signature is:
  // def lift[(S, A)](a: => (S, A)): M[(S, A)]
  def point[M[_], S, A](v: A)(implicit M: Monad[M]): StateT[M, S, A] = StateT(run = s => M.lift((s, v)))
}