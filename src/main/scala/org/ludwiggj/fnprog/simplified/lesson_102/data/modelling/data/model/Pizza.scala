package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.data.model

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common._

case class Pizza(
                  crustSize: CrustSize,
                  crustType: CrustType,
                  toppings: Seq[Topping]
                )