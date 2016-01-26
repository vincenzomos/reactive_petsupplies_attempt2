package nl.sogeti.reactivepetsupplies.model.api

import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity

object UserProtocol {

  import spray.json._

  object Role extends Enumeration {
    type Role = Value
    val CUSTOMER, ADMIN = Value
  }

  case class User(role: String, username: String, firstname : String, surname: String, streetAddress:String, city : String, postalCode: String, emailAddress: String)

  case class Users(orders: List[User])

  case class UserUpdate(role: Option[String], username: Option[String], firstname : Option[String], surname: Option[String], streetAddress:Option[String], city : Option[String], postalCode: Option[String], emailAddress: Option[String])

  /* messages */

  case class UserCreated(id: String)

  case class UserUpdated(id: String)

  case object UserDeleted

  case object UserNotFound

  /* json (un)marshalling */

  object User extends DefaultJsonProtocol {
    implicit val format = jsonFormat8(User.apply)
  }

  object Users extends DefaultJsonProtocol {
    implicit val format = jsonFormat1(Users.apply)
  }

  object UserUpdate extends DefaultJsonProtocol {
    implicit val format = jsonFormat8(UserUpdate.apply)
  }

  /* implicit conversions */

  implicit def toUser(userEntity: UserEntity): User = User(userEntity.role, userEntity.username, userEntity.firstname, userEntity.surname, userEntity.streetAddress, userEntity.city, userEntity.postalCode, userEntity.emailAddress)
//  implicit def toUserUpdate(userEntity: UserEntity): UserUpdate = UserUpdate(Some(userEntity.role), Some(userEntity.username), userEntity.firstname, userEntity.surname, userEntity.streetAddress, userEntity.city, userEntity.postalCode, userEntity.emailAddress)


}
