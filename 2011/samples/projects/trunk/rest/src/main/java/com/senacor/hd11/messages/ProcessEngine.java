package com.senacor.hd11.messages;

import com.senacor.hd11.model.Message;
import com.senacor.hd11.model.UserApplication;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.process.workitem.wsht.WSHumanTaskHandler;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * Ralph Winzinger, Senacor
 */
public class ProcessEngine {
    private static ProcessEngine ourInstance = new ProcessEngine();

    private StatefulKnowledgeSession ksession;
    private FactHandle strikesHandle = null;
    private FactHandle niceMessageHandle = null;

    public static ProcessEngine getInstance() {
        return ourInstance;
    }

    private ProcessEngine() {
        try {
            KnowledgeBase kbase = readKnowledgeBase();
            ksession = kbase.newStatefulKnowledgeSession();
            ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new WSHumanTaskHandler());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("rules/CheckMessage.drl", getClass()), ResourceType.DRL);

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

    public Message checkMessage(Message message) {
        FactHandle mHandle = ksession.insert(message);
        ksession.fireAllRules();

        return (Message) ksession.getObject(mHandle);
    }

    public void insertStrikesRule(Strikes strikes) {
        if (strikesHandle != null) {
            ksession.update(strikesHandle, strikes);
        } else {
            strikesHandle = ksession.insert(strikes);
        }
    }

    public void insertNiceText(NiceMessage niceMessage) {
        if (niceMessageHandle != null) {
            ksession.update(niceMessageHandle, niceMessage);
        } else {
            niceMessageHandle = ksession.insert(niceMessage);
        }
    }
}
