package custis.young.seminars.dirtymagic.reflection;

import sun.misc.Unsafe;

public class UnsafeJvmCrash {

    public static void main(String[] args) throws Exception {

        final Unsafe unsafe = StolenUnsafe.getUnsafe();

        final Object obj = new Object();

        for (int i = 0; i < 8; i++) {
            unsafe.putInt(obj, (long) i, 23);
        }

        obj.toString();
    }
}
