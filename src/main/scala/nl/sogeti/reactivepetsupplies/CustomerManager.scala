package nl.sogeti.reactivepetsupplies

import nl.sogeti.reactivepetsupplies.dao.CustomerDao
import nl.sogeti.reactivepetsupplies.model.api.CustomerProtocol._
import nl.sogeti.reactivepetsupplies.model.persistence.CustomerEntity

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomerManager extends CustomerDao {

  def createCustomer(customerEntity: CustomerEntity) = save(customerEntity)

  def deleteCustomerEntity(id: String) = deleteById(id)

  def getCustomer(maybeId: Option[String] = None) = {

    def extractCustomer(maybeCustomer: Option[CustomerEntity]) = maybeCustomer match {
      case Some(customerEntity) => toCustomer(customerEntity)
      case _ => CustomerNotFound
    }
    tryGetCustomer(maybeId).map(extractCustomer)
  }

  private def tryGetCustomer(maybeId: Option[String]): Future[Option[CustomerEntity]] = maybeId match {
    case Some(id) => { println("found customer")
      findById(id) }
    case _ => findOne
  }
}
