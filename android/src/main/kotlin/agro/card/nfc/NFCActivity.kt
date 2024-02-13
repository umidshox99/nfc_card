package uz.agrobank.chakanapay

import agro.card.nfc.SimpleCardReader
import agro.card.nfc.model.EmvCard
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import uz.agrobank.chakanapay.databinding.ActivityNfcBinding

//import uz.agrobank.winn.databinding.ActivityNfcBinding

var buttonText: String = ""
var infoText: String = ""
var nfc_error: String = ""
var nfc_no_available: String = ""
var nfcAppBar = ""
var yesText = ""
var noText = ""
var isTurnOnNfcText = ""
var isDark: Boolean = false


class NFCActivity : AppCompatActivity(), SimpleCardReader.SimpleCardReaderCallback,
    NfcAdapter.ReaderCallback {
    private lateinit var binding: ActivityNfcBinding
    private var mSafeExit = false
    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        binding.backButton.text = buttonText
        binding.description.text = infoText
        binding.nfcAppBarText.text = nfcAppBar
        val bgElement: RelativeLayout = findViewById<View>(R.id.container_id) as RelativeLayout

        if (isDark) {
            binding.back.setColorFilter(Color.WHITE)
            binding.nfcAppBarText.setTextColor(Color.WHITE)
            binding.appBar.setBackgroundColor(Color.rgb(17, 17, 17))
            binding.description.setTextColor(Color.WHITE)
//            binding.containerId.setBackgroundColor(Color.rgb(17, 17, 17))
            bgElement.setBackgroundColor(Color.rgb(17, 17, 17))

        } else {
            binding.back.setColorFilter(Color.BLACK)
            binding.nfcAppBarText.setTextColor(Color.BLACK)
            binding.appBar.setBackgroundColor(Color.WHITE)
            binding.description.setTextColor(Color.BLACK)
//            binding.containerId.setBackgroundColor(Color.rgb(244, 244, 244))
            bgElement.setBackgroundColor(Color.rgb(244, 244, 244))

        }

        if (mNfcAdapter == null) {
            Toast.makeText(this, nfc_no_available, Toast.LENGTH_SHORT).show()
        } else if (mNfcAdapter?.isEnabled == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    startActivityForResult(Intent(Settings.Panel.ACTION_NFC), 2265)
                } catch (ignored: ActivityNotFoundException) {
                }

            } else {
                try {
                    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                } catch (ignored: ActivityNotFoundException) {
                }
            }
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun nfcIsEnabled() {
        if (!NfcAdapter.getDefaultAdapter(this).isEnabled) {
            showDialog()
        }
    }

    public override fun onResume() {
        super.onResume()
        mNfcAdapter?.enableReaderMode(
            this, this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }


    private fun buildResult(card: EmvCard?) {
//        Toast.makeText(
//            this,
//            "${card?.cardNumber}_${card?.expireDateMonth}/${card?.expireDateYear}",
//            Toast.LENGTH_SHORT
//        ).show()

        if (card != null) {
            val intent = Intent()
            intent.putExtra(
                "card",
                "${card.cardNumber}_${card.expireDateMonth}/${card.expireDateYear}"
            )
            Log.d(
                "card",
                "${card.cardNumber}_${card.expireDateMonth}/${card.expireDateYear}"
            )
            setResult(98, intent)
            finish()
        } else {
            Toast.makeText(this, nfc_error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (mSafeExit) {
            super.onBackPressed()
        }
    }


    public override fun onPause() {
        super.onPause()
        mNfcAdapter?.disableReaderMode(this)
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        var textView = dialog.findViewById(R.id.txt_dia) as TextView
        textView.text = isTurnOnNfcText
        var dialog_bc = dialog.findViewById(R.id.dialog_bc) as LinearLayout
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        yesBtn.text = yesText
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        noBtn.text = noText

        if (isDark) {
            textView.setTextColor(Color.WHITE)
            yesBtn.setBackgroundColor(Color.rgb(35, 35, 36))
            noBtn.setBackgroundColor(Color.rgb(35, 35, 36))
            dialog_bc.setBackgroundColor(Color.rgb(35, 35, 36))
        } else {
            textView.setTextColor(Color.BLACK)
            dialog_bc.setBackgroundColor(Color.WHITE)
        }
        yesBtn.setOnClickListener {
            dialog.dismiss()
            val myIntent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
            this.startActivityForResult(myIntent, 101)
        }
        noBtn.setOnClickListener {
            finish()
        }
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        nfcIsEnabled()
    }

    override fun cardIsReadyToRead(card: EmvCard) {
        buildResult(card);
    }

    override fun cardMovedTooFastOrLockedNfc() {
        Toast.makeText(this, "Moved too fast or Locked", Toast.LENGTH_SHORT).show()
    }

    override fun errorReadingOrUnsupportedCard() {
        Toast.makeText(this, "Unsupported card", Toast.LENGTH_SHORT).show()
        Log.d("umid0202", "errorReadingOrUnsupportedCard");
    }

    override fun onTagDiscovered(tag: Tag?) {
        SimpleCardReader.readCard(tag, this)
    }
}
