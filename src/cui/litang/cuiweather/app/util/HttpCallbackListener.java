package cui.litang.cuiweather.app.util;

public interface HttpCallbackListener {
	/**
	 * http请求结束时候所做的操作
	 * @param response response返回的字符串
	 */
	void onFinish(String response);
	
	/**
	 * http请求报异常时候执行的操作
	 * @param e 抛出的异常
	 */
	void onError(Exception e);

}
