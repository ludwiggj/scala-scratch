package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.modular.services

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common._
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.data.model.Order

trait AbstractOrderService extends OrderServiceInterface {

  // create a concrete implementation of the trait so we can use its `calculatePizzaPrice` function
  object PizzaService extends PizzaService

  import PizzaService.calculatePizzaPrice

  // all implementations of this trait will use these functions, so go ahead and define them here
  private lazy val toppingPricesMap = database.getToppingPrices
  private lazy val crustSizePricesMap = database.getCrustSizePrices
  private lazy val crustTypePricesMap = database.getCrustTypePrices

  // the publicly-available service
  def calculateOrderPrice(o: Order): Money =
    calculateOrderPriceInternal(o, toppingPricesMap, crustSizePricesMap, crustTypePricesMap)

  private def calculateOrderPriceInternal(o: Order,
                                          toppingPrices: Map[Topping, Money],
                                          crustSizePrices: Map[CrustSize, Money],
                                          crustTypePrices: Map[CrustType, Money]
                                         ): Money = {
    val pizzaPrices: Seq[Money] = for {
      pizza <- o.pizzas
    } yield {
      calculatePizzaPrice(
        pizza,
        toppingPrices,
        crustSizePrices,
        crustTypePrices
      )
    }
    pizzaPrices.sum
  }
}