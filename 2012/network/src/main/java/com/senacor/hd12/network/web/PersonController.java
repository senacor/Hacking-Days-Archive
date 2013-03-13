package com.senacor.hd12.network.web;

import com.google.common.base.Preconditions;
import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.engine.RelationshipRepository;
import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.visualisation.Visualisation;
import com.senacor.hd12.neo.service.NetworkService;
import com.senacor.hd12.neo.service.VisualisationService;
import org.neo4j.helpers.Function;
import org.neo4j.kernel.impl.core.NodeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.neo4j.helpers.collection.Iterables.map;
import static org.neo4j.helpers.collection.Iterables.toList;

@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    RelationshipRepository relationshipRepository;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private VisualisationService visualisationService;

    @RequestMapping(value="/{username}/greeting", method = RequestMethod.GET)
    public HttpEntity<String> getDaten(@PathVariable("username") String username) {
        return new HttpEntity<String>("Hallo " + username);
    }

    @RequestMapping(value="/{username}")
    public HttpEntity<Person> getPerson(@PathVariable("username") String username) {
        Person person = personRepository.getPersonByUsername(username);
        if(person == null) {
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        }

        return new HttpEntity<Person>(person);
    }

    @RequestMapping(value="/{username}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void putPerson(@PathVariable("username") String username, @RequestBody Person person) {
        Preconditions.checkArgument(username.equals(person.getUsername()));
        personRepository.saveOrReplacePerson(person);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<Person> putPerson(@RequestParam("username") String username,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname) {
        Person person = new Person(firstname, lastname, username);
        personRepository.save(person);
        return new HttpEntity<Person>(person);
    }

    @RequestMapping(value="/{username}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePerson(@PathVariable("username") String username) {
        personRepository.deleteUser(username);
    }

    @RequestMapping(value="/{username}/peers")
    public HttpEntity<List<PersonDTO>> getPeers(@PathVariable("username") String username) {
        EndResult peers = personRepository.findPeersExt(username);
        List<PersonDTO> peerUsernames = transformToPersonDTO(peers);
        return new HttpEntity<List<PersonDTO>>(peerUsernames);
    }

    @RequestMapping(value="/topTen")
    public HttpEntity<List<PersonDTO>> getTopTen() {
        EndResult peers = personRepository.getTopTen();
        List<PersonDTO> peerUsernames = transformToPersonDTO(peers);
        return new HttpEntity<List<PersonDTO>>(peerUsernames);
    }

    private List<PersonDTO> transformToPersonDTO(EndResult result) {
    	List<PersonDTO> ret = new ArrayList<PersonDTO>();
        for (Object row : result) {
        	Map<String,Object> map = (Map<String,Object>)row;
//            System.out.println(map.keySet().toArray());
        	NodeProxy node = (NodeProxy)map.get("poi");
        	ret.add(new PersonDTO(
        			getProperty(node, "username"),
        			getProperty(node, "avatar"),
        			(Long)map.get("friendcount")
        			));
        }
        return ret;
    }
    
    private String getProperty(NodeProxy node, String field) {
    	if (!node.hasProperty(field))
    		return null;
    			
		return (String)node.getProperty(field);
	}

	private List<String> transformToUsernameList(Iterable<Person> peers) {
        return toList(map(new Function<Person, String>() {
            @Override
            public String map(Person person) {
                return person.getUsername();
            }
        }, peers));
    }

    @RequestMapping(value="/{username}/follows")
    public HttpEntity<List<String>> getFollows(@PathVariable("username") String username) {
        Iterable<Person> peers = personRepository.findPeers(username);
        List<String> peerUsernames = transformToUsernameList(peers);
        return new HttpEntity<List<String>>(peerUsernames);
    }

    @RequestMapping(value="/{username}/peers/{peerUsername}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void addPeer(@PathVariable("username") String username,
            @PathVariable("peerUsername") String peerUsername) {
        personRepository.createRelationship(username, peerUsername);
    }

    @RequestMapping(value="/{username}/peers/{peerUsername}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePeer(@PathVariable("username") String username,
            @PathVariable("peerUsername") String peerUsername) {
        relationshipRepository.deleteRelationships(username, peerUsername);
    }

    @RequestMapping(value="/{username}/followers")
    public HttpEntity<List<String>> getFollowers(@PathVariable("username") String username) {
        Iterable<Person> peers = personRepository.findFollowers(username);
        List<String> peerUsernames = transformToUsernameList(peers);
        return new HttpEntity<List<String>>(peerUsernames);
    }

    @RequestMapping(value="/{username}/suggest")
    public HttpEntity<List<String>> suggestUnknowns(@PathVariable("username") String username) {
        return new HttpEntity<List<String>>(personRepository.suggestUnknowns(username));
    }

    @RequestMapping(value="")
    public HttpEntity<List<String>> getUserList() {
        Iterable<Person> users = personRepository.findAll();
        List<String> usernames = transformToUsernameList(users);
        return new HttpEntity<List<String>>(usernames);
    }

    @RequestMapping(value="/{username}/organisation/{organisationName}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setOrganisation(@PathVariable("username") String username,
                        @PathVariable("organisationName") String organisationName) {
        networkService.setOrganisation(username, organisationName);
    }

    @RequestMapping(value = "/_search")
    public HttpEntity<List<String>> findUsers(@RequestParam("lastname.startswith") String lastname) {
        Iterable<Person> users = personRepository.findPersonsWithLastnameStarting(lastname);
        List<String> usernames = transformToUsernameList(users);
        return new HttpEntity<List<String>>(usernames);
    }

    @RequestMapping(value = "/{username}/_visualisation")
    public HttpEntity<Visualisation> getVisualisation(@PathVariable("username") String username,
            @RequestParam("depth") int depth) {
        Visualisation visualisation = visualisationService.getVisualisation(username, depth);
        return new HttpEntity<Visualisation>(visualisation);
    }

}
