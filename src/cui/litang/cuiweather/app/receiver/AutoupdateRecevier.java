package cui.litang.cuiweather.app.receiver;

import cui.litang.cuiweather.app.service.AutoupdateService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * service与receiver互调，保证service的运行。
 * @author Cuilitang
 * @Date 2015年5月28日
 */
public class AutoupdateRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
			Intent i = new Intent(context, AutoupdateService.class);		
			context.startService(i);
	}

}
