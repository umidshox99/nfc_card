import 'package:flutter_test/flutter_test.dart';
import 'package:nfc_card/nfc_card.dart';
import 'package:nfc_card/nfc_card_platform_interface.dart';
import 'package:nfc_card/nfc_card_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockNfcCardPlatform
    with MockPlatformInterfaceMixin
    implements NfcCardPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final NfcCardPlatform initialPlatform = NfcCardPlatform.instance;

  test('$MethodChannelNfcCard is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelNfcCard>());
  });

  test('getPlatformVersion', () async {
    NfcCard nfcCardPlugin = NfcCard();
    MockNfcCardPlatform fakePlatform = MockNfcCardPlatform();
    NfcCardPlatform.instance = fakePlatform;

    expect(await nfcCardPlugin.getPlatformVersion(), '42');
  });
}
