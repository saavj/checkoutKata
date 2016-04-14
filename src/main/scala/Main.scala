package com.saacole.checkoutkata

import scala.util.Try

object Main extends App {
	println(  
  s"""
    |**************************************
		|*  Welcome to the Shopping Checkout  *
		|**************************************
   """.stripMargin)

	var transaction1 = new Checkout(Map(
			"A" -> 50, 
			"B" -> 30, 
			"C" -> 20,
			"D" -> 15
		))

	try { transaction1.calcTotal(transaction1.scanItems(List("E"))) 			} catch { case e: Throwable => println(e) }
	try { transaction1.calcTotal(transaction1.scanItems(List("A, B, C"))) } catch { case e: Throwable => println(e) }
	try { transaction1.calcTotal(transaction1.scanItems(List())) 					} catch { case e: Throwable => println(e) }

}