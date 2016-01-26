package nl.sogeti.reactivepetsupplies

import nl.sogeti.reactivepetsupplies.dao.UserDao
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol._
import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserManager extends UserDao {

  def createUser(userEntity: UserEntity) = save(userEntity)
    .map(_ => UserCreated(userEntity.id.stringify))

  def createUpdatedUser(userEntity: UserEntity) = save(userEntity)
    .map(_ => UserUpdated(userEntity.id.stringify))

  def deleteUserEntity(id: String) = deleteById(id)

  def findAllCustomers = findAllForRole("customer") map extractCustomers

  def getUser(id: String) = findById(id).map(extractCustomer)

  def getUserByUsername(username: String) = findByUsername(username).map(extractCustomer)

  /**
    * Updates a User
    * @param username
    * @param update
    * @return
    */
  def updateUser(username: String, update: UserUpdate )  = {

    def updateEntity(user: UserEntity): User = {
      val role = update.role.getOrElse(user.role)
      val username = update.role.getOrElse(user.username)
      val firstname = update.role.getOrElse(user.firstname)
      val surname = update.role.getOrElse(user.surname)
      val streetAddress = update.role.getOrElse(user.streetAddress)
      val city = update.role.getOrElse(user.city)
      val postalCode = update.role.getOrElse(user.postalCode)
      val emailAddress = update.role.getOrElse(user.emailAddress)
      User(role, username, firstname, surname, streetAddress, city, postalCode, emailAddress)
    }

    findByUsername(username).flatMap { maybeUserEntity =>
      maybeUserEntity match {
        case None => Future {
          None
        } // No question found, nothing to update
        case Some(userEntity: UserEntity) =>
          val updatedUser = updateEntity(userEntity)
          deleteUserEntity(userEntity.id.stringify).flatMap { _ =>
            createUpdatedUser(updatedUser)
          }
      }
    }
  }

 private  def extractCustomer(maybeCustomer: Option[UserEntity]) = maybeCustomer match {
    case Some(customerEntity) => toUser(customerEntity)
    case _ => UserNotFound
  }

  private def extractCustomers(customers: List[UserEntity]) = customers match {
    case Nil => UserNotFound
    case l:List[UserEntity] => Users(l.map { o => toUser(o)})
  }

}
