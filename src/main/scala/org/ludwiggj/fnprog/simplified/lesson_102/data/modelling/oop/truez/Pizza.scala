package org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.oop.truez

import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common.CommonModels.Money
import org.ludwiggj.fnprog.simplified.lesson_102.data.modelling.common._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * This class shows an example of a “real” OOP design.
 */
class Pizza {

  // private fields
  private var _crustSize: CrustSize = MediumCrustSize
  private var _crustType: CrustType = RegularCrustType
  private val _toppings = ArrayBuffer[Topping]()

  def getCrustSize: CrustSize = _crustSize

  def setCrustSize(cs: CrustSize) {
    _crustSize = cs
  }

  def getCrustType: CrustType = _crustType

  def setCrustType(ct: CrustType) {
    _crustType = ct
  }

  def getToppings: mutable.Seq[Topping] = _toppings

  def addTopping(t: Topping) {
    _toppings += t
  }

  // other built-in methods
  def removeTopping(t: Topping) {
    _toppings -= t
  }

  def removeAllToppings() {
    _toppings.clear()
  }

  def getPrice: Money = ???
}