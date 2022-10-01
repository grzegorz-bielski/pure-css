package pureframes.css

trait FileSystemRenderer:
  def toFiles(directory: String, renderers: StyleSheetRenderer*): Unit
