package custis.young.seminars.dirtymagic.tricks;

import custis.young.seminars.dirtymagic.reflection.StolenUnsafe;

public class Thrower4 {

    public static void sneakyThrow(Throwable t) {
        StolenUnsafe.getUnsafe().throwException(new Exception());
    }
}

