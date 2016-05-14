package nl.sogeti.reactivepetsupplies

import spray.routing.RequestContext
import spray.routing.authentication.{ContextAuthenticator, BasicAuth, UserPass}
import spray.routing.directives.AuthMagnet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by mosvince on 17-1-2016.
  */
package object security {

  val userManager = new UserManager

  import model.api.UserProtocol._

  trait Authenticator {
    def basicUserAuthenticator(implicit ec: ExecutionContext): AuthMagnet[AuthInfo] = {

      def authenticator(userPass: Option[UserPass]): Future[Option[AuthInfo]] = Future { validateUser(userPass) }

      BasicAuth(authenticator _, realm = "Private API")
    }

    def validateUser(userPass: Option[UserPass]): Option[AuthInfo] = {
      for {
        p: UserPass <- userPass
        user: User <- userManager.getUserByUsername(p.user)
        if user.passwordMatches(p.pass)
      } yield new AuthInfo(user)
    }
  }

  case class AuthInfo(val user: User) {
    def hasPermission(permission: String) = {
      // Code to verify whether user has the given permission      }
      permission.equalsIgnoreCase(user.role)
    }
  }

  def hasPrivilege(user: User, privilege: String): Boolean =
    user.role.contains(privilege)

//  val authenticator: ContextAuthenticator[Unit] = { ctx =>
//    Future {
//      val maybeCredentials = extractCredentials(ctx)
//      maybeCredentials.fold[authentication.Authentication[Unit]](
//        Left(AuthenticationFailedRejection(CredentialsMissing, List()))
//      )( credentials =>
//        credentials match {
//          case AppCredentials("my-client-id", "my-client-super-secret") => Right()
//          case _ => Left(AuthenticationFailedRejection(CredentialsRejected, List()))
//        }
//      )
//    }
//  }
//
//  case class Credentials(id: String, secret: String)
//
//  private def extractCredentials(ctx: RequestContext): Option[Credentials] = {
//    val queryParams = ctx.request.uri.query.toMap
//    for {
//      id <- queryParams.get("client_id")
//      secret <- queryParams.get("client_secret")
//    } yield Credentials(id, secret)
//  }

}
