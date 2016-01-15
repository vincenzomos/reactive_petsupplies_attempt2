package nl.sogeti.reactivepetsupplies

import nl.sogeti.reactivepetsupplies.dao.UserDao
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol._
import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserManager extends UserDao {

  def createUser(userEntity: UserEntity) = save(userEntity)
    .map(_ => UserCreated(userEntity.id.stringify))

  def deleteUserEntity(id: String) = deleteById(id)

  def findAllCustomers = findAllForRole("customer") map extractCustomers

  def getCustomer(id: String) = findById(id).map(extractCustomer)

  def getUserByUsername(username: String) = findByUsername(username).map(extractCustomer)

 private  def extractCustomer(maybeCustomer: Option[UserEntity]) = maybeCustomer match {
    case Some(customerEntity) => toUser(customerEntity)
    case _ => UserNotFound
  }

  private def extractCustomers(customers: List[UserEntity]) = customers match {
    case Nil => UserNotFound
    case l:List[UserEntity] => Users(l.map { o => toUser(o)})
  }

}
