package com.saacole.checkoutkata

/** Error Handling 
  */
case class SKUNotFound(sku: String, message: String) extends Exception(message)

class Checkout(priceRules: Map[String, Int]) {

	/** Scanning one item
		*
		*	Calculating price for one item from the current pricing rules. 
		* Throws an error when a sku is passed that doesn't exist. We 
		* cannot calculate a transaction if a product doesn't exist.
	  */
	def scanItem(sku: String): Int = priceRules get sku match {
		case Some(price) => price
		case None				 => throw SKUNotFound(sku, "SKU not found")
	}

	/** Scanning items
		*
		*	Collecting a list of prices from a list of SKUs.
	  */
	def scanItems(skus: Seq[String]): Seq[Int] = skus.map(sku => scanItem(sku))

	/** Scanning items
		*
		*	Sums a list of prices.
	  */
	def calcTotal(prices: Seq[Int]): Int = prices.sum
}