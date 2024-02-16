package agro.card.nfc.nfc_card_example

import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import io.flutter.FlutterInjector
import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.PluginRegistry


open class Application : FlutterApplication() {

  override fun onCreate() {
    super.onCreate()
    MultiDex.install(this)
  }
  // override fun registerWith(registry: PluginRegistry?) {
  //     val key: String? = FlutterFirebaseMessagingPlugin::class.java.canonicalName
  //     if (!registry?.hasPlugin(key)!!) {
  //         FlutterFirebaseMessagingPlugin.registerWith(registry?.registrarFor("io.flutter.plugins.firebase.messaging.FlutterFirebaseMessagingPlugin"));
  //       }
  //   }
  // override fun registerWith(registry: PluginRegistry?) {
//      val key: String = FlutterFirebaseMessagingPlugin::class.java.canonicalName
//      if (!registry?.hasPlugin(key)!!) {
//          FlutterFirebaseMessagingPlugin.registerWith(registry.registrarFor("io.flutter.plugins.firebase.messaging.FlutterFirebaseMessagingPlugin"));
//        }
  // }
  //   override fun registerWith(registry: PluginRegistry?) {
  //       io.flutter.plugins.firebasemessaging.FirebaseMessagingPlugin.registerWith(registry?.registrarFor("io.flutter.plugins.firebasemessaging.FirebaseMessagingPlugin"));
  //   }

//      override fun registerWith(registry: PluginRegistry?) {
//        val key: String? = FlutterFirebaseMessagingPlugin::class.java.canonicalName
//        if (!registry?.hasPlugin(key)!!) {
//            FlutterFirebaseMessagingPlugin.registerWith(registry?.registrarFor("io.flutter.plugins.firebase.messaging.FlutterFirebaseMessagingPlugin"));
//        }
//    }

}

//public class FlutterApplication : Application{
//
//    @Override
//    @CallSuper
//    public void onCreate(){
//        super.onCreate();
//        FlutterInjector.instance().flutterLoader().startInitialization(this);
//    }
//
//    private Activity mCurrentActivity = null;
//
//    public Activity getCurrentActivity() {
//        return mCurrentActivity;
//    }
//
//    public void setCurrentActivity(Activity mCurrentActivity){
//        this.mCurrentActivity = mCurrentActivity;
//    }
//
//}



