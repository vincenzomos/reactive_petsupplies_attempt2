package nl.sogeti.reactivepetsupplies.dao

import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * Created by mosvince on 20-12-2015.
  */
class CustomerDaoTests extends FunSuite with BeforeAndAfter with MockitoSugar {
  import org.scalatest.FunSuite
  import org.scalatest.BeforeAndAfter
  import org.scalatest.mock.MockitoSugar
  import org.mockito.Mockito._

    test ("test customer Dao") {

      // (1) init
      val service = mock[CustomerDao]
      val dummyCustomrer = """{
        "city": "Antwerpen",
        "role": "customer",
        "streetAddress": "Grote Markt 10",
        "firstname": "Admin",
        "surname": "Administrator",
        "postalCode": "12344"""
      }

      // (2) setup: when someone logs in as "johndoe", the service should work;
      //            when they try to log in as "joehacker", it should fail.
      when(service.findById("johndoe", "secret")).thenReturn(Some(User("johndoe")))
      when(service.login("joehacker", "secret")).thenReturn(None)

      // (3) access the service
      val johndoe = service.login("johndoe", "secret")
      val joehacker = service.login("joehacker", "secret")

      // (4) verify the results
      assert(johndoe.get == User("johndoe"))
      assert(joehacker == None)

    }

  }
}
