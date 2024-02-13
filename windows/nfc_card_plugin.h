#ifndef FLUTTER_PLUGIN_NFC_CARD_PLUGIN_H_
#define FLUTTER_PLUGIN_NFC_CARD_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace nfc_card {

class NfcCardPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  NfcCardPlugin();

  virtual ~NfcCardPlugin();

  // Disallow copy and assign.
  NfcCardPlugin(const NfcCardPlugin&) = delete;
  NfcCardPlugin& operator=(const NfcCardPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace nfc_card

#endif  // FLUTTER_PLUGIN_NFC_CARD_PLUGIN_H_
