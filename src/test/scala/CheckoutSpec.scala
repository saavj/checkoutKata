package com.saacole.checkoutkata

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class CheckoutSpec extends FlatSpec {

  trait TestTransactionData {

    // Given
    val unknownSku           = "unknown"

    val skus                 = List("B", "C", "D")
    val prices               = List(BigDecimal(10), BigDecimal(20), BigDecimal(30))
    val priceRules           = (skus zip prices.map(p => Prices(p))).toMap

    val multiRuleTransaction = new Checkout(priceRules)
    val emptyRuleTransaction = new Checkout(Map())
  }

  "Checkout" should "produce an error when negative prices are used" in new TestTransactionData {

    // Given
    val sku         = "A"
    val quantity    = 1

    // When
    val price       = -50

    // Then
    the [Exception] thrownBy {
      new Checkout(Map(sku -> Prices(price)))
    } should have message s"Invalid price $price - Cannot be a negative value"
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
    actualPrice should === (50)
  }

  it should "return correct price for multiple skus (without offers)" in new TestTransactionData {

    // Given C costs 20 (as it is in the multiRuleTransaction we are using)
    // 2 C's should cost 40

    // When
    val actualPrice = multiRuleTransaction.scanItem("C", 2)

    // Then

    actualPrice should === (BigDecimal(40))
  }

  it should "produce SKUNotFound when sku doesn't exist in pricing rules" in new TestTransactionData {

    // When you scan an unknow item 
    
    // Then 
    the [Exception] thrownBy {        
      emptyRuleTransaction.scanItem(unknownSku, 1)
    } should have message s"SKU $unknownSku not found"
  }

  "scanItems" should "return a list of prices" in new TestTransactionData {

    // When
    val actualPrices = multiRuleTransaction.scanItems(skus)
    
    // Then 
    actualPrices should contain theSameElementsAs (prices)
  }

  it should "return an empty list for an empty list (not error)" in new TestTransactionData {

    // When 
    val actualPrices = emptyRuleTransaction.scanItems(List())
    
    // Then   
    actualPrices should === (List())
  }

  it should "produce SKUNotFound when sku doesn't exist in pricing rules" in new TestTransactionData {

    // When one of the SKUs don't exist
    
    // Then 
    the [Exception] thrownBy {        
      emptyRuleTransaction.scanItems(List(unknownSku))
    } should have message s"SKU $unknownSku not found"
  }

  "calcTotal" should "sum a list of prices" in new TestTransactionData {

    // When 
    val actualTotal = multiRuleTransaction.calcTotal(prices) 
    
    // Then 
    actualTotal should === (60)
  }

  it should "return 0 for empty list" in new TestTransactionData {

    // When
    val actualTotal = emptyRuleTransaction.calcTotal(List()) 
    
    // Then 
    actualTotal should === (0)
  }


  "calcSpecialPrice" should "return a total cost for a quantity that matches offer exactly" in new TestTransactionData {

    // Given
    val offer     = "3 for 130"
    val quantity  = 3
    val unitPrice = BigDecimal(50)

    // When 
    val specialPrice = multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity) 
    
    // Then 
    specialPrice should === (130)
  }

  it should "return a total cost for a quantity that is more than the offer " in new TestTransactionData {

    // Given
    val offer     = "2 for 45"
    val quantity  = 3
    val unitPrice = BigDecimal(30)

    // When
    val specialPrice = multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity) 
    
    // Then 
    specialPrice should === (75)
  }

  it should "produce ParsingError when offer is not in acceptable format (extra words)" in new TestTransactionData {

    // Given
    val offer     = "2 items for 45"
    val quantity  = 3
    val unitPrice = BigDecimal(30)

    // When we use the above format that isn't in expected format
    
    // Then 
    the [Exception] thrownBy {        
      multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity) 
    } should have message s"Cannot read offer '$offer'. Read documentation for correct format"
  }

  it should "produce ParsingError when offer is not in acceptable format (quantity is 0)" in new TestTransactionData {

    // Given
    val offer     = "0 for 45"
    val quantity  = 3
    val unitPrice = BigDecimal(30)

    // When we use the above format that isn't in expected format

    // Then
    the [Exception] thrownBy {
      multiRuleTransaction.calcSpecialPrice(unitPrice, offer, quantity)
    } should have message s"Cannot read offer '$offer'. Read documentation for correct format"
  }
}
