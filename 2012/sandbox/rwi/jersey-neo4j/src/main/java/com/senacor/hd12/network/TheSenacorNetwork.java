package com.senacor.hd12.network;

import com.senacor.hd12.neo.engine.DepartmentRepository;
import com.senacor.hd12.neo.engine.NetworkRepositoryImpl;
import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.network.routes.DumpAllRoute;
import com.senacor.hd12.network.routes.EmploymentRoute;
import com.senacor.hd12.network.routes.StaticContentRoute;
import com.senacor.hd12.network.routes.VisualizerRoute;
import com.senacor.hd12.network.util.Populator;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Ralph Winzinger, Senacor
 */
@Service
public class TheSenacorNetwork {
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private Populator populator;

    public void dumpNetwork() {
        Person klLoaded = personRepo.findByPropertyValue("firstname", "Katharina");
        System.out.println(klLoaded.toString());
    }

    public void startNetworkBuilding() {
        populator.populateIfNecessary(System.getProperty("forceClear", "false").equals("true"));

        /*
        // install routes
        post(employmentRoute);
        get(dumpAllRoute);
        get(visualizerRoute);
        get(staticContentRoute);

        before(new Filter() { // matches all routes
            @Override
            public void handle(Request request, Response response) {
                System.err.println("requesting: "+request.pathInfo());
            }
        });
        
        after(new Filter() {
            @Override
            public void handle(Request request, Response response) {

                System.err.println(request.pathInfo()+" -> "+response.raw().toString());
            }
        });
        */

        /*
        post(new Route("/introduction") {
            @Override
            public Object handle(Request request, Response response) {
                String firstname = request.queryParams("firstname");
                String lastname = request.queryParams("lastname");

                persons.employ(firstname, lastname);

                return "done.";
            }
        });
        */

    }
}
