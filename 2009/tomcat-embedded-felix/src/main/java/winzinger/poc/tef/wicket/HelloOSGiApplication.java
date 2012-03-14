package winzinger.poc.tef.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class HelloOSGiApplication extends WebApplication {

	@Override
	// public Class<HelloOSGi> getHomePage() {
	//     return HelloOSGi.class;
	// }
	public Class<OSGIpoc> getHomePage() {
		return OSGIpoc.class;
	}

	@Override
	protected void init() {
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this));
	}
}
