public class Bundle<A,B, C> {
    public final A a;
    public final B b;
    public final C c;

    public Bundle(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Bundle(A a, B b) {
        this.a = a;
        this.b = b;
        this.c = null;
    }
}
