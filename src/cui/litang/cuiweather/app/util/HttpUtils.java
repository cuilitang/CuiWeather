package cui.litang.cuiweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
	
	/**
	 * 发送Http请求
	 * @param address http请求地址
	 * @param listener 请求结束或者异常时候的回调函数
	 */
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			private HttpURLConnection conn;

			@Override
			public void run() {
				
				try{
					
					URL url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					InputStream inputStream = conn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					
					StringBuilder builder = new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
						builder.append(line);
					}
					
					if(listener!=null){
						listener.onFinish(builder.toString());
					}
					
				}catch(Exception e){
					e.printStackTrace();
					if(listener!=null){
						listener.onError(e);
					}
				}finally{
					if(conn!=null){
						conn.disconnect();
						conn = null;
					}
				}
				
			}
		}).start();
		
	}
}
