package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.functional.objects

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common._

object MockPizzaDao {

  def getToppingPrices: Map[Topping, Money] = {
    Map(
      Cheese -> BigDecimal(1),
      Pepperoni -> BigDecimal(1),
      Sausage -> BigDecimal(1),
      Mushrooms -> BigDecimal(1)
    )
  }

  def getCrustSizePrices: Map[CrustSize, Money] = {
    Map(
      SmallCrustSize -> BigDecimal(0),
      MediumCrustSize -> BigDecimal(1),
      LargeCrustSize -> BigDecimal(2)
    )
  }

  def getCrustTypePrices: Map[CrustType, Money] = {
    Map(
      RegularCrustType -> BigDecimal(0),
      ThickCrustType -> BigDecimal(1),
      ThinCrustType -> BigDecimal(1)
    )
  }

}