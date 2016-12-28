package bot.line.model.event

trait Source {
  val `type`: String
}

case class UserSource(userId: String) extends Source {
  override val `type`: String = "user"
}

case class GroupSource(userId: String) extends Source {
  override val `type`: String = "group"
}

case class RoomSource(userId: String) extends Source {
  override val `type`: String = "room"
}
