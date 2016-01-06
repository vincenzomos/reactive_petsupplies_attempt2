package nl.sogeti.reactivepetsupplies

import nl.sogeti.reactivepetsupplies.dao.UserDao
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol._
import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserManager extends UserDao {

  def createUser(userEntity: UserEntity) = save(userEntity)

  def deleteUserEntity(id: String) = deleteById(id)

  def findAllCustomers = findAll map extractCustomers

  def getCustomer(maybeId: Option[String] = None) = tryGetCustomer(maybeId).map(extractCustomer)

 private  def extractCustomer(maybeCustomer: Option[UserEntity]) = maybeCustomer match {
    case Some(customerEntity) => toUser(customerEntity)
    case _ => UserNotFound
  }

  private def extractCustomers(customers: List[UserEntity]) = customers match {
    case Nil => UserNotFound
    case l:List[UserEntity] => Users(l.map { o => toUser(o)})
  }

  private def tryGetCustomer(maybeId: Option[String]): Future[Option[UserEntity]] = maybeId match {
    case Some(id) => { println("found customer")
      findById(id) }
    case _ => findOne
  }
}
