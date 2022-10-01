/** based on fs2 facade:
  * https://github.com/typelevel/fs2/blob/main/io/js/src/main/scala/fs2/io/internal/facade/fs.scala
  */
package pureframes.css.internals.fs

import scala.annotation.nowarn
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("fs", "constants")
def constants: FsConstants = js.native

@js.native
@JSImport("fs", "promises")
def promises: FsPromises = js.native

@js.native
trait FsConstants extends js.Object:
  val R_OK: Double = js.native

@js.native
trait FsPromises extends js.Object:
  def access(path: String, mode: Double = js.native): js.Promise[Unit] =
    js.native

  def writeFile(file: String, data: String): js.Promise[Unit] =
    js.native
