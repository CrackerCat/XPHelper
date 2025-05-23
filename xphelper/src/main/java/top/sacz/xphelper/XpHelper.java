package top.sacz.xphelper;

import android.annotation.SuppressLint;
import android.content.Context;

import de.robv.android.xposed.IXposedHookZygoteInit;
import top.sacz.xphelper.activity.ActivityProxyManager;
import top.sacz.xphelper.dexkit.cache.DexKitCache;
import top.sacz.xphelper.reflect.ClassUtils;
import top.sacz.xphelper.util.ActivityTools;
import top.sacz.xphelper.util.ConfigUtils;

public class XpHelper {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static ClassLoader classLoader;

    public static String moduleApkPath;

    public static void initContext(Context application) {
        context = application;
        classLoader = application.getClassLoader();
        ClassUtils.intiClassLoader(classLoader);
        ConfigUtils.initialize(application);
        ActivityProxyManager.initActivityProxyManager(application);
        DexKitCache.checkCacheExpired(application);
    }

    public static void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
        moduleApkPath = startupParam.modulePath;
    }

    /**
     * 设置配置存储路径
     */
    public static void setConfigPath(String pathDir) {
        ConfigUtils.initialize(pathDir);
    }

    /**
     * 设置配置默认密码
     * @param password 密码 采用AES加密 不设置则不使用加密
     */
    public static void setConfigPassword(String password) {
        ConfigUtils.setGlobalPassword(password);
    }

    /**
     * 注入模块的Res资源到上下文中
     *
     * @param context 要注入的上下文
     */
    public static void injectResourcesToContext(Context context) {
        ActivityTools.injectResourcesToContext(context, moduleApkPath);
    }

}
