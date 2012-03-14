package com.hd11.process.ihu;

import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.process.ProcessEventListener;
import org.drools.io.ResourceFactory;
import org.drools.process.workitem.wsht.WSHumanTaskHandler;
import org.drools.runtime.StatefulKnowledgeSession;

import com.hd11.process.ihu.model.AdminList;
import com.senacor.hd11.model.UserApplication;

public class Ihu {
	private StatefulKnowledgeSession ksession;
	//private KnowledgeRuntimeLogger logger;

	public Ihu() {
		init();
	}
	
	public Ihu(ProcessEventListener listener) { 
		init();
		ksession.addEventListener(listener);
	}
	
	private void init(){
		if (ksession == null) {
			ksession = getProcessSession();
			//logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "anmeldung");
		}
	}
	
	public void startProcess(UserApplication userApplication) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userApplication", userApplication);
		params.put("service_adminList", new AdminList());
		ksession.startProcess("com.hd11.process.ihu.AnmeldungBearbeiten", params);
		
		System.out.println();
	}
		
	
	public void stopProcess() {
		if (ksession != null) {
			ksession.dispose();
			//logger.close();
		}
	}

	public StatefulKnowledgeSession getProcessSession() {
		// load up the knowledge base
		KnowledgeBase kbase;
		StatefulKnowledgeSession ksession = null;
		try {
			kbase = readKnowledgeBase();
			ksession = kbase.newStatefulKnowledgeSession();
			ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
					new WSHumanTaskHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ksession;
	}

	public KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("IHU_Anmeldung.rf"),
				ResourceType.DRF);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	}
