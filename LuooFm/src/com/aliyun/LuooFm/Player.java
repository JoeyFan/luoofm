package com.aliyun.LuooFm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

public class Player {
	
	protected static final String TAG = "my";
	private MediaPlayer mediaPlayer;

	public Player() {
		mediaPlayer = new MediaPlayer();		
		mediaPlayer.setOnErrorListener(  
                new MediaPlayer.OnErrorListener() {  
                    public boolean onError(MediaPlayer mp, int what, int extra) {  
                        Log.e(getClass().getName(), "Error in MediaPlayer: (" + what +") with extra (" +extra +")" );  
                        return false;  
                    }  
                });  
	}
	public void play(String url) throws IOException   
    {  
        getFile(url);       
    }  
      
	 private void getFile(final String strPath)   
	    {   
	      try  
	      {   
	        Runnable r = new Runnable()   
	        {   
	          public void run()   
	          {   
	            try  
	            {   
	              // ����һ���߳̽���Զ���ļ�����   
	              getDataSource(strPath);   
	            } catch (Exception e)   
	            {   
	              Log.e(TAG, e.getMessage(), e);   
	            }   
	          }   
	        };   
	        new Thread(r).start();   
	      } catch (Exception e)   
	      {   
	        e.printStackTrace();   
	      }   
	    }   
	private void getDataSource(String strPath) throws Exception   
	{   
		String fileEx = strPath.substring(strPath.lastIndexOf(".") + 1, strPath.length()).toLowerCase(); 
	     System.out.println("***************fileEx =" + fileEx);   
	     String fileNa = strPath.substring(strPath.lastIndexOf("/") + 1, strPath.lastIndexOf(".")); 
		if (!URLUtil.isNetworkUrl(strPath))   
		{   
			System.out.println("�����URL");   
		} else  
		{   
			/* ȡ��URL */  
			URL myURL = new URL(strPath);   
			/* �������� */  
			URLConnection conn = myURL.openConnection();   
			conn.connect();   
			/* InputStream �����ļ� */  
			InputStream is = conn.getInputStream();   
			if (is == null)   
			{   
				throw new RuntimeException("stream is null");   
			}   
			// ������ʱ�ļ�    
			// ���������ֱ�Ϊǰ׺�ͺ�׺
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				String sdCardDir = Environment.getExternalStorageDirectory() + "/LuooFm/download/";
				new File(sdCardDir).mkdirs();
				File saveFile = new File(sdCardDir, fileNa + "." + fileEx);  				
				saveFile.getAbsolutePath();   
				/* ���ļ�д���ݴ��� */  
				FileOutputStream fos = new FileOutputStream(saveFile);   
				byte buf[] = new byte[128];   
				do  
				{   
					int numread = is.read(buf);   
					if (numread <= 0)   
					{   
						break;   
					}   
					fos.write(buf, 0, numread);   
				} while (true); 
				Log.d("my", "DOWNLOADED " + saveFile.getAbsolutePath());
				//��ȡ�ļ�·��  
		        mediaPlayer.reset();  
		        FileInputStream fis = new FileInputStream(saveFile);  
		        mediaPlayer.setDataSource(fis.getFD());  		      
		        mediaPlayer.prepare();  
		        mediaPlayer.start();  				
			}
		}   
	}  
    
}
