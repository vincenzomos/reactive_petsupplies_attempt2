package nl.sogeti.reactivepetsupplies.model.persistence

import nl.sogeti.reactivepetsupplies.model.api.CustomerProtocol.{Customer}
import org.mindrot.jbcrypt.BCrypt
import reactivemongo.bson.Macros.Annotations.Key
import reactivemongo.bson._

/**
  * Created by mosvince on 6-12-2015.
  */
case class CustomerEntity(@Key(key = "_id") id: BSONObjectID = BSONObjectID.generate,
                          role: String,
                          firstname : String,
                          surname: String,
                          streetAddress: String,
                          city: String,
                          postalCode: String)


object CustomerEntity {

  implicit def toCustomerEntity(customer: Customer) = CustomerEntity(role = customer.role, firstname = customer.firstname, surname = customer.surname, streetAddress =customer.streetAddress, city = customer.city, postalCode = customer.postalCode)

//  implicit val customerHandler : BSONHandler[BSONDocument, CustomerEntity] = Macros.handler[CustomerEntity]
//  implicit val customerAddressHandler : BSONHandler[BSONDocument, CustomerAddress] = Macros.handler[CustomerAddress]

  implicit object CustomerEntityBSONReader extends BSONDocumentReader[CustomerEntity]   {

    def read(doc: BSONDocument): CustomerEntity = Macros.handler[CustomerEntity].read(doc)


  }
  implicit object CustomerEntityBSONWriter extends BSONDocumentWriter[CustomerEntity] {
    def write(customerEntity: CustomerEntity): BSONDocument = Macros.handler[CustomerEntity].write(customerEntity)
  }

  /*  The macros make the following old fashioned translators redundant. However it will off cause provide more control ove reach field */

  //  implicit object CustomerEntityBSONReader extends BSONDocumentReader[CustomerEntity] {

//    def read(doc: BSONDocument): CustomerEntity =
//      CustomerEntity(
    //        id = doc.getAs[BSONObjectID]("_id").get,
    //        username = doc.getAs[String]("username").get,
    //        password = doc.getAs[String]("password").get,
    //        role = doc.getAs[String]("role").get,
    //        firstname = doc.getAs[String]("firstname").get,
    //        surname = doc.getAs[String]("surname").get,
    //        address = Option(doc.getAs[String]("address").get)
    //      )
//  }

//  implicit object CustomerAddressBSONWriter extends BSONWriter[CustomerAddress] {
//    def toBSON(customerAddress: CustomerAddress) = {
//      BSONDocument(
//        "streetAddress" -> BSONString(customerAddress.streetAddress),
//        "city" -> BSONString(customerAddress.city)
//      )
//    }
//  }
//  implicit object CustomerEntityBSONWriter extends BSONDocumentWriter[CustomerEntity] {
//    def write(customerEntity: CustomerEntity): BSONDocument =
//      BSONDocument(
//        "_id" -> customerEntity.id,
//        "username" -> customerEntity.username,
//        "password" -> customerEntity.password,
//        "role" -> customerEntity.role,
//        "firstname" -> customerEntity.firstname,
//        "surname" -> customerEntity.surname,
//        "address" -> customerEntity.address
//      )
//  }
}




//  case class CustomerEntity(id: BSONObjectID = BSONObjectID.generate, username: String, password: String, role: String, firstname : String, surname: String, address:Option[String]) {
//    require(!username.isEmpty, "username.empty")
//    require(!password.isEmpty, "password.empty")
//    def withHashedPassword(): CustomerEntity = this.copy(password = BCrypt.hashpw(password, BCrypt.gensalt()))
//  }
//  case class CustomerEntityUpdate(username: Option[String] = None, password: Option[String] = None) {
//    def merge(customer: CustomerEntity): CustomerEntity = {
//      CustomerEntity(customer.id, username.getOrElse(customer.username), password.map(ps => BCrypt.hashpw(ps, BCrypt.gensalt())).getOrElse(customer.password), customer.role, customer.firstname, customer.surname, customer.address)
//    }
//
//  object CustomerEntity {
//
//    }
//
//    implicit def toQuizEntity(quiz: Customer) = QuizEntity(question = quiz.question, correctAnswer = quiz.correctAnswer)
//  }
