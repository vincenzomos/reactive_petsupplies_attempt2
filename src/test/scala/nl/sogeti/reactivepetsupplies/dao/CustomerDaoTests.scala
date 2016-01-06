package nl.sogeti.reactivepetsupplies.dao

import nl.sogeti.reactivepetsupplies.model.api.CustomerProtocol
import nl.sogeti.reactivepetsupplies.model.api.CustomerProtocol.Customer
import nl.sogeti.reactivepetsupplies.model.persistence.CustomerEntity
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

import spray.json._

import scala.concurrent.{Await, Future}

/**
  * Created by mosvince on 20-12-2015.
  */
class CustomerDaoTests extends FunSuite with BeforeAndAfter with MockitoSugar with ScalaFutures {


  var service: CustomerDao = mock[CustomerDao]
  var dummyCustomer = """{
        "city": "Antwerpen",
        "role": "customer",
        "streetAddress": "Grote Markt 10",
        "firstname": "Kees",
        "surname": "Kooten",
        "postalCode": "12344"}""".parseJson.convertTo[Customer]


  test("test findById") {
    when(service.findById("123456")).thenReturn(Future {
      Option(CustomerEntity.toCustomerEntity(dummyCustomer))
    })
    //      when(service.login("joehacker", "secret")).thenReturn(None)


    // (3) access the service
    val keesKootenFuture = service.findById("123456")

    whenReady(keesKootenFuture) { result =>
      // (4) verify the results
      println("result is " + result)
      assert(CustomerProtocol.toCustomer(result.get).equals(dummyCustomer))
    }

  }
}
