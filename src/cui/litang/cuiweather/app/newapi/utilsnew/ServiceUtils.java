package cui.litang.cuiweather.app.newapi.utilsnew;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * Service工具类
 * @author Cuilitang
 * @Date 2015年7月16日
 */
public class ServiceUtils {
	
	/**
	 * 检测服务目前是否运行
	 * @param context 上下文
	 * @param serviceName 服务全路径名
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String serviceName) {
		
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServicesInfo = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServicesInfo) {
			String name = runningServiceInfo.service.getClassName();
			if (serviceName.equals(name)) {
				return true;
			}
			
		}
		return false;
		
	}

}
