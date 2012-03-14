package senacor.hd.impl;

import senacor.hd.GreeterService;

public class GreeterImplGerman implements GreeterService {

	public String sayHello(String name) {
		return "Hallo, "+name+"!";
	}
}
