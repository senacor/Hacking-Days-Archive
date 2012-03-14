package winzinger.poc.tef.felixintegration;

import org.osgi.framework.ServiceReference;

public class Logger extends org.apache.felix.framework.Logger {

	@Override
	protected void doLog(ServiceReference sr, int level, String msg,
			Throwable throwable) {
		System.err.println("### " + msg);
		super.doLog(sr, level, msg, throwable);
	}

}
