package com.senacor.hd11;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatelessRulesTest {
    private StatelessKnowledgeSession ksession = null;

    @Before
    public void setup() {
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("/rules/SimpleRules.drl", getClass()), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            fail(kbuilder.getErrors().toString());
        }
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        ksession = kbase.newStatelessKnowledgeSession();
    }

    @After
    public void tearDown() {
        System.out.println("----------------------");
    }

    @Test
    public void testInvalidAge() {
        Person p = new Person("Manuela", "Minderjährig", 17);

        ksession.execute(p);

        assertFalse("rule did not recognize invalid age", p.isValid());
    }

    @Test
    public void testValidAge() {
        Person p = new Person("Volker", "Volljährig", 19);

        ksession.execute(p);

        assertTrue("rule did not recognize valid age", p.isValid());
    }
}
