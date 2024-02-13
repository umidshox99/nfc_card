#include "include/nfc_card/nfc_card_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "nfc_card_plugin.h"

void NfcCardPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  nfc_card::NfcCardPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
