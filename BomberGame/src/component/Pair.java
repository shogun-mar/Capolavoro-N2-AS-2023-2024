package component;

public class Pair<F, S>{
    public F f;
    public S s;

    public Pair(F first, S second){
        this.f = first;
        this.s = second;
    }

    @Override
    public String toString() {
        return "Pair{" + f + ", " + s +"}";
    }
}