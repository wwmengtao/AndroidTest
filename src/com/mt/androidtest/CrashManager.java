package com.mt.androidtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * http://blog.csdn.net/urmytch/article/details/53642945
 * ����Ӧ��δԤ������쳣
 * @author _TODO
 *
 */

public class CrashManager implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "CrashManager_UncaughtExceptionHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos;
    private ATApplication application;
    
    public CrashManager(ATApplication application){
        //��ȡϵͳĬ�ϵ�UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }
    
    private boolean handleException(final Throwable exc){
        if (exc == null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ALog.Log("��������д����־");
                flushBufferedUrlsAndReturn();
                //�������
                collectDeviceAndUserInfo(application);
                String CrashFileName = writeCrash(exc);
                Toast.makeText(application.getApplicationContext(), "CrashFile saved path:\n"+CrashFileName, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
        return true;
    }

    /**
     * ��δ���̵�url�ͷ�������д����־�ļ�
     */
    private void flushBufferedUrlsAndReturn(){
        //TODO ��������������ʱ��url�ͷ���xml��json���ݻ����ڶ����У�����ʱ��д���Ա����ԭ��
    }

    /**
     * �ɼ��豸���û���Ϣ
     * @param context ������
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
     * �ɼ�����ԭ��
     * @param exc �쳣
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
            ALog.Log("filePath: "+filePath);//filePath: "/storage/emulated/0/Download/com.mt.androidtest/Crash/"
            CrashFileName = writeLog(sb.toString(), filePath);
        }
        return CrashFileName;
    }
    /**
     *
     * @param log �ļ�����
     * @param name �ļ�·��
     * @return ����д����ļ�·��
     * д��Log��Ϣ�ķ�����д�뵽SD������
     */
    private String writeLog(String log, String name)
    {
        String filename = name + "mycrash"+ ".log";
        File file =new File(filename);
        if(!file.getParentFile().exists()){
        	ALog.Log(TAG, "�½��ļ�");
            file.getParentFile().mkdirs();
        }
        if (file != null && file.exists() && file.length() + log.length() >= 64 * 1024) {
            //������־�ļ���С
            file.delete();
        }
        try
        {
            file.createNewFile();
            FileWriter fw=new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            //д�����Log���ļ�
            bw.write(log);
            bw.newLine();
            bw.close();
            fw.close();
            return filename;
        }
        catch(IOException e)
        {
        	ALog.Log(TAG, e.getMessage());
            return null;
        }
    }
    
    @Override
    public void uncaughtException(Thread thread, Throwable exc) {
        if(!handleException(exc) && mDefaultHandler != null){
            //����û�û�д�������ϵͳĬ�ϵ��쳣������������
            mDefaultHandler.uncaughtException(thread, exc);
        }else{
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
            	ALog.Log(TAG, e.getMessage());
            }
            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    application.getApplicationContext(), 0, intent,
                    0);
            //�˳�����
            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
            //1�������Ӧ��
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}