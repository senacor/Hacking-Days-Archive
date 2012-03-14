package winzinger.poc.tef.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import senacor.hd.GreeterService;

public class HelloWorldController extends AbstractController {
	private  GreeterService greeterService;
	
	public GreeterService getGreeterService() {
		return greeterService;
	}

	public void setGreeterService(GreeterService greeterService) {
		this.greeterService = greeterService;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		return new ModelAndView("welcomePage","welcomeMessage", greeterService.sayHello("Ralph - MVC"));
	}
}
