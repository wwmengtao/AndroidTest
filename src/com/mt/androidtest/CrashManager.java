package com.mt.androidtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**需要当前应用获取"android.permission.READ_EXTERNAL_STORAGE"以及"android.permission.WRITE_EXTERNAL_STORAGE"权限
 * http://blog.csdn.net/urmytch/article/details/53642945
 * 捕获应用未预处理的异常
 * @author _TODO
 *
 */

public class CrashManager implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "CrashManager";
    private static final String SAVE_CRASH_EXCEPTION = "MSG_SAVE_CRASH_EXCEPTION";
    private static final String STOP_APP_AND_DO_CLEAN = "MSG_STOP_APP_AND_DO_CLEAN";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos;
    private ATApplication application;
    private HandlerThread mHandlerThread = null;
    private HandlerCostTime mHandlerCostTime=null;

    public CrashManager(ATApplication application){
        //获取系统默认的UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    @Override
    /**
     * uncaughtException：Thread.UncaughtExceptionHandler接口类默认调用的处理异常的函数
     */
    public void uncaughtException(Thread thread, Throwable exc) {
        if(!handleException(exc) && mDefaultHandler != null){
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, exc);
        }else{
            try{
                Thread.sleep(3000);//确保提示性Toast可以被用户看到
            }catch (InterruptedException e){
                ALog.Log(TAG, e.getMessage());
            }
            Message msg = Message.obtain();
            msg.obj = STOP_APP_AND_DO_CLEAN;
            mHandlerCostTime.sendMessage(msg);
        }
    }

    private boolean handleException(final Throwable exc){
        if (exc == null) {
            return false;
        }
        initHandlerThread();
        sendCrashMessage(exc);
        return true;
    }

    public void initHandlerThread(){
        mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_FOREGROUND);
        mHandlerThread.start();
        //应用崩溃时，application.getMainLooper()为null，因此采用mHandlerThread.getLooper()
        mHandlerCostTime = new HandlerCostTime(mHandlerThread.getLooper());
    }

    /**
     * 将exc中异常信息写入进log文件中
     * @param exc
     */
    private void sendCrashMessage(Throwable exc){
        Bundle mBundle = new Bundle();
        Message msg = Message.obtain();
        msg.obj = SAVE_CRASH_EXCEPTION;
        mBundle.putSerializable(SAVE_CRASH_EXCEPTION, exc);
        msg.setData(mBundle);
        //应用崩溃只能使用mHandlerThread.getLooper()，主线程的Looper已经为null
        if(null != mHandlerThread.getLooper())
            mHandlerCostTime.sendMessage(msg);
    }

    class HandlerCostTime extends Handler {
        public HandlerCostTime(Looper loop) {
            super(loop);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch((String)msg.obj){
                case SAVE_CRASH_EXCEPTION:
                    flushBufferedUrlsAndReturn();
                    collectDeviceAndUserInfo(application);
                    String CrashFileName = writeCrash((Throwable)msg.getData().getSerializable(SAVE_CRASH_EXCEPTION));
                    ALog.Log(TAG,"SAVE_CRASH_EXCEPTION: Show Toast!");
                    Toast.makeText(application.getApplicationContext(), "CrashFile saved path:\n"+CrashFileName, Toast.LENGTH_LONG).show();
                    break;
                case STOP_APP_AND_DO_CLEAN:
                    Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                    PendingIntent restartIntent = PendingIntent.getActivity(
                            application.getApplicationContext(), 0, intent,0);
                    //退出程序
                    AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
                    //1秒后重启应用
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                            restartIntent);
//                    //下列代码无须执行，因为应用崩溃重启后，内存泄露威胁自然解除
//                    mHandlerCostTime.removeCallbacksAndMessages(null);
//                    mHandlerThread.getLooper().quit();
                    ALog.Log(TAG,"killProcess");
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
            }
        }
    }

    /**
     * 可以在请求网络时把url和返回xml或json数据缓存在队列中，崩溃时先写入以便查明原因
     */
    private void flushBufferedUrlsAndReturn(){
        //TODO
    }

    /**
     * 采集设备和用户信息
     * @param context 上下文
     */
    private void collectDeviceAndUserInfo(Context context){
        PackageManager pm = context.getPackageManager();
        infos = new HashMap<String, String>();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null?"null":pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName",versionName);
                infos.put("versionCode",versionCode);
                infos.put("crashTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        } catch (PackageManager.NameNotFoundException e) {
        	ALog.Log(TAG, e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
        } catch (IllegalAccessException e) {
        	ALog.Log(TAG, e.getMessage());
        }
    }

    /**
     * 采集崩溃原因
     * @param exc 异常
     */
    private String writeCrash(Throwable exc){
    	String CrashFileName = null;
        StringBuffer sb = new StringBuffer();
        sb.append("------------------crash----------------------");
        sb.append("\r\n");
        for (Map.Entry<String,String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key+"="+value+"\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        exc.printStackTrace(pw);
        sb.append("-------------crash_getCause---------------");
        sb.append("\r\n");
        Throwable excCause = exc.getCause();
        while (excCause != null) {
            excCause.printStackTrace(pw);
            excCause = excCause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        sb.append("\r\n");
        sb.append("-------------------end-----------------------");
        sb.append("\r\n");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            String sdcardPath = Environment.getExternalStorageDirectory().getPath();
            String filePath = sdcardPath + File.separator+"Download"+ File.separator+application.getPackageName()
            		+ File.separator+"Crash"+ File.separator;
//            ALog.Log(TAG, "filePath: "+filePath);//filePath: "/storage/emulated/0/Download/packageName/Crash/"
            CrashFileName = writeLog(sb.toString(), filePath);
        }
        return CrashFileName;
    }

    /**
     *写入Log信息的方法，写入到SD卡里面
     * @param log 文件内容
     * @param name 文件路径
     * @return 返回写入的文件路径
     */
    private String writeLog(String log, String name){
        String filename = name + "mycrash.log";
        File file =new File(filename);
        if(!file.getParentFile().exists()){
            if(!file.getParentFile().mkdirs()){
                ALog.Log(TAG, "Can not mkdirs: "+file.getParentFile().toString());
                tipsForCheckStoragePermissions("writeLog");
                return null;
            }
        }
        if (file != null && file.exists() && file.length() + log.length() >= 64 * 1024) {
            //控制日志文件大小，因为此Crash日志是不断追加的
            file.delete();
        }
        try{
            ALog.Log(TAG,"Create crash log: "+file.getAbsolutePath());
            file.createNewFile();
            FileWriter fw=new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            //写入相关Log到文件
            bw.write(log);
            bw.newLine();
            bw.close();
            fw.close();
            return filename;
        }catch(IOException e){
        	ALog.Log(TAG, "IOException: "+e.getMessage());//IOException: No such file or directory
            tipsForCheckStoragePermissions("IOException:");
            return null;
        }
    }

    // 本应用可能没有权限
    private void tipsForCheckStoragePermissions(String mark){
        String tips = "Please require READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions!";
        ALog.Log(TAG, mark+": "+tips);
    }
}