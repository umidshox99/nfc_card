import 'package:nfc_card/nfc_card_method_channel.dart';
import 'package:nfc_card/nfc_card_platform_interface.dart';

class NfcCard {
  Future<NFCAvailability?> checkNFCAvailability() {
    return NfcCardPlatform.instance.checkNFCAvailability();
  }

  Future<String?> read() {
    return NfcCardPlatform.instance.read();
  }
  Future stop() {
    return NfcCardPlatform.instance.read();
  }
}
