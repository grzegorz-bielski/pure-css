package pureframes.css

trait FileSystemRenderer:
  def toFiles(directory: String, renderers: StyleSheetRenderer*): Unit

  // TODO: use fs2 file API ?
  protected def collectStyles(
      directory: String,
      renderers: Seq[StyleSheetRenderer]
  ): Map[String, String] =
    renderers
      .groupBy(_.name)
      .map { (name, renderers) =>
        val path = s"$directory/$name.css"
        val collected = renderers.foldLeft("")(_ + _.render.filterNot(_.isWhitespace))

        path -> collected
      }
