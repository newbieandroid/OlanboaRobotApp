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
#萤石
-libraryjars  ../YSCameraSdk/libs/EZOpenSDK_V4.5.0.20170605.jar
-libraryjars  ../YSCameraSdk/libs/glide-3.7.0.jar
-libraryjars  ../YSCameraSdk/libs/jmdns-3.4.1.jar

-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libAnalyzeData.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libAudioEngine.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libCpuFeatures.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libcurl.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libezstreamclient.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libgnustl_shared.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libhpr.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libminiupnpc.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libopensslwrap.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libPlayCtrl.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libprotobuf-lite.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libStunClientSDK.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libSystemTransform.so
-libraryjars  ../YSCameraSdk/src/main/jniLibs/armeabi/libUdpClient.so