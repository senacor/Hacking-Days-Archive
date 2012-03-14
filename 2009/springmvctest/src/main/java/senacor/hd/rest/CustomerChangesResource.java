package senacor.hd.rest;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomerChangesResource {
	private static Logger LOG = Logger.getLogger("CustomerChangesResource");
	
	@RequestMapping(value="/customers/changes", method=RequestMethod.GET)
	public String getCustomerChanges(Model model) {
		LOG.info("getCustomerChanges - ???");
		
		return "customerChanges";
	}
}
