package org.ludwiggj.fnprog.simplified.lesson_085.state.monad

// http://blog.rcard.in/design/programming/fp/monad/2018/11/22/and-monads-for-all-state-monad.html

object StockStateMonad {
  type Stocks = Map[String, Double]

  def prices(name: String): Double = ???

  object Step1 {
    // Buys an amount (dollars) of the stock with given name.
    // Returns the number of purchased stocks, and updated portfolio
    def buy(name: String, amount: Double, portfolio: Stocks): (Double, Stocks) = {
      val purchased = amount / prices(name)
      val owned = portfolio(name)
      (purchased, portfolio + (name -> (owned + purchased)))
    }

    // Sells a quantity of stocks of the given name.
    // Returns the amount of dollars earned by the selling operation, and updated portfolio.
    def sell(name: String, quantity: Double, portfolio: Stocks): (Double, Stocks) = {
      val revenue = quantity * prices(name)
      val owned = portfolio(name)
      (revenue, portfolio + (name -> (owned - quantity)))
    }

    // Returns the quantity of stocks owned for name.
    def get(name: String, portfolio: Stocks): Double = portfolio(name)

    // Sell all the stocks for a given company
    // Using the sell’s revenue, buy stocks of another company
    // Returns the number of stocks of the first type owned, and the quantity of the newly purchased stocks

    // Note that we have to pass the correct reference to the updated stocks portfolio to every step of our algorithm
    def move(from: String, to: String, portfolio: Stocks): ((Double, Double), Stocks) = {
      val originallyOwned = get(from, portfolio)
      val (revenue, newPortfolio) = sell(from, originallyOwned, portfolio)
      val (purchased, veryNewPortfolio) = buy(to, revenue, newPortfolio)
      ((originallyOwned, purchased), veryNewPortfolio)
    }
  }

  // Stateful computation is a function that takes some state and returns a value along with some new state.

  // Buy and sell functions already behave in this sense. They return both a value and a new instance of state.

  // The type of the stateful computation quoted above is S => (A, S), where:
  //   S is the state
  //   A is the value resulting from the execution of the function.

  // Functions of this type are called state actions of state transitions.

  object Step2 {
    // Use currying to divide inputs into two groups, and to isolate the state
    def buy(name: String, amount: Double)(portfolio: Stocks): (Double, Stocks) = {
      val purchased = amount / prices(name)
      val owned = portfolio(name)
      (purchased, portfolio + (name -> (owned + purchased)))
    }

    def sell(name: String, quantity: Double)(portfolio: Stocks): (Double, Stocks) = {
      val revenue = quantity * prices(name)
      val owned = portfolio(name)
      (revenue, portfolio + (name -> (owned - quantity)))
    }

    def get(name: String)(portfolio: Stocks): (Double, Stocks) = (portfolio(name), portfolio)

    // If we pass to these functions only the first group of inputs, we obtain precisely a set
    // of functions of type S => (A, S)

    def buyPartial(name: String, amount: Double): Stocks => (Double, Stocks) =
      buy(name, amount)

    def sellPartial(name: String, quantity: Double): Stocks => (Double, Stocks) =
      sell(name, quantity)

    def getPartial(name: String): Stocks => (Double, Stocks) =
      get(name)
  }

  // The next step is to remove the currying at all. It is not elegant, and we don’t like its syntax ;)
  object Step3 {
    // Now buy function returns a function that takes portfolio as an explicit input
    def buy(name: String, amount: Double): Stocks => (Double, Stocks) = (portfolio: Stocks) => {
      val purchased = amount / prices(name)
      val owned = portfolio(name)
      (purchased, portfolio + (name -> (owned + purchased)))
    }

    def sell(name: String, quantity: Double): Stocks => (Double, Stocks) = portfolio => {
      val revenue = quantity * prices(name)
      val owned = portfolio(name)
      (revenue, portfolio + (name -> (owned - quantity)))
    }

    def get(name: String): Stocks => (Double, Stocks) = portfolio => (portfolio(name), portfolio)
  }

  // Generalise
  object Step4 {
    type Transaction[+A] = Stocks => (A, Stocks)

    def buy(name: String, amount: Double): Transaction[Double] = portfolio => {
      val purchased = amount / prices(name)
      val owned = portfolio(name)
      (purchased, portfolio + (name -> (owned + purchased)))
    }

    def sell(name: String, quantity: Double): Transaction[Double] = portfolio => {
      val revenue = quantity * prices(name)
      val owned = portfolio(name)
      (revenue, portfolio + (name -> (owned - quantity)))
    }

    def get(name: String): Transaction[Double] = portfolio => (portfolio(name), portfolio)

    // Note that we have to pass the correct reference to the updated stocks portfolio to every step of our algorithm
    def move(from: String, to: String, portfolio: Stocks): ((Double, Double), Stocks) = {
      // Don't need resulting portfolio here as it is unchanged
      val (originallyOwned, _) = get(from)(portfolio)
      val (revenue, newPortfolio) = sell(from, originallyOwned)(portfolio)
      val (purchased, veryNewPortfolio) = buy(to, revenue)(newPortfolio)
      ((originallyOwned, purchased), veryNewPortfolio)
    }
  }

  // Now we need to build our version of the State monad. Define a set of functions that let us combine states smartly,
  // i.e. Transaction[+A] instances
  object Step5 {
    type Transaction[+A] = Stocks => (A, Stocks)

    def buy(name: String, amount: Double): Transaction[Double] = portfolio => {
      val purchased = amount / prices(name)
      val owned = portfolio(name)
      (purchased, portfolio + (name -> (owned + purchased)))
    }

    def sell(name: String, quantity: Double): Transaction[Double] = portfolio => {
      val revenue = quantity * prices(name)
      val owned = portfolio(name)
      (revenue, portfolio + (name -> (owned - quantity)))
    }

    def get(name: String): Transaction[Double] = portfolio => (portfolio(name), portfolio)

    def map[A, B](tr: Transaction[A])(f: A => B): Transaction[B] = portfolio => {
      val (a, newPortfolio) = tr(portfolio)
      (f(a), newPortfolio)
    }

    def flatMap[A, B](tr: Transaction[A])(f: A => Transaction[B]): Transaction[B] = portfolio => {
      val (a, newPortfolio) = tr(portfolio)
      f(a)(newPortfolio)
    }

    def move(from: String, to: String): Transaction[(Double, Double)] =
      flatMap(get(from))(
        originallyOwned => flatMap(sell(from, originallyOwned))(
          revenue => map(buy(to, revenue))(
            purchased => (originallyOwned, purchased)
          )
        )
      )

    // Note that this requires flatmap and map to be methods on a class
//    def moveAsComprehension(from: String, to: String): Transaction[(Double, Double)] =
//      for {
//        originallyOwned <- get(from)
//        revenue <- sell(from, originallyOwned)
//        purchased <- buy(to, revenue)
//      } yield {
//        (originallyOwned, purchased)
//      }
  }
}