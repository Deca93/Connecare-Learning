package model;

/**
 * Created by Andrea De Castri on 09/11/2017.
 *
 */
public class Pair<S,T> {

    private S first;
    private T second;

    public Pair(S first, T second){
        super();
        this.first = first;
        this.second = second;
    }

    public S getFirst(){
        return this.first;
    }

    public T getSecond(){
        return this.second;
    }

}
