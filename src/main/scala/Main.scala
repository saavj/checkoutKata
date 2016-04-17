package com.saacole.checkoutkata

import scala.io.StdIn.readLine

object Main extends App {

  // CHANGE RULES HERE

  val pricingRules = Map(
    "A" -> Prices(50, Some("3 for 130")),
    "B" -> Prices(30, Some("2 for 45")),
    "C" -> Prices(20),
    "D" -> Prices(15)
  )

  println(s"""
    |**************************************
    |*  Welcome to the Shopping Checkout  *
    |**************************************
    |
    |Current Pricing Rules:
    |
    |${pricingRules.map(pair => pair._1 + " = " + pair._2).mkString("\n")}
    |
    |Commands are as follows:
    |
    |- add [SKU] - To add an item / items to your basket
    |              Seperate sku's with a space
    |              e.g. "add A B C"
    |
    |- price     - Adds up the current contents of
    |              your basket
    |
    |- clear     - Clears you basket
    |
    |- list      - Lists all the items in your basket
    |
    |- quit      - Leaves the current checkout transaction
    |
    |
   """.stripMargin)

  val checkout = new Checkout(pricingRules)

  private def readInput(checkout: Checkout, skus: List[String]): Unit =
    readLine.split("[ \t]+").toList match {

    case "add" :: x   =>
      println("Adding: " + x)
      readInput(checkout, skus ++ x)

    case "price" :: _ =>
      try {
        println("Total: " + checkout.calcTotal(checkout.scanItems(skus)))
      } catch { case e: Throwable =>
        println(s"Failed to price basket - ${e.getMessage}")
      }

      readInput(checkout, skus)

    case "clear" :: _ =>
      println("Basket is now empty")
      readInput(checkout, List())

    case "list" :: _  =>
      println("Current skus in your basket: " + skus)
      readInput(checkout, skus)

    case "quit" :: _  => println("Thank you! Goodbye :)")
    case _            => readInput(checkout, skus)
  }

  readInput(checkout, List())
}
