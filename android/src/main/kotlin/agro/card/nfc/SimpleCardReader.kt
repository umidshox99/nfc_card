package agro.card.nfc

import agro.card.nfc.model.EmvCard
import agro.card.nfc.utils.EmvCardReader
import android.nfc.Tag
import android.nfc.tech.IsoDep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

object SimpleCardReader {
  private const val ISO_DEP_TAG = "android.nfc.tech.IsoDep"
  private const val NFC_A_TAG = "android.nfc.tech.NfcA"
  private const val NFC_B_TAG = "android.nfc.tech.NfcB"
  private var isoDep: IsoDep? = null

  interface SimpleCardReaderCallback {
    fun cardIsReadyToRead(card: EmvCard)
    fun cardMovedTooFastOrLockedNfc()
    fun errorReadingOrUnsupportedCard()
  }

  fun readCard(tag: Tag?, callback: SimpleCardReaderCallback) {
    if (tag == null) {
      callback.errorReadingOrUnsupportedCard()
      return
    }

    val tagString = tag.toString()

    if (!tagString.contains(ISO_DEP_TAG) || (!tagString.contains(NFC_A_TAG) && !tagString.contains(
        NFC_B_TAG
      ))
    ) {
      callback.errorReadingOrUnsupportedCard()
      return
    }

    isoDep = IsoDep.get(tag)
    if (isoDep == null) {
      callback.cardMovedTooFastOrLockedNfc()
      return
    }

    //TODO this scope MUST be changed
    GlobalScope.launch(Dispatchers.Main) {
      readTheCard(isoDep!!, callback)
    }
  }

  private fun readTheCard(isoDep: IsoDep, simpleCardReaderCallback: SimpleCardReaderCallback) {
    try {
      isoDep.connect()
      val parser = EmvCardReader(isoDep)
      val card = parser.readEmvCard()

      if (card.cardNumber.isNotEmpty()) {
        simpleCardReaderCallback.cardIsReadyToRead(card)
      } else {
        simpleCardReaderCallback.cardMovedTooFastOrLockedNfc()
      }

    } catch (e: IOException) {
      simpleCardReaderCallback.errorReadingOrUnsupportedCard()
    } finally {
      try {
        isoDep.close()
      } catch (ignored: IOException) {
      }
    }
  }


}
