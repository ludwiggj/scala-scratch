package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.data.model

case class Address(
                    street1: String,
                    street2: Option[String],
                    city: String,
                    state: String,
                    zipCode: String
                  )