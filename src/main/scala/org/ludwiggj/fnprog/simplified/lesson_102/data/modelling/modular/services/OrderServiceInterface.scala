package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.services

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.data.model.Order
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.database.PizzaDaoInterface

trait OrderServiceInterface {

  // implementing classes should provide their own database
  // that is an instance of PizzaDaoInterface, such as
  // MockPizzaDao, TestPizzaDao, or ProductionPizzaDao
  protected def database: PizzaDaoInterface

  def calculateOrderPrice(o: Order): Money
}