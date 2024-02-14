import 'package:nfc_card/nfc_card_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class NfcCardPlatform extends PlatformInterface {
  /// Constructs a NfcAndroidPlatform.
  NfcCardPlatform() : super(token: _token);

  static final Object _token = Object();

  static NfcCardPlatform _instance = MethodChannelNfcCard();

  /// The default instance of [NfcCardPlatform] to use.
  ///
  /// Defaults to [MethodChannelNfcAndroid].
  static NfcCardPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [NfcCardPlatform] when
  /// they register themselves.
  static set instance(NfcCardPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }


  Future<String?> read();

  Future stop();

  Future<NFCAvailability?> checkNFCAvailability();

}
