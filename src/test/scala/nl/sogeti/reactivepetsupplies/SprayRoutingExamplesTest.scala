package nl.sogeti.reactivepetsupplies

import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner
import org.specs2.mutable.Specification
import spray.routing._
import spray.testkit.{ScalatestRouteTest, Specs2RouteTest}

/**
  * Created by mosvince on 26-1-2016.
  */

trait MyRoute extends HttpService {

  def route: Route =
    pathPrefix("ball") {
      pathEnd {
        complete("/ball")
      } ~
        path(IntNumber) { int =>
          complete(if (int % 2 == 0) "even ball" else "odd ball")
        }
    }
}

//@RunWith(classOf[JUnitRunner])
class SprayRoutingExamplesTest extends FlatSpec with Matchers  with ScalatestRouteTest with  MyRoute {


    def actorRefFactory = system
    "MyService" should "it should bla die bla" in {
    Get("/") ~> route ~> check {
      handled === false
    }


    Get("/ball") ~> route ~> check {
      responseAs[String] === "/ball"
    }

    Get("/ball/1337") ~> route ~> check {
      responseAs[String] === "odd ball"
    }
  }
}
