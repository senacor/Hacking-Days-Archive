package com.senacor.hd12.neo;

import com.senacor.hd12.neo.engine.NetworkRepositoryImpl;
import com.senacor.hd12.neo.model.Person;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Ralph Winzinger, Senacor
 */
public class NeoTest {
    private ConfigurableApplicationContext applicationContext = null;
    private NetworkRepositoryImpl senacor = null;

    public static void main(String[] args) {
        NeoTest nt = new NeoTest();
        nt.showOffNeo();
    }

    public void showOffNeo() {
        applicationContext = new ClassPathXmlApplicationContext("/src/main/webapp/WEB-INF/spring/neoContext.xml");
        senacor = applicationContext.getBean(NetworkRepositoryImpl.class);

        try {
            senacor.initializeCompany();

            Person rw = senacor.employ("Ralph", "Winzinger");
            showColleagues(rw);

            Person ss = senacor.employ("Silvia", "Scheurich");
            showColleagues(rw);

            rw.isIntroducedTo(ss);
            showColleagues(rw);
        } finally {
            applicationContext.close();
        }
    }

    private void showColleagues(Person person) {
        System.out.println("colleagues for "+person);
        Iterable<Person> colleagues = senacor.findColleagues(person);
        for (Person colleague : colleagues) {
            if (person.equals(colleague)) {
                continue;
            }
            System.out.println("-> " + colleague);
        }
    }
}
