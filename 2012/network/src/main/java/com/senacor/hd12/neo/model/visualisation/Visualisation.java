package com.senacor.hd12.neo.model.visualisation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.senacor.hd12.neo.model.Person;

public class Visualisation {
    private Map<String, Node> nodes = new HashMap<String, Node>();
    private Map<String, Map<String, Length>> edges = new HashMap<String, Map<String, Length>>();

    public void addEdges(Person p, Set<Person> peers) {

        if(!p.getRelationships().isEmpty()) {
            edges.put(p.getUsername(), new HashMap<String, Length>());
            for (Person peer : peers) {
                edges.get(p.getUsername()).put(peer.getUsername(), new Length(3));
            }
        }
    }

    public void addPerson(Person p, String color) {
        nodes.put(p.getUsername(), new Node(color, "dot", p.getUsername()));
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public Map<String, Map<String, Length>> getEdges() {
        return edges;
    }
}
