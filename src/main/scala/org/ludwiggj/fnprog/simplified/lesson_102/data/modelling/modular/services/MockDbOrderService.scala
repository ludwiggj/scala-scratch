package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.services

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.database.{MockPizzaDao, PizzaDaoInterface}

object MockDbOrderService extends AbstractOrderService {
  val database: PizzaDaoInterface = MockPizzaDao
}