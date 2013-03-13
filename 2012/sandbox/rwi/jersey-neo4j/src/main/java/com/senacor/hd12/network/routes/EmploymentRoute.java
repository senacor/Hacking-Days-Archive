package com.senacor.hd12.network.routes;

import com.senacor.hd12.neo.model.Person;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public class EmploymentRoute extends BaseRoute {

    public EmploymentRoute() {
        super("/employment");
    }

    /*
    @Override
    public Object handle(Request request, Response response) {
        Person person = null;
        try {
            person = jsonMapper.readValue(request.body(), Person.class);
        } catch (IOException e) {
            halt(501, "JSON-parser failed: "+e.getMessage());
        }

        if (person == null) {
            halt(400, "no person data");
        } else if (person.getId() != null) {
            halt(409, "person "+person.getId()+" already exists");
        }

        try {
            person = senacor.employ(person);
        } catch (Throwable t) {
            t.printStackTrace();
            halt(501, t.getMessage());
        }
        response.status(201);
        response.header("Location", "/employee/"+person.getId());
        try {
            response.body(jsonMapper.writeValueAsString(person));
        } catch (IOException e) {
            halt(501, "JSON-formatter failed: "+e.getMessage());
        }
        return response;
    }
    */
}
