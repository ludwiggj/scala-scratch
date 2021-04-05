package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.database

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common._

trait PizzaDaoInterface {
  def getToppingPrices: Map[Topping, Money]

  def getCrustSizePrices: Map[CrustSize, Money]

  def getCrustTypePrices: Map[CrustType, Money]
}