package com.saacole.checkoutkata

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class CheckoutSpec extends FlatSpec {

  trait TestTransactionData {

    // Given
    val unknownSku           = "unknown"

    val skus                 = List("B", "C", "D", "C")
    val prices               = List(BigDecimal(10), BigDecimal(20), BigDecimal(30))
    val priceRules           = (skus zip prices.map(p => Prices(p))).toMap

    val multiRuleTransaction = new Checkout(priceRules)
    val emptyRuleTransaction = new Checkout(Map())
  }

  "scanItem" should "return price if sku is available" in new TestTransactionData {

    // Given
    val sku         = "A"
    val quantity    = 1
    val price       = Prices(50)
    val transaction = new Checkout(Map(sku -> price))

  	// When 
  	val actualPrice = transaction.scanItem(sku, quantity)
    
    // Then 
    actualPrice === price
  }

  it should "produce SKUNotFound when sku doesn't exist in pricing rules" in new TestTransactionData {

    // When you scan an unknow item 
    
    // Then 
    the [Exception] thrownBy {        
      emptyRuleTransaction.scanItem(unknownSku, 1)
    } should have message ("SKU not found")
  }

  "scanItems" should "return a list of prices" in new TestTransactionData {

    // When
    val actualPrices = multiRuleTransaction.scanItems(skus)
    
    // Then 
    actualPrices === prices
  }

  it should "return an empty list for an empty list (not error)" in new TestTransactionData {

    // When 
    val actualPrices = emptyRuleTransaction.scanItems(List())
    
    // Then   
    actualPrices === List()   
  }

  it should "produce SKUNotFound when sku doesn't exist in pricing rules" in new TestTransactionData {

    // When one of the SKUs don't exist
    
    // Then 
    the [Exception] thrownBy {        
      emptyRuleTransaction.scanItems(List(unknownSku))
    } should have message ("SKU not found")
  }

  "calcTotal" should "sum a list of prices" in new TestTransactionData {

    // When 
    val actualTotal = multiRuleTransaction.calcTotal(prices) 
    
    // Then 
    actualTotal === 60
  }

  it should "return 0 for empty list" in new TestTransactionData {

    // When
    val actualTotal = emptyRuleTransaction.calcTotal(List()) 
    
    // Then 
    actualTotal === 0
  }


  "calcSpecialPrice" should "return a total cost for a quantity that matches offer exactly" in new TestTransactionData {

    // Given
    val offer     = "3 for 130"
    val quantity  = 3
    val unitPrice = BigDecimal(50)

    // When 
    val specialPrice = multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity) 
    
    // Then 
    specialPrice === 130
  }

  it should "return a total cost for a quantity that is more than the offer " in new TestTransactionData {

    // Given
    val offer     = "2 for 45"
    val quantity  = 3
    val unitPrice = BigDecimal(30)

    // When
    val specialPrice = multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity) 
    
    // Then 
    specialPrice === 75
  }

  it should "produce ParsingError when offer is not in acceptable format" in new TestTransactionData {

    // Given
    val offer    = "2 items for 45"
    val quantity = 3
    val unitPrice = BigDecimal(30)

    // When we use the above format that isn't in expected format
    
    // Then 
    the [Exception] thrownBy {        
      multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity) 
    } should have message ("Cannot read offer. Read documentation for correct format")
  }
}