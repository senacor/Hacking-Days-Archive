package senacor.hd.mw.artworks.osgihelper;

import javax.annotation.Resource;

import senacor.hd.sgh.HookService;

/** 
 * This class is necessary to get spring to load the HookService.
 * If this isn't done, then the HookService lookup doesn't work in the server - seems to be a bug...
 * @author sscheuri
 */
public class SomeMystericalClass {
	@Resource(name="senacor.hd.sgh.HookService")
	private HookService hookService;
}
