package senacor.hd.impl;

import senacor.hd.GreeterService;

public class GreeterImplEnglish implements GreeterService {

	public String sayHello(String name) {
		return "Hello, "+name+"!";
	}
}
