import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:nfc_card/nfc_card.dart';
import 'package:nfc_card/nfc_card_method_channel.dart';
import 'package:nfc_card_example/second_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: SecondPage(),
    );
  }
}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String _platformVersion = 'Unknown';
  NFCAvailability? isAviable;
  String? card;
  final _nfcCardPlugin = NfcCard();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      isAviable = await _nfcCardPlugin.checkNFCAvailability();
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    if (!mounted) return;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: Column(
          children: [
            Text('Running on: ${isAviable}\n $card'),
            TextButton(
              child: Text('start'),
              onPressed: () async {
                String? card = await _nfcCardPlugin.read();
                 await _nfcCardPlugin.stop();
                Navigator.pop(context, card);
              },
            ),
            TextButton(
              child: Text('stop'),
              onPressed: () {
                // Navigator.push(context,
                //     MaterialPageRoute(builder: (context) => SecondPage()));
              },
            ),
          ],
        ),
      ),
    );
  }
}
