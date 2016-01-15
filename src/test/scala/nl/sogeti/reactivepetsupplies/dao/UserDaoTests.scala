package nl.sogeti.reactivepetsupplies.dao

import nl.sogeti.reactivepetsupplies.model.api.UserProtocol
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol.User
import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

/**
  * Created by mosvince on 20-12-2015.
  */
class UserDaoTests extends FunSuite with BeforeAndAfter with MockitoSugar with ScalaFutures {


  var service: UserDao = mock[UserDao]
  var dummyCustomer = """{
        "city": "Antwerpen",
        "role": "customer",
        "username": "testuser02",
        "streetAddress": "Grote Markt 10",
        "firstname": "Kees",
        "surname": "Kooten",
        "postalCode": "12344",
        "emailAddress": "tester@reactivecountry.nl"}""".parseJson.convertTo[User]

  val userEntityObject = UserEntity.toUserEntity(dummyCustomer)
  val userObject = UserProtocol.toUser(userEntityObject)

//  val userJsonList = for ( i <- Source.fromFile("users.json").getLines) yield UserEntity.toUserEntity(_)

  test("test findById") {
    when(service.findById("123456")).thenReturn(Future {
      Option(userEntityObject)
    })
    //      when(service.login("joehacker", "secret")).thenReturn(None)


    // (3) access the service
    val keesKootenFuture = service.findById("123456")

    whenReady(keesKootenFuture) { result =>
      // (4) verify the results. We need to verify the User objects. The entity will allways contain a new generated ID
      println("result is " + result)
      assert(UserProtocol.toUser(result.get).equals(dummyCustomer))
    }
  }

    test("test findAll") {
      when(service.findAllForRole("customer")).thenReturn(Future {
        List(UserEntity.toUserEntity(dummyCustomer))
      })

      // (3) access the service
      val customerList = service.findAllForRole("customer")

      whenReady(customerList) { result =>
        // (4) verify the results
        println("result is " + result)
        println("resultDummy is " + List(userEntityObject))
        assert(result.map(UserProtocol.toUser(_)).equals(List(userObject)))
      }
    }

//  test("test find  by Username") {
//    when(service.findByUsername("testUser02")).thenReturn(Future {
//      Option(UserEntity.toUserEntity(dummyCustomer))
//    })
//
//    // (3) access the service
//    val user = service.findByUsername("testUser02")
//
//    whenReady(user) { result =>
//      // (4) verify the results
//      println("result is " + result)
//      assert(result.map(UserProtocol.toUser(_)).equals(userObject))
//    }
//  }
}
