package nl.sogeti.reactivepetsupplies

import akka.actor._
import akka.pattern.pipe
import akka.util.Timeout
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol.LoginSuccessfull
import nl.sogeti.reactivepetsupplies.security.{AuthInfo, Authenticator}
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.routing.authentication.UserPass

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

class RestInterface extends HttpServiceActor
with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging with Authenticator {
  actor: Actor =>

  import model.api.UserProtocol._

  implicit val timeout = Timeout(10 seconds)

  val userManager = new UserManager

  ///////////////////////////////
  // To DO !!!!!!!!!!!!!!!! 25-01- 2015 Fix the routing !!!!!!!!!!!!!!!!!!!!!!!!!!!
  /////////////////////////////////////
  def routes: Route =
    pathPrefix("user") {
      pathEnd {
        post {
          log.info("entered the Post for user ")
          entity(as[User]) { user => requestContext =>
            val responder = createResponder(requestContext)
            userManager.createUser(user).pipeTo(responder)
          }
        }
        get {
          authenticate(basicUserAuthenticator) { authInfo =>  Route { requestContext =>
            val responder = createResponder(requestContext)
            userManager.findAllCustomers.pipeTo(responder)
          }
          }
        }
      } ~
        path(Segment) { username =>
          get { requestContext =>
            val responder = createResponder(requestContext)
            userManager.getUserByUsername(username).pipeTo(responder)
          } ~
            put {
              entity(as[UserUpdate]) { update => requestContext =>
                val responder = createResponder(requestContext)
                userManager.updateUser(username, update).pipeTo(responder)
              }
            } ~
            delete { requestContext =>
              val responder = createResponder(requestContext)
              userManager.deleteUserEntity(username).pipeTo(responder)
            }
        }
    }


  private def createResponder(requestContext: RequestContext) =
    context.actorOf(Props(new Responder(requestContext)))
}

class Responder(requestContext: RequestContext) extends Actor with ActorLogging {

  import model.api.UserProtocol._

  def receive = {

      case UserCreated(id) =>
      requestContext.complete(StatusCodes.Created, id)
      killYourself

    case UserUpdated(id) =>
      requestContext.complete(StatusCodes.Created, id)
      killYourself

    case UserDeleted =>
      requestContext.complete(StatusCodes.NoContent)
      killYourself

    case customer: User =>
      requestContext.complete(StatusCodes.OK, customer)
      killYourself

    case UserNotFound =>
      requestContext.complete(StatusCodes.NotFound)
      killYourself

    case LoginSuccessfull =>
      requestContext.complete(StatusCodes.OK)
      killYourself


    case unexpected =>
      println("Unexpected result !!!!!! :" + unexpected)
      requestContext.complete(StatusCodes.NotFound)
      killYourself
  }

  private def killYourself = self ! PoisonPill

}
