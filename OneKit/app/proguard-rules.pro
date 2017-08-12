-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
    @org.xwalk.core.JavascriptInterface <methods>;
    @org.chromium.content.browser.JavascriptInterfacee <methods>;
}
-keep class org.xwalk.core.** {
    *;
}
-keep class org.chromium.** {
    *;
}
-keep class android.webkit.JavascriptInterface
-keepattributes **
-keepattributes Signature,Exceptions,InnerClasses,EnclosingMethod,Deprecated
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes *JavascriptInterface*