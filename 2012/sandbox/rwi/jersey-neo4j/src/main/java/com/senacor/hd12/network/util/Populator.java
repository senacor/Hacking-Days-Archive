package com.senacor.hd12.network.util;

import com.senacor.hd12.neo.engine.ClientServiceTeamRepository;
import com.senacor.hd12.neo.engine.DepartmentRepository;
import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.ClientServiceTeam;
import com.senacor.hd12.neo.model.Department;
import com.senacor.hd12.neo.model.Person;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.aspects.core.NodeBacked;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public class Populator {
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private DepartmentRepository departmentRepo;
    @Autowired
    private ClientServiceTeamRepository cstRepo;
    @Autowired
    private Neo4jOperations template;
    @Autowired
    private GraphDatabase graphDatabase;

    // @Transactional
    public void populateIfNecessary(boolean forceRepopulate) {

        Department dept = template.lookup(Department.class,"function","HR").to(Department.class).singleOrNull();

        if (dept == null || forceRepopulate) {
            // delete everything else (if present)
            System.out.println("clear repositories ...");

            clearRepositories();
            // Transaction tx = graphDatabase.beginTx();
            initializeCompany();
            // tx.success();
        } else {
            System.out.println("HR was present - nothing done");
        }
    }

    public void clearRepositories() {
        personRepo.deleteAll();
        departmentRepo.deleteAll();
        cstRepo.deleteAll();
    }

    private void initializeCompany() {
        Person kl = personRepo.save(new Person("Katharina", "Landes"));
        Person mt = personRepo.save(new Person("Matthias", "Tomann"));
        Person mp = personRepo.save(new Person("Marcus", "Purzer"));
        Person rw = personRepo.save(new Person("Ralph", "Winzinger"));

        Department mgmt = departmentRepo.save(new Department("mgmt"));
        Department hr = departmentRepo.save(new Department("hr"));
        Department pspool = departmentRepo.save(new Department("pspool"));

        ClientServiceTeam pbs = cstRepo.save(new ClientServiceTeam("PBS"));
        ClientServiceTeam hre = cstRepo.save(new ClientServiceTeam("HRE"));

        mgmt.affilate(mt);
        mgmt.affilate(mp);
        hr.affilate(kl);
        pspool.affilate(rw);

        pbs.affilate(rw);

        kl.isIntroducedTo(mt);
        kl.isIntroducedTo(mp);

        rw.isIntroducedTo(kl);

        System.out.println("company initialized");

        dump();

        Person klLoaded = personRepo.findByPropertyValue("firstname", "Katharina");
        System.out.println(klLoaded.toString());
        System.out.println("kl nodeBacked?     : "+(klLoaded instanceof NodeBacked));
        System.out.println("kl node?           : "+(klLoaded instanceof Node));
        System.out.println("kl.persState node? : "+(((NodeBacked)klLoaded).getPersistentState() instanceof Node));
    }

    private void dump() {
        Iterable<Person> persons = personRepo.findAll();
        for (Person person: persons) {
            System.out.println(person);
        }

        Iterable<Department> departments = departmentRepo.findAll();
        for (Department department: departments) {
            System.out.println(department);
        }

        Iterable<ClientServiceTeam> csts = cstRepo.findAll();
        for (ClientServiceTeam cst: csts) {
            System.out.println(cst);
        }

    }

}
