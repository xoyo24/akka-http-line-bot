package bot.line.model.event

trait Source {
  val `type`: String
}

case class UserSource(id: String) extends Source {
  override val `type`: String = "user"
}

case class GroupSource(id: String) extends Source {
  override val `type`: String = "group"
}

case class RoomSource(id: String) extends Source {
  override val `type`: String = "room"
}
