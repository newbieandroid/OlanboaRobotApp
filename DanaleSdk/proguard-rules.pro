# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#大拿
-libraryjars  ../DanaleSdk/libs/danale-player-v5.3.6-20170607.jar
-libraryjars  ../DanaleSdk/libs/danale-record-thumb-20170421.jar
-libraryjars  ../DanaleSdk/libs/danale-video-sdk-v5.3.6-20170412.jar
-libraryjars  ../DanaleSdk/libs/okhttp-3.4.1.jar
-libraryjars  ../DanaleSdk/libs/okio-1.6.0.jar
-libraryjars  ../DanaleSdk/libs/osslib.jar
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libdanale_sdk_device.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libDanaleAirLink.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libDanaleEucVideo.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libdecoder.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libDtmfAudioCodec.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libh264decoder.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libH265Decode.so
-libraryjars  ../DanaleSdk/src/main/jniLibs/armeabi/libvideodecoder.so