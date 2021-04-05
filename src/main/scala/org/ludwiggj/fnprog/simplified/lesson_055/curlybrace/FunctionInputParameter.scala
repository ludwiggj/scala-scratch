package org.ludwiggj.fnprog.simplified.lesson_055.curlybrace

object FunctionInputParameter {

  object ClassWithFIP {

    // DEFINITION 1: Given the following definition:

    // val x = FOO { (a: String) =>
    //  // more code here
    // }

    // POSSIBILITY 1: A case class that takes a function parameter (FIP = Function Input Parameter)

    // NOTE:          It cannot take a by-name parameter, as that syntax does not compile unless the
    //                function returns unit (this contradicts page 638).
    //                See also https://stackoverflow.com/questions/2647141/by-name-parameters-for-constructors

    // FIP with String argument
    case class StringToInt(run: String => Int)

    // Declare FIP inline
    object InlineFIP {
      private val stringToInt = StringToInt { s: String =>
        s.length
      }

      def run(): Unit = println(s"Length of bananas is ${stringToInt.run("bananas")}")
    }

    // FIP as standalone
    object FIPAsStandalone {
      def len(s: String): Int = s.length

      private val stringToInt = StringToInt(len)

      def run(): Unit = println(s"Length of fruit is ${stringToInt.run("fruit")}")
    }

    // FIP as standalone
    object FIPAsStandaloneVariation {
      val len: String => Int = _.length
      private val stringToInt = StringToInt(len)

      def run(): Unit = println(s"Length of pineapple is ${stringToInt.run("pineapple")}")
    }

    // Inline FIP with generic type
    object InlineFIPGeneric {

      case class GenericToInt[A](run: A => Int)

      private val stringToInt = GenericToInt { s: String =>
        s.length
      }

      def run(): Unit = println(s"Length of grapes is ${stringToInt.run("grapes")}")
    }

    object MoreComplexFIP {
      def run(): Unit = {
        case class Transform2ParamsTo1Param[A, B](fun: (A, A) => B)

        val addStringLengths = Transform2ParamsTo1Param { (a: String, b: String) =>
          a.length + b.length
        }

        println(s"Combined length of foo & bar is: ${addStringLengths.fun("foo", "bar")}")

        val addTwoIntegers = Transform2ParamsTo1Param { (a: Int, b: Int) =>
          a + b
        }

        println(s"1 + 2 = ${addTwoIntegers.fun(1, 2)}")
      }
    }

    // POSSIBILITY 2: A function that takes an FIP
    //                But since it doesn't have any other input, this is effectively an edge case,
    //                as it cannot depend on any other input. In this example it's the identity function
    object FunctionWithFIP {
      def run(): Unit = {
        def s2i(f: String => Int): String => Int = f

        println(s"Length of Hello is ${
          val f = s2i { s: String =>
            s.length
          }

          // Needs to be explicitly called here
          f("Hello")
        }")
      }
    }
  }

  object FunctionOrClassWithFIP {
    // DEFINITION 2: Given the following definition:

    // val res = s2i("hello") { s: String =>
    //   s.length
    // }

    // POSSIBILITY 1: A function that takes a FIP
    object FunctionWithFIP {
      def run(): Unit = {
        def s2i(s: String)(f: String => Int): Int = f(s)

        println(s"Length of Hello is ${
          s2i("Hello") { s: String =>
            s.length
          }
        }")
      }
    }

    object FunctionWithFIPGeneric {
      def run(): Unit = {
        def s2i[A](s: A)(f: A => Int): Int = f(s)

        println(s"Length of Sailor is ${
          s2i("Sailor") { s: String =>
            s.length
          }
        }")

        println(s"Square of 9 is ${
          s2i(9) { s: Int =>
            s * s
          }
        }")
      }
    }

    // POSSIBILITY 2: A case class that takes a FIP
    object ClassWithFIP {
      def run(): Unit = {
        case class s2i(s: String)(f: String => Int) {
          // NOTE: f is not automatically exposed outside of the class as it is not in the first parameter group,
          //       so we'll have to define a function to call
          def fun: Int = f(s)
        }

        val stringToInt = s2i("Bonjour") { s: String =>
          s.length
        }

        println(s"Length of Bonjour is ${stringToInt.fun}")
      }
    }

    object ClassWithFIPGeneric {
      def run(): Unit = {
        case class s2i[A](s: A)(f: A => Int) {
          // NOTE: f is not automatically exposed outside of the class as it is not in the first parameter group,
          //       so we'll have to define a function to call
          def fun: Int = f(s)
        }

        println(s"Length of Guten tag is ${
          s2i("Guten tag") { s: String =>
            s.length
          }.fun
        }")

        println(s"Cube of 9 is ${
          s2i(9) { s: Int =>
            s * s * s
          }.fun
        }")
      }
    }
  }

  def main(args: Array[String]): Unit = {
//    ClassWithFIP.InlineFIP.run()
//    ClassWithFIP.FIPAsStandalone.run()
//    ClassWithFIP.FIPAsStandaloneVariation.run()
//    ClassWithFIP.InlineFIPGeneric.run()
//    ClassWithFIP.MoreComplexFIP.run()
//    ClassWithFIP.FunctionWithFIP.run()
//    FunctionOrClassWithFIP.FunctionWithFIP.run()
//    FunctionOrClassWithFIP.FunctionWithFIPGeneric.run()
//    FunctionOrClassWithFIP.ClassWithFIP.run()
//    FunctionOrClassWithFIP.ClassWithFIPGeneric.run()
  }
}
