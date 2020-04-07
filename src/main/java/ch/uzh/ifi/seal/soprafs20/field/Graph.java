package ch.uzh.ifi.seal.soprafs20.field;

import java.util.*;

public class Graph {
    private HashMap<Field, List<Field>> adjVertices = new HashMap<>();

    void addField(Field field){
        adjVertices.putIfAbsent(field, new ArrayList<Field>());
    }

    void removeField(Field field){
        adjVertices.values().stream().forEach(e->e.remove(field));
        adjVertices.remove(field);
    }

    void addEdge(Field field1, Field field2){
        adjVertices.get(field1).add(field2);
    }

    void removeEdge(Field field1, Field field2){
        List<Field> eV1 = adjVertices.get(field1);
        if (eV1 != null){
            eV1.remove(field2);
        }
    }

    ArrayList<Field> sortById(ArrayList<Field> fields){
        ArrayList<Field> sortedFields = new ArrayList<>();
        long id=1;
        while(sortedFields.size()!=fields.size()) {
            for (Field field : fields) {
                if (field.id == id) {
                    sortedFields.add(field);
                }
            }
        }
        return sortedFields;
    }

    public Graph createGraph(ArrayList<Field> fields){
        Graph graph = new Graph();
        ArrayList<Field> sortedFields = sortById(fields);

        for(int id=0; id<63;id++){
            graph.addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            graph.addEdge(first,second);
        }

        for(int id=64; id<67;id++){
            graph.addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            graph.addEdge(first,second);
        }
        for(int id=68; id<71;id++){
            graph.addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            graph.addEdge(first,second);
        }
        for(int id=72; id<75;id++){
            graph.addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            graph.addEdge(first,second);
        }
        for(int id=76; id<80;id++){
            graph.addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            graph.addEdge(first,second);
        }

        graph.addField(sortedFields.get(63));
        graph.addField(sortedFields.get(67));
        graph.addField(sortedFields.get(71));
        graph.addField(sortedFields.get(75));
        graph.addField(sortedFields.get(79));
        graph.addEdge(sortedFields.get(63), sortedFields.get(0));
        graph.addEdge(sortedFields.get(0),sortedFields.get(64));
        graph.addEdge(sortedFields.get(16),sortedFields.get(68));
        graph.addEdge(sortedFields.get(32),sortedFields.get(72));
        graph.addEdge(sortedFields.get(48),sortedFields.get(76));

        return graph;
    }
}
