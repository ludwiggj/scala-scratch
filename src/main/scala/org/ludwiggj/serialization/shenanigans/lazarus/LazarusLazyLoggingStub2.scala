package org.ludwiggj.serialization.shenanigans.lazarus

import java.io.{IOException, ObjectInputStream, ObjectOutputStream}

import org.apache.log4j.Logger

trait LazarusLazyLoggingStub2 extends Serializable  {
  def logger: Logger = theLogger

  @transient
  var theLogger: Logger = newStubbedLogger()

  private def newStubbedLogger(): Logger = {
    new Logger("my logger") {
      override def debug(message: Any): Unit = {
        println(s"my logger DEBUG called: [$message]")
      }

      override def info(message: Any): Unit = {
        println(s"my logger INFO called: [$message]")
      }

      override def error(message: Any, t: Throwable): Unit = {
        println(s"my logger ERROR called: [$message] exception: [${t.getMessage}]")
      }
    }
  }

  @throws(classOf[IOException])
  private def writeObject(out: ObjectOutputStream): Unit = {
    println(">>>>>>>>>>>>>>>>>>>>>>>> Custom writeObject!!! <<<<<<<<<<<<<<<<<<<<")
    println(">>>>>>>>>>>>>>>>>>>>>>>> Custom writeObject!!! <<<<<<<<<<<<<<<<<<<<")
    println(">>>>>>>>>>>>>>>>>>>>>>>> Custom writeObject!!! <<<<<<<<<<<<<<<<<<<<")
    out.defaultWriteObject()
  }

  @throws(classOf[IOException])
  private def readObject(in: ObjectInputStream): Unit = {
    println(">>>>>>>>>>>>>>>>>>>>>>>> Yo Yo Yo!!! <<<<<<<<<<<<<<<<<<<<")
    println(">>>>>>>>>>>>>>>>>>>>>>>> Yo Yo Yo!!! <<<<<<<<<<<<<<<<<<<<")
    println(">>>>>>>>>>>>>>>>>>>>>>>> Yo Yo Yo!!! <<<<<<<<<<<<<<<<<<<<")
    in.defaultReadObject()
    theLogger = newStubbedLogger()
  }
}