package bot.line.client

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class SignatureVerifier {

  def isValid(channelSecret: String, bodyString: String, signature: String): Boolean = {
    val key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(key)
    val source = mac.doFinal(bodyString.getBytes(StandardCharsets.UTF_8))
    Base64.getEncoder.encodeToString(source) == signature
  }

}
