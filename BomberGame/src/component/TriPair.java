package component;

public class TriPair<F, S, T> extends Pair<F, S>{
    public T t;
    public TriPair(F first, S second, T t) {
        super(first, second);
        this.t = t;
    }

    public TriPair(Pair<F, S> p, T t) {
        super(p.f, p.s);
        this.t = t;
    }

    @Override
    public String toString() {
        return "TriPair{" + f + ", " + s + ", " + t + '}';
    }
}
