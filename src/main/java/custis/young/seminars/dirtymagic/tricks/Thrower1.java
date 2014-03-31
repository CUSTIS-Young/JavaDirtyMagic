package custis.young.seminars.dirtymagic.tricks;

public class Thrower1 {

    private static Throwable t;

    private Thrower1() throws Throwable {
        throw t;
    }

    public static synchronized void sneakyThrow(Throwable t) {
        Thrower1.t = t;
        try {
            Thrower1.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            Thrower1.t = null; // Avoid memory leak
        }
    }
}
