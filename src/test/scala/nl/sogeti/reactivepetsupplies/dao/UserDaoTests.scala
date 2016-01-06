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

/**
  * Created by mosvince on 20-12-2015.
  */
class UserDaoTests extends FunSuite with BeforeAndAfter with MockitoSugar with ScalaFutures {


  var service: UserDao = mock[UserDao]
  var dummyCustomer = """{
        "city": "Antwerpen",
        "role": "customer",
        "streetAddress": "Grote Markt 10",
        "firstname": "Kees",
        "surname": "Kooten",
        "postalCode": "12344"}""".parseJson.convertTo[User]


  test("test findById") {
    when(service.findById("123456")).thenReturn(Future {
      Option(UserEntity.toUserEntity(dummyCustomer))
    })
    //      when(service.login("joehacker", "secret")).thenReturn(None)


    // (3) access the service
    val keesKootenFuture = service.findById("123456")

    whenReady(keesKootenFuture) { result =>
      // (4) verify the results
      println("result is " + result)
      assert(UserProtocol.toUser(result.get).equals(dummyCustomer))
    }

  }
}
