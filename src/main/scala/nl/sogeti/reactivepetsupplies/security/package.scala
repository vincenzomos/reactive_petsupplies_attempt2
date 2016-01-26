package nl.sogeti.reactivepetsupplies

import org.mindrot.jbcrypt.BCrypt
/**
  * Created by mosvince on 17-1-2016.
  */
package object security {

  case class ApiUser(login: String,
                     hashedPassword: Option[String] = None) {
    def withPassword(password: String) = copy(hashedPassword = Some(BCrypt.hashpw(password, BCrypt.gensalt())))

    def passwordMatches(password: String): Boolean = hashedPassword.exists(hp => BCrypt.checkpw(password, hp))
  }


  class AuthInfo(val user: ApiUser) {
    def hasPermission(permission: String) = {
      // Code to verify whether user has the given permission      }
    }
  }

}
