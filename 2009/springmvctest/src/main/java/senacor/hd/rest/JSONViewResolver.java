package senacor.hd.rest;

import java.util.Locale;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class JSONViewResolver extends WebApplicationObjectSupport implements ViewResolver, Ordered {
    private static Logger LOG = Logger.getLogger("JSONViewResolver");
    
    private int order = Integer.MAX_VALUE; // default: same as non-Ordered

    public void setOrder(int order) {
    	this.order = order;
    }

    public int getOrder() {
    	return order;
    }

	public View resolveViewName(String viewName, Locale loc) throws Exception {
		String patchedName = viewName+".JSON";
		
		LOG.info("changing '"+viewName+"' to '"+patchedName+"'");

	    ApplicationContext context = getApplicationContext();
	    if (!context.containsBean(patchedName)) {
	    	LOG.info("not found");
	    	return null;
	    }
	    
	    return (View) context.getBean(patchedName, View.class);
	}

}
