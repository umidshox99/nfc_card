package agro.card.nfc.nfc_card

import agro.card.nfc.SimpleCardReader
import agro.card.nfc.model.EmvCard

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.concurrent.CopyOnWriteArrayList

import androidx.annotation.NonNull

const val PERMISSION_NFC: Int = 1007
private var nfcFlags =
  NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_BARCODE or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V

/** NfcCardPlugin */
class NfcCardPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
  SimpleCardReader.SimpleCardReaderCallback, NfcAdapter.ReaderCallback {
  private lateinit var channel: MethodChannel
  private var nfcAdapter: NfcAdapter? = null
  private var nfcManager: NfcManager? = null
  private var activity: Activity? = null
  private var result: Result? = null
  private var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null

  companion object {
    internal val listeners = CopyOnWriteArrayList<NfcAdapter.ReaderCallback>()
  }

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    this.flutterPluginBinding = flutterPluginBinding;
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "nfc_card")
    channel.setMethodCallHandler(this)
  }


  override fun onMethodCall(call: MethodCall, result: Result) {
    this.result = result;
    when (call.method) {
      "checkNFCAvailability" -> {
        when {
          this.nfcAdapter == null -> result.success("not_supported")
          this.nfcAdapter!!.isEnabled -> result.success("available")
          else -> result.success("disabled")
        }
        return
      }

      "read" -> {
        nfcAdapter?.enableReaderMode(
          activity,
          this,
          NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
          null
        )

        return
      }

      "stop" -> {
        Log.e("card", "stop")
//      nfcAdapter?.disableForegroundDispatch(activity);
        nfcAdapter?.disableReaderMode(activity);
        return
      }
    }

  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun cardIsReadyToRead(card: EmvCard) {
    buildResult(card);
  }

  override fun cardMovedTooFastOrLockedNfc() {
    Toast.makeText(this.activity, "Moved too fast or Locked", Toast.LENGTH_SHORT).show()
  }

  override fun errorReadingOrUnsupportedCard() {
    Toast.makeText(this.activity, "Unsupported card", Toast.LENGTH_SHORT).show()
//    Log.d("umid0202", "errorReadingOrUnsupportedCard");
  }

  override fun onTagDiscovered(tag: Tag?) {
    SimpleCardReader.readCard(tag, this)
  }

  private fun buildResult(card: EmvCard?) {
//    Log.d(
//      "card", "${card!!.cardNumber}_${card!!.expireDateMonth}/${card.expireDateYear}"
//    )
    if (card != null && result != null) {
//      Log.d(
//        "card", "${card.cardNumber}_${card.expireDateMonth}/${card.expireDateYear}"
//      )
      result!!.success("${card.cardNumber}_${card.expireDateMonth}/${card.expireDateYear}")
//      Log.e("card", "DONE")
      nfcAdapter?.disableReaderMode(activity);

    } else {
      Toast.makeText(this.activity, "NFC error", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    val activity = binding.activity
    this.activity = activity
    nfcManager = activity.getSystemService(Context.NFC_SERVICE) as NfcManager
    nfcAdapter = nfcManager?.defaultAdapter
    this.activity!!.requestPermissions(
      arrayOf(Manifest.permission.NFC), PERMISSION_NFC
    )
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding): Unit =
    onAttachedToActivity(binding)

  override fun onDetachedFromActivityForConfigChanges(): Unit = onDetachedFromActivity()

  override fun onDetachedFromActivity() {
    activity = null
  }


}
