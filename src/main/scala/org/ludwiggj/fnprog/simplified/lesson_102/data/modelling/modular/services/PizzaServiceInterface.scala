package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.services

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common._
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.data.model.Pizza

trait PizzaServiceInterface {
  def addTopping(p: Pizza, t: Topping): Pizza

  def removeTopping(p: Pizza, t: Topping): Pizza

  def removeAllToppings(p: Pizza): Pizza

  def updateCrustSize(p: Pizza, cs: CrustSize): Pizza

  def updateCrustType(p: Pizza, ct: CrustType): Pizza

  def calculatePizzaPrice(
                           p: Pizza,
                           toppingsPrices: Map[Topping, Money],
                           crustSizePrices: Map[CrustSize, Money],
                           crustTypePrices: Map[CrustType, Money]
                         ): Money
}
