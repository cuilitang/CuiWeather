package cui.litang.cuiweather.app.receiver;

import cui.litang.cuiweather.app.service.AutoupdateService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoupdateRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
			Intent i = new Intent(context, AutoupdateService.class);		
			context.startService(i);
	}

}
