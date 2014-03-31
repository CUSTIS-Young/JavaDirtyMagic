package custis.young.seminars.dirtymagic.reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class StolenUnsafe {
    private StolenUnsafe() {} // static use only

    /**
     * {@link sun.misc.Unsafe#getUnsafe()} has runtime check to be accessed
     * only from system classloader, but we can steal it via reflection
     */

    private static Unsafe unsafe;

    public static Unsafe getUnsafe() {
        if (unsafe == null) {
            unsafe = steal();
        }
        return unsafe;
    }

    private static Unsafe steal() {
        try {
            final Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            try {
                return (Unsafe) theUnsafeField.get(null);
            } finally {
                theUnsafeField.setAccessible(false);
            }
        } catch (Exception x) {
            throw new RuntimeException("Failed to steal sun.misc.Unsafe via reflection", x);
        }
    }
}
