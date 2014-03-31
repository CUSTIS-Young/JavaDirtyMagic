package custis.young.seminars.dirtymagic.tricks;

public class Thrower3<T extends Throwable> {

    public static void sneakyThrow(Throwable t) {
        new Thrower3<Error>().sneakyThrow2(t);
    }

    private void sneakyThrow2(Throwable t) throws T {
        throw (T) t;
    }
}

