package senacor.hd.sharedservices.infrastructure;

import javax.annotation.Resource;

import senacor.hd.sgh.HookService;

public class SomeMystericalClass {
	@Resource(name="senacor.hd.sgh.HookService")
	private HookService hookService;
}
