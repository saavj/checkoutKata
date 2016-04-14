package com.saacole.checkoutkata

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class CheckoutSpec extends FlatSpec {

  trait TestTransaction {

    // Given 
    val sku                   = "A"
    val price                 = 50
    val oneItemTransaction    = new Checkout(Map(sku -> price))

    val skus                  = List("B", "C", "D")
    val prices                = List(10, 20, 30)
    val totalPrice            = 60
    val priceRules            = (skus zip prices).toMap
    val multiItemTransaction  = new Checkout(priceRules)

    val emptyTransaction      = new Checkout(Map())

  }

  "scanItem" should "return price if sku is available" in new TestTransaction {

  	// When 
  	val actualPrice = oneItemTransaction.scanItem(sku)
    
    // Then 
    actualPrice === price
  }

  it should "produce SKUNotFound when sku doesn't exist in pricing rules" in new TestTransaction {

    // Given 
    val unknownSku = "unknown"
    
    // Then 
    the [Exception] thrownBy {        
      emptyTransaction.scanItem(unknownSku)
    } should have message ("SKU not found")
  }

  "scanItems" should "return a list of prices" in new TestTransaction {

    // When
    val listOfPrices = multiItemTransaction.scanItems(skus)
    
    // Then 
    listOfPrices === prices
  }

  it should "return an empty list for an empty list (not error)" in new TestTransaction {

    // Given 
    val listOfPrices = emptyTransaction.scanItems(List())
    
    // Then   
    listOfPrices === List()   
  }

  it should "produce SKUNotFound when sku doesn't exist in pricing rules" in new TestTransaction {

        // Given 
    val unknownSku = "unknown"
    
    // Then 
    the [Exception] thrownBy {        
      emptyTransaction.scanItems(List(unknownSku))
    } should have message ("SKU not found")
  }

  "calcTotal" should "sum a list of prices" in new TestTransaction {

    // Given 
    val actualTotal = multiItemTransaction.calcTotal(prices) 
    
    // Then 
    actualTotal === totalPrice
  }

  it should "return 0 for empty list" in new TestTransaction {

    // Given 
    val actualTotal = emptyTransaction.calcTotal(List()) 
    
    // Then 
    actualTotal === 0
  }
}
