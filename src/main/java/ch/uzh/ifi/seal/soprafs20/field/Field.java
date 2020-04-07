package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.User.Figure;

public abstract class Field {

    private Figure occupant;
    protected long id;

    public Figure getOccupant() {
        return occupant;
    }

    public void setOccupant(Figure occupant){
        return;
    }

    public long getId(){return id;};

    public boolean equals(Object v){
        if (v==this){
            return true;
        }
        if(!(v instanceof Field)){
            return false;
        }

        Field v2 = (Field) v;

        return id == v2.id;
    }

    public int hashCode() {
        int result = 17;
        result = (int) (31 * result + id);
        return result;
    }
}
