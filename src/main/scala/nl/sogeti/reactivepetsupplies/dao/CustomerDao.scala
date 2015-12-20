package nl.sogeti.reactivepetsupplies.dao

import com.typesafe.scalalogging.Logger
import nl.sogeti.reactivepetsupplies.model.api.CustomerProtocol
import nl.sogeti.reactivepetsupplies.model.persistence.CustomerEntity
import org.slf4j.LoggerFactory
import reactivemongo.api.QueryOpts
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.Count

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

trait CustomerDao extends MongoDao {

  val logger = Logger(LoggerFactory.getLogger("CustomerDao"))


  import CustomerEntity._
  import CustomerProtocol._

  val CUSTOMER_COLLECTION = "customers"

  val collection = db[BSONCollection](CUSTOMER_COLLECTION)

  def save(customerEntity: CustomerEntity) = collection.save(customerEntity)
    .map(_ => CustomerCreated(customerEntity.id.stringify))

  def findById(id: String) = {
  logger.info("Reached the findByID with {}", id)
  collection.find(queryById(id)).one[CustomerEntity]
}

  def findOne = {
    val futureCount = db.command(Count(collection.name))
    futureCount.flatMap { count =>
      val skip = Random.nextInt(count)
      collection.find(emptyQuery).options(QueryOpts(skipN = skip)).one[CustomerEntity]
    }
  }
  
  def deleteById(id: String) = collection.remove(queryById(id)).map(_ => CustomerDeleted)

  private def queryById(id: String)  = {
    val doc = BSONDocument("_id" -> BSONObjectID(id))
    logger.info("found document {}", doc)
    doc
  }

  private def emptyQuery = BSONDocument()
}
