package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.data.model

case class Order(
                  pizzas: Seq[Pizza],
                  customer: Customer
                )