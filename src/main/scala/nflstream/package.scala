import nflstream.models.Update

package object nflstream {

  type UpdateE = Either[String, (String, Update)]

}
