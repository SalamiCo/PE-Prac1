package pe1314.g11.pr3;

public interface LispValue extends Comparable<LispValue> {
    
    public abstract int depth ();
    public abstract int nodes ();
    public abstract int expressions ();
}
