package com.senacor.hd12.network.web;

import com.google.common.base.Preconditions;
import com.senacor.hd12.neo.engine.OrganisationRepository;
import com.senacor.hd12.neo.model.Organisation;
import org.neo4j.helpers.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.neo4j.helpers.collection.Iterables.map;
import static org.neo4j.helpers.collection.Iterables.toList;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 11:59
 */
@Controller
@RequestMapping("/organisation")
public class OrganisationController {

    @Autowired
    private OrganisationRepository organisationRepository;

    @RequestMapping(value = "")
    public HttpEntity<List<String>> getOrganisationList() {
        Iterable<Organisation> organisations = organisationRepository.findAll();
        List<String> organisationList = toList(map(new Function<Organisation, String>() {
            @Override
            public String map(Organisation o) {
                return o.getName();
            }
        }, organisations));
        return new HttpEntity<List<String>>(organisationList);
    }

    @RequestMapping(value = "/{name}")
    public HttpEntity<Organisation> getOrganisation(@PathVariable("name") String name) {
        Organisation organisation = organisationRepository.getOrganisationByName(name);
        if (organisation == null) {
            return new ResponseEntity<Organisation>(HttpStatus.NOT_FOUND);
        }

        return new HttpEntity<Organisation>(organisation);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void putOrganisation(@PathVariable("name") String name,
                                @RequestBody Organisation organisation) {
        Preconditions.checkArgument(name.equals(organisation.getName()));
        organisationRepository.save(organisation);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<Organisation> putOrganisation(@RequestParam("name") String name,
                                              @RequestParam("ort") String ort) {
        Organisation organisation = new Organisation(name)
                .setOrt(ort);
        organisation = organisationRepository.save(organisation);
        return new HttpEntity<Organisation>(organisation);
    }

    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePerson(@PathVariable("name") String name) {
        organisationRepository.deleteOrganisation(name);
    }
}
