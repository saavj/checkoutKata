package com.saacole.checkoutkata

/** Error Handling 
  */
case class SKUNotFound(message: String)  extends Exception(message)
case class ParsingError(message: String) extends Exception(message)
case class InvalidPrice(message: String) extends Exception(message)

/** Pricing 
	* 
	* Prices always come with a unit price - modelled as a BigDecimal for accuracy.
	* Prices may come with a special price - modelled as a String. 
  */
case class Prices(unitPrice: BigDecimal, specialPrice: Option[String] = None) {
  require(unitPrice >= 0, throw InvalidPrice(s"Invalid price $unitPrice - Cannot be a negative value"))
}

/** Offers 
	* 
	* Models how offers should look after parsed.
	* We want to extract the important information out of the offer String
	* This will be the eligible quantity for the given offer - modelled as an Int
	* and a price for that quantity of items - modelled as a BigDecimal for accuracy.
  */
case class Offer(eligibleQuantity: Int, price: BigDecimal)
