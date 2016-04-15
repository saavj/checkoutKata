package com.saacole.checkoutkata

object Main extends App {
	println(  
  s"""
    |**************************************
    |*  Welcome to the Shopping Checkout  *
    |**************************************
   """.stripMargin)

	var transaction1 = new Checkout(Map(
			"A" -> Prices(50, Some("3 for 130")), 
			"B" -> Prices(30, Some("2 units for 45")), 
			"C" -> Prices(20),
			"D" -> Prices(15)
		))

	try { transaction1.calcTotal(transaction1.scanItems(List("E"))) } catch { case e: Throwable => println(e) }
	try { transaction1.calcTotal(transaction1.scanItems(List("A", "B", "C", "B"))) } catch { case e: Throwable => println(e) }
	try { transaction1.calcTotal(transaction1.scanItems(List())) } catch { case e: Throwable => println(e) }

}