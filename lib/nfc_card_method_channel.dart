import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:nfc_card/nfc_card_platform_interface.dart';

import 'dart:async';
import 'dart:io' show Platform;

import 'package:flutter/services.dart';

enum NFCStatus {
  none,
  reading,
  read,
  stopped,
  error,
}

class NfcData {
  final String? id;
  final String? content;
  final String? error;
  final String? statusMapper;

  NFCStatus status = NFCStatus.none;

  NfcData({
    this.id,
    this.content,
    this.error,
    this.statusMapper,
  });

  factory NfcData.fromMap(Map data) {
    NfcData result = NfcData(
      id: data['nfcId'],
      content: data['nfcContent'],
      error: data['nfcError'],
      statusMapper: data['nfcStatus'],
    );
    switch (result.statusMapper) {
      case 'none':
        result.status = NFCStatus.none;
        break;
      case 'reading':
        result.status = NFCStatus.reading;
        break;
      case 'stopped':
        result.status = NFCStatus.stopped;
        break;
      case 'error':
        result.status = NFCStatus.error;
        break;
      default:
        result.status = NFCStatus.none;
    }
    return result;
  }
}

enum NFCAvailability { available, disabled, not_supported }

/// An implementation of [NfcAndroidPlatform] that uses method channels.
class MethodChannelNfcCard extends NfcCardPlatform {
  /// The method channel used to interact with the native platform.
  static const methodChannel = MethodChannel('nfc_card');
  static const stream = EventChannel('com.example.nfc_android.nfc_card');

  @override
  Future<String?> read({String? instruction}) async {
    final String data = await _callRead(instruction: instruction);
    return data;
  }

  Future<String> _callRead({String? instruction}) async {
    return await methodChannel.invokeMethod('read');
  }

  @override
  Future<NFCAvailability?> checkNFCAvailability() async {
    String? hasNFC =
        await methodChannel.invokeMethod<String?>("checkNFCAvailability");
    return NFCAvailability.values.byName(hasNFC!);
  }

  @override
  Stream<NfcData?> onTagDiscovered({String? instruction}) {
    if (Platform.isIOS) {}
    return stream.receiveBroadcastStream().map((rawNfcData) {
      return NfcData.fromMap(rawNfcData);
    });
  }

  @override
  Future stop() async {
    await methodChannel.invokeMethod("stop");
  }
}
