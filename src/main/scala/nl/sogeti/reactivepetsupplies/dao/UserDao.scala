package nl.sogeti.reactivepetsupplies.dao

import com.typesafe.scalalogging.Logger
import nl.sogeti.reactivepetsupplies.model.api.UserProtocol
import nl.sogeti.reactivepetsupplies.model.persistence.UserEntity
import org.slf4j.LoggerFactory
import reactivemongo.api.QueryOpts
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.Count

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

trait UserDao extends MongoDao {

  val logger = Logger(LoggerFactory.getLogger("UserDao"))


  import UserEntity._
  import UserProtocol._

  val USER_COLLECTION = "customers"

  val collection = db[BSONCollection](USER_COLLECTION)

  def save(userEntity: UserEntity) = {
    val selector = BSONDocument("_id:" -> userEntity.id)
    collection.update(selector, userEntity, upsert = true)
  }

  def findAll = collection.find(emptyQuery).cursor[UserEntity].collect[List]()

  def findById(id: String) = {
  logger.info("Reached the findByID with {}", id)
  collection.find(queryById(id)).one[UserEntity]
}

  def findOne = {
    val futureCount = db.command(Count(collection.name))
    futureCount.flatMap { count =>
      val skip = Random.nextInt(count)
      collection.find(emptyQuery).options(QueryOpts(skipN = skip)).one[UserEntity]
    }
  }
  
  def deleteById(id: String) = collection.remove(queryById(id)).map(_ => UserDeleted)

  private def queryById(id: String)  = {
    val doc = BSONDocument("_id" -> BSONObjectID(id))
    logger.info("found document {}", doc)
    doc
  }

  private def emptyQuery = BSONDocument()
}
