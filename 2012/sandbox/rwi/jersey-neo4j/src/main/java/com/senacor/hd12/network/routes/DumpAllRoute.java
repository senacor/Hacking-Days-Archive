package com.senacor.hd12.network.routes;

import com.senacor.hd12.neo.engine.ClientServiceTeamRepository;
import com.senacor.hd12.neo.engine.DepartmentRepository;
import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.ClientServiceTeam;
import com.senacor.hd12.neo.model.Department;
import com.senacor.hd12.neo.model.Person;
import org.neo4j.helpers.collection.ClosableIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public class DumpAllRoute extends BaseRoute {
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private DepartmentRepository departmentRepo;
    @Autowired
    private ClientServiceTeamRepository cstRepo;

    public DumpAllRoute() {
        super("/dump");
    }

    /*
    @Override
    public Object handle(Request request, Response response) {
        ClosableIterable<Person> persons = personRepo.findAll();
        for (Person person: persons) {
            System.out.println(person);
        }

        ClosableIterable<Department> departments = departmentRepo.findAll();
        for (Department department: departments) {
            System.out.println(department);
        }

        ClosableIterable<ClientServiceTeam> csts = cstRepo.findAll();
        for (ClientServiceTeam cst: csts) {
            System.out.println(cst);
        }

        response.status(200);
        return response;
    }
    */
}
