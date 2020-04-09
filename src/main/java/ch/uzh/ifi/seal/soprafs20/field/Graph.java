package ch.uzh.ifi.seal.soprafs20.field;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import org.hibernate.mapping.Value;



import java.util.*;

public class Graph {

    public HashMap<Field, List<Field>> adjVertices;

    public Graph(){
        this.adjVertices = new HashMap<>();
    }

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
                if (field.getId() == id) {
                    sortedFields.add(field);
                    id++;
                }
            }
        }
        return sortedFields;
    }

    List<Field> getAdjFields(Field field){
        return adjVertices.get(field);
    }

    ArrayList<Field> getPossibleFields(Card card, Field field, Graph graph){
        int moveValue = card.getValue().getValue();

        int level = 0;
        Queue<Field> queue = new LinkedList<>();
        queue.add(field);
        queue.add(null);
        while(!queue.isEmpty() && level<moveValue){
            Field temp = queue.poll();
            if(temp==null){
                level++;
                queue.add(null);
            }
            else{
                List<Field> adjFields= graph.getAdjFields(temp);
                for(Field f: adjFields){
                    if(f instanceof FirstField && f.getOccupant()!=null) {

                    }
                    else{
                        queue.add(f);
                    }
                }
            }
        }
        ArrayList<Field> fields = new ArrayList<>();
        while(!queue.isEmpty()){
            if(queue.peek()!=null) {
                fields.add(queue.poll());
            }
            else{
                queue.poll();
            }
        }

        return fields;
    }

    public void createGraph(ArrayList<Field> fields){
        ArrayList<Field> sortedFields = sortById(fields);

        for(int id=0; id<63;id++){
            addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            addEdge(first,second);
        }

        for(int id=64; id<67;id++){
            addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            addEdge(first,second);
        }
        for(int id=68; id<71;id++){
            addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            addEdge(first,second);
        }
        for(int id=72; id<75;id++){
            addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            addEdge(first,second);
        }
        for(int id=76; id<79;id++){
            addField(sortedFields.get(id));
            Field first = sortedFields.get(id);
            Field second = sortedFields.get(id+1);
            addEdge(first,second);
        }

        addField(sortedFields.get(63));
        addField(sortedFields.get(67));
        addField(sortedFields.get(71));
        addField(sortedFields.get(75));
        addField(sortedFields.get(79));
        addEdge(sortedFields.get(63), sortedFields.get(0));
        addEdge(sortedFields.get(0),sortedFields.get(64));
        addEdge(sortedFields.get(16),sortedFields.get(68));
        addEdge(sortedFields.get(32),sortedFields.get(72));
        addEdge(sortedFields.get(48),sortedFields.get(76));
    }
}
