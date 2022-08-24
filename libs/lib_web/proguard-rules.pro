# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
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

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
-keep public class android.support.v4.app.FragmentManagerImpl
#如果引用了v4或者v7包
-dontwarn android.support.**
-keep class android.support.** {*;}
#忽略警告
-ignorewarning

#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#再次崩溃的时候就有源文件和行号的信息了
-keepattributes SourceFile,LineNumberTable
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆

-keep class com.tencent.smtt.export.external.**{
     *;
 }

 -keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
 	*;
 }

 -keep class com.tencent.smtt.sdk.CacheManager {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.CookieManager {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.WebHistoryItem {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.WebViewDatabase {
 	public *;
 }

 -keep class com.tencent.smtt.sdk.WebBackForwardList {
 	public *;
 }

 -keep public class com.tencent.smtt.sdk.WebView {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
 	public static final <fields>;
 	public java.lang.String getExtra();
 	public int getType();
 }

 -keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebView$PictureListener {
 	public <fields>;
 	public <methods>;
 }


 -keepattributes InnerClasses

 -keep public enum com.tencent.smtt.sdk.WebSettings$** {
     *;
 }

 -keep public enum com.tencent.smtt.sdk.QbSdk$** {
     *;
 }

 -keep public class com.tencent.smtt.sdk.WebSettings {
     public *;
 }


 -keepattributes Signature
 -keep public class com.tencent.smtt.sdk.ValueCallback {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebViewClient {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.DownloadListener {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebChromeClient {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
 	public <fields>;
 	public <methods>;
 }

 -keep class com.tencent.smtt.sdk.SystemWebChromeClient{
 	public *;
 }
 # 1. extension interfaces should be apparent
 -keep public class com.tencent.smtt.export.external.extension.interfaces.* {
 	public protected *;
 }

 # 2. interfaces should be apparent
 -keep public class com.tencent.smtt.export.external.interfaces.* {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebIconDatabase {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.WebStorage {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.DownloadListener {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.QbSdk {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
 	public <fields>;
 	public <methods>;
 }
 -keep public class com.tencent.smtt.sdk.CookieSyncManager {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.Tbs* {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.LogFileUtils {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.TbsLog {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.TbsLogClient {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.CookieSyncManager {
 	public <fields>;
 	public <methods>;
 }

 # Added for game demos
 -keep public class com.tencent.smtt.sdk.TBSGamePlayer {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
 	public <fields>;
 	public <methods>;
 }

 -keep public class com.tencent.smtt.utils.Apn {
 	public <fields>;
 	public <methods>;
 }
 # end


 -keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
 	public <fields>;
 	public <methods>;
 }

 -keep class MTT.ThirdAppInfoNew {
 	*;
 }

 -keep class com.tencent.mtt.MttTraceEvent {
 	*;
 }

 # Game related
 -keep public class com.tencent.smtt.gamesdk.* {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.TBSGameBooter {
         public <fields>;
         public <methods>;
 }

 -keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
 	public protected *;
 }

 -keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
 	public protected *;
 }

 -keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
 	public *;
 }
 #腾讯X5内核 END
