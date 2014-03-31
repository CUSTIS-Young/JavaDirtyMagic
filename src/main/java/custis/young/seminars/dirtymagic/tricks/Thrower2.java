package custis.young.seminars.dirtymagic.tricks;

public class Thrower2 {

    public static void sneakyThrow(Throwable t) {
        Thread.currentThread().stop(t);
    }
}
