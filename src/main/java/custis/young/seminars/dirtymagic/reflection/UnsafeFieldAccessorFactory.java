package custis.young.seminars.dirtymagic.reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnsafeFieldAccessorFactory implements FieldAccessorFactory {

    @Override
    public FieldAccessor createFieldAccessor(final Field field) {

        final boolean fieldIsVolatile = Modifier.isVolatile(field.getModifiers());
        final Class<?> fieldType = field.getType();

        final Unsafe unsafe = StolenUnsafe.getUnsafe();
        final long offset = unsafe.objectFieldOffset(field);

        return new FieldAccessor() {

            @Override
            public Object get(Object target) {
                if (fieldType.isPrimitive()) {
                    throw new UnsupportedOperationException("for fieldType = " + fieldType);
                } else {
                    if (fieldIsVolatile) {
                        return unsafe.getObjectVolatile(target, offset);
                    } else {
                        return unsafe.getObject(target, offset);
                    }
                }
            }
        };
    }
}
