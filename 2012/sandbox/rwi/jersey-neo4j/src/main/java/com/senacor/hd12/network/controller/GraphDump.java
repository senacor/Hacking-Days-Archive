package com.senacor.hd12.network.controller;

import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.network.util.Populator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.Set;

/**
 * Ralph Winzinger, Senacor
 */
@Controller
public class GraphDump {
    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private Populator populator;

    @RequestMapping(value = "/populate", produces="application/json")
    @ResponseBody
    public String popuplate() {
        populator.populateIfNecessary(false);
        return "{\"population\":\"done\"}";
    }


    /**
     * JSON Beispiel
     *
     * Senacor :=
     * {
     *   "employees":[
     *     {"name":"ralph", "age":38},
     *     {"name":"charly", "age":30}
     *   ],
     *   "departments":[
     *     {"id":"HR", "foo":"bar", "budget":1000},
     *     {"id":"ITI", "foo":"abc", "budget":1250}
     *   ]
     * }
     *
     * http://localhost:8080/jersey-neo4j/spring/dump
     */
    @RequestMapping(value = "/dumpAll", produces="application/json")
    @ResponseBody
    public String dumpNetworkGraph() {
        return "{\"senacor\":}";
    }

    /**
     * (Arbor, http://arborjs.org/reference) JSON Beispiel
     *
     * {
     *   nodes:{
     *     katharina:{'color':'orange','shape':'dot','label':'KL', radius:3},
     *     ralph:{'color':'green','shape':'dot','label':'RW', radius:2},
     *     charly:{'color':'blue','shape':'dot','label':'CTK', radius:1}
     *   },
     *   edges:{
     *     katharina:{ hr:{} },
     *     ralph:{ ps:{} },
     *     charly:{ ps:{} }
     *   }
     * }
     *
     */
    @RequestMapping(value = "/dump", produces="application/json")
    @ResponseBody
    public String dumpNetworkGraphForArbor() {
        System.out.println("incoming call ...");

        StringBuffer handcraftedJson = new StringBuffer();
        Person kl = personRepo.findByPropertyValue("firstname", "Katharina");

        if (kl == null) {
            return "{\"nodes\":{}, \"edges\":{}}";
        }

        handcraftedJson.
                append("{").
                append("  \"nodes\":{");
        // now all nodes
        handcraftedJson.
                append("    \"kl\":{\"label\":\""+kl.getFirstname()+"\"},");
        Set<Person> persons = kl.getKnows();
        Iterator<Person> personIt = persons.iterator();
        while (personIt.hasNext()) {
            Person person = personIt.next();
            String shortName = person.getFirstname().substring(0,1).toLowerCase()+person.getLastname().substring(0,1).toLowerCase();
            handcraftedJson.
                    append("    \"" + shortName + "\":{\"label\":\"" + person.getFirstname() + "\"}");
            if (personIt.hasNext()) {
                handcraftedJson.append(",");
            }
        }
        handcraftedJson.
                append("  },").
                append("  \"edges\":{");
        // now all edges
        handcraftedJson.
                append("    \"kl\":{");
        persons = kl.getKnows();
        personIt = persons.iterator();
        while (personIt.hasNext()) {
            Person person = personIt.next();
            String shortName = person.getFirstname().substring(0,1).toLowerCase()+person.getLastname().substring(0,1).toLowerCase();
            handcraftedJson.
                    append("\""+shortName+"\":{}");
            if (personIt.hasNext()) {
                handcraftedJson.append(",");
            }
        }
        handcraftedJson.
                append("    }");

        handcraftedJson.
                append("  }").
                append("}");

        System.out.println(handcraftedJson.toString());
        return handcraftedJson.toString();
    }

}
