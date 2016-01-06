package nl.sogeti.reactivepetsupplies.model.api

import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity

object UserProtocol {

  import spray.json._

  object Role extends Enumeration {
    type Role = Value
    val CUSTOMER, ADMIN = Value
  }

  case class User(role: String, firstname : String, surname: String, streetAddress:String, city : String, postalCode: String)

  case class Users(orders: List[User])

  /* messages */

  case class UserCreated(id: String)

  case object UserDeleted

  case object UserNotFound

  /* json (un)marshalling */

  object User extends DefaultJsonProtocol {
    implicit val format = jsonFormat6(User.apply)
  }

  object Users extends DefaultJsonProtocol {
    implicit val format = jsonFormat1(Users.apply)
  }

  /* implicit conversions */

  implicit def toUser(userEntity: UserEntity): User = User(userEntity.role, userEntity.firstname, userEntity.surname, userEntity.streetAddress, userEntity.city, userEntity.postalCode)


}
