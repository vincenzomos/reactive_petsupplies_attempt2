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
  
  import model.api.CustomerProtocol._

  implicit val timeout = Timeout(10 seconds)
  
  val customerManager = new CustomerManager

  def routes: Route =
    path("customer" / Segment) {
      customerId =>
        get { requestContext =>
          val responder = createResponder(requestContext)
          customerManager.getCustomer(Option(customerId)).pipeTo(responder)

        }
    }~
        post {
          entity(as[Customer]) { customer => requestContext =>
            val responder = createResponder(requestContext)
            customerManager.createCustomer(customer).pipeTo(responder)
          }
        } ~
      path(Segment) { id =>
        delete { requestContext =>
          val responder = createResponder(requestContext)
          customerManager.deleteCustomerEntity(id).pipeTo(responder)
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
  import model.api.CustomerProtocol._

  def receive = {

    case CustomerCreated(id) =>
      requestContext.complete(StatusCodes.Created, id)
      killYourself

    case CustomerDeleted =>
      requestContext.complete(StatusCodes.OK)
      killYourself

    case customer: Customer =>
      requestContext.complete(StatusCodes.OK, customer)
      killYourself

    case CustomerNotFound =>
      requestContext.complete(StatusCodes.NotFound)
      killYourself

    case unexpected =>
      println("Unexpected result !!!!!! :" + unexpected)
      requestContext.complete(StatusCodes.NotFound)
      killYourself
  }

  private def killYourself = self ! PoisonPill
  
}
