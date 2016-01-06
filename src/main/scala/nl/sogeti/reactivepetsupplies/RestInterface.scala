package nl.sogeti.reactivepetsupplies

import akka.actor._
import akka.pattern.pipe
import akka.util.Timeout
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

class RestInterface extends HttpServiceActor
  with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging { actor: Actor =>
  
  import model.api.UserProtocol._

  implicit val timeout = Timeout(10 seconds)
  
  val userManager = new UserManager

  def routes: Route =
    path("user" / Segment) {
      userId =>
        get { requestContext =>
          val responder = createResponder(requestContext)
          userId match {
            case id => userManager.getCustomer(Option(id)).pipeTo(responder)
          }
        }
    }~
        post {
          entity(as[User]) { user => requestContext =>
            val responder = createResponder(requestContext)
            userManager.createUser(user).pipeTo(responder)
          }
        } ~
      path("users") {
        get { requestContext =>
          val responder = createResponder(requestContext)
          userManager.findAllCustomers
          }
        }~
      path(Segment) { id =>
        delete { requestContext =>
          val responder = createResponder(requestContext)
          userManager.deleteUserEntity(id).pipeTo(responder)
        }
      }

//    pathPrefix("questions") {
//      pathEnd {
//        get { requestContext =>
//          val responder = createResponder(requestContext)
//          questionManager.getQuestion().pipeTo(responder)
//        }
//      } ~
//      path(Segment) { id =>
//        get { requestContext =>
//          val responder = createResponder(requestContext)
//          questionManager.getQuestion(Some(id)).pipeTo(responder)
//        } ~
//        put {
//          entity(as[Answer]) { answer => requestContext =>
//            val responder = createResponder(requestContext)
//            questionManager.answerQuestion(id, answer).pipeTo(responder)
//          }
//        }
//      }
//    }
  
  private def createResponder(requestContext: RequestContext) =
    context.actorOf(Props(new Responder(requestContext)))
}

class Responder(requestContext:RequestContext) extends Actor with ActorLogging {
  import model.api.UserProtocol._

  def receive = {

    case UserCreated(id) =>
      requestContext.complete(StatusCodes.Created, id)
      killYourself

    case UserDeleted =>
      requestContext.complete(StatusCodes.OK)
      killYourself

    case customer: User =>
      requestContext.complete(StatusCodes.OK, customer)
      killYourself

    case UserNotFound =>
      requestContext.complete(StatusCodes.NotFound)
      killYourself

    case unexpected =>
      println("Unexpected result !!!!!! :" + unexpected)
      requestContext.complete(StatusCodes.NotFound)
      killYourself
  }

  private def killYourself = self ! PoisonPill
  
}
