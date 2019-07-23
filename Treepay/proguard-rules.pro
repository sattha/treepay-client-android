# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hhan/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

-keep public class kr.co.kcp.treepay.common.android.activity.** { *; }
-keep public class kr.co.kcp.treepay.common.model.** { *; }
-keep public class kr.co.kcp.treepay.common.retrofit.** { *; }
-keep public class kr.co.kcp.treepay.common.** { *; }

-dontwarn okio.**
-keep public class okio.** { *; }

-dontwarn butterknife.**
-keep public class butterknife.** { *; }

#retrofit2
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

-dontwarn okio.**

#okhttp3
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class okhttp3.* { *; }
-keep interface okhttp3.* { *; }
-dontwarn okhttp3.*
