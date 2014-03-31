package custis.young.seminars.dirtymagic.reflection;

import java.lang.reflect.Field;

public class ReflectFieldAccessorFactory implements FieldAccessorFactory {

    @Override
    public FieldAccessor createFieldAccessor(Field field) {
        return new ReflectFieldAccessor(field);
    }

    private class ReflectFieldAccessor implements FieldAccessor {

        private final Field field;

        public ReflectFieldAccessor(Field field) {
            this.field = field;
            field.setAccessible(true);
        }

        @Override
        public Object get(Object target) {
            try {
                return field.get(target);
            } catch (IllegalAccessException x) {
                throw new RuntimeException("IllegalAccessException", x);
            }
        }
    }
}
