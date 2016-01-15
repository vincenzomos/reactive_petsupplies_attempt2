package nl.sogeti.reactivepetsupplies

import akka.actor.{Actor, ActorRefFactory}
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol.User
import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import spray.http.StatusCodes._
import spray.json._
import spray.testkit.ScalatestRouteTest
import spray.routing.RejectionHandler.Default

import scala.concurrent.Future

/**
  * Created by mosvince on 9-1-2016.
  */
@RunWith(classOf[JUnitRunner])
class RestApiTest extends FlatSpec with ScalatestRouteTest with ShouldMatchers with Matchers with MockitoSugar with RestApi with Actor {

  override def receive: Receive = runRoute(routes)

  override val userManager = mock[UserManager]

  override implicit def actorRefFactory: ActorRefFactory = system

  //  var service: UserDao = mock[UserDao]
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

  "The user Service" should "return a list of users when using url /user" in {
    when(userManager.findAllForRole("customer")).thenReturn(Future {
      List(userEntityObject)
    })
    when(userManager.findByUsername("testuser02")).thenReturn(Future {
      Option(userEntityObject)
    })
    Get("/user?username=testuser02") ~> routes ~> check {
      status should equal(OK)
      entity.toString should include("testuser02")
    }
  }
}
