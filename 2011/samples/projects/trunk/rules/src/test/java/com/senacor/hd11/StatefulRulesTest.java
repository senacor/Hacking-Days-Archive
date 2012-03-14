package com.senacor.hd11;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatefulRulesTest {
    private StatefulKnowledgeSession ksession = null;

    @Before
    public void setup() {
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("/rules/SimpleRules.drl", getClass()), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            fail(kbuilder.getErrors().toString());
        }
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        ksession = kbase.newStatefulKnowledgeSession();
    }

    @After
    public void tearDown() {
        System.out.println("----------------------");
    }

    @Test
    public void testInvalidAge() {
        Person p = new Person("Manuela", "Minderjährig", 17);

        ksession.insert(p);
        ksession.fireAllRules();

        assertFalse("rule was wrong", p.isValid());
    }

    @Test
    public void testValidAge() {
        Person p = new Person("Volker", "Volljährig", 19);

        ksession.insert(p);
        ksession.fireAllRules();

        assertTrue("rule was wrong", p.isValid());
    }

    @Test
    public void testManualBirthday() {
        Person p = new Person("Manuela", "Minderjährig", 17);

        FactHandle pHandle = ksession.insert(p);
        ksession.fireAllRules();
        assertFalse("rule was wrong", p.isValid());

        p.setAge(18);
        ksession.fireAllRules();
        assertFalse("rule was wrong", p.isValid());

        ksession.update(pHandle, p);
        ksession.fireAllRules();
        assertTrue("rule was wrong", p.isValid());
    }

    @Test
    public void testAutomaticBirthday() {
        Person p = new Person("Manuela", "Minderjährig", 17);

        ksession.insert(p);
        ksession.fireAllRules();
        assertFalse("rule was wrong", p.isValid());

        Birthday b = new Birthday(p);
        ksession.insert(b);
        ksession.fireAllRules();
        assertTrue("rule was wrong", p.isValid());
    }
}
