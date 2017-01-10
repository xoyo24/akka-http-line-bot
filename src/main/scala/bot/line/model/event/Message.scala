package bot.line.model.event

sealed trait Message {
  val `type`: String
}

case class TextMessage(id: String, text: String) extends Message {
  override val `type`: String = "text"
}

case class ImageMessage(id: String) extends Message {
  override val `type`: String = "image"
}

case class VideoMessage(id: String) extends Message {
  override val `type`: String = "video"
}

case class AudioMessage(id: String) extends Message {
  override val `type`: String = "audio"
}

case class LocationMessage(
                            id: String,
                            title: String,
                            address: String,
                            latitude: BigDecimal,
                            longitude: BigDecimal
                          ) extends Message {
  override val `type`: String = "location"
}

case class StickerMessage(
                           id: String,
                           packageId: String,
                           stickerId: String
                         ) extends Message {
  override val `type`: String = "sticker"
}
