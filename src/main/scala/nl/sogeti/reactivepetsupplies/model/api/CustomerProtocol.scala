package nl.sogeti.reactivepetsupplies.model.api

import nl.sogeti.reactivepetsupplies.model.persistence.CustomerEntity

object CustomerProtocol {

  import spray.json._

  case class Customer(role: String, firstname : String, surname: String, streetAddress:String, city : String, postalCode: String)

  case class Customers(orders: List[Customer])

  /* messages */

  case class CustomerCreated(id: String)

  case object CustomerDeleted

  case object CustomerNotFound

  /* json (un)marshalling */

  object Customer extends DefaultJsonProtocol {
    implicit val format = jsonFormat6(Customer.apply)
  }

  object Customers extends DefaultJsonProtocol {
    implicit val format = jsonFormat1(Customers.apply)
  }

  /* implicit conversions */

  implicit def toCustomer(customerEntity: CustomerEntity): Customer = Customer(customerEntity.role, customerEntity.firstname, customerEntity.surname, customerEntity.streetAddress, customerEntity.city, customerEntity.postalCode)


}
