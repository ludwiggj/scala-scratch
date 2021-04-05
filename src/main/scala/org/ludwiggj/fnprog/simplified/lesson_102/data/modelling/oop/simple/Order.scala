package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.oop.simple

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money

import scala.collection.mutable.ArrayBuffer

class Order {

  // an order contains a list of pizzas
  val pizzas: ArrayBuffer[Pizza] = ArrayBuffer[Pizza]()

  // could be a constructor parameter *if* you always create
  // a customer before creating an order
  var customer: Customer = _

  def addPizzaToOrder(p: Pizza): Unit = {
    pizzas += p
  }

  def removePizzaFromOrder(p: Pizza): Unit = {
    pizzas -= p
  }

  // TODO
  //def calculateOrderPrice(): Money = ???
  def getBasePrice: Money = ???

  def getTaxes: Money = ???

  def getTotalPrice: Money = ???
}