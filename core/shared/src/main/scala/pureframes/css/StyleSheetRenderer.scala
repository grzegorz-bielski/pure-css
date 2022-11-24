package pureframes.css

trait StyleSheetRenderer:
  def render: String
  def name: String