package senacor.hd.rest;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

public class CustomerChangesView implements View {
	private static Logger LOG = Logger.getLogger("CustomerChangesView");

	public String getContentType() {
		LOG.info("content type requested");
		
		return "application/json";
	}

	public void render(Map<String, ?> arg0, HttpServletRequest arg1, HttpServletResponse resp) throws Exception {
		resp.getOutputStream().print("done json");

	}

}
