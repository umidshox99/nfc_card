import 'package:flutter/material.dart';
import 'package:nfc_card_example/main.dart';

class SecondPage extends StatefulWidget {
  const SecondPage({super.key});

  @override
  State<SecondPage> createState() => _SecondPageState();
}

class _SecondPageState extends State<SecondPage> {
  String? card;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          children: [
            Text(card ?? ''),
            TextButton(
              child: Text("read"),
              onPressed: () async {
                card = await Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => HomePage(),
                    ));
                setState(() {});
              },
            ),
          ],
        ),
      ),
    );
  }
}
