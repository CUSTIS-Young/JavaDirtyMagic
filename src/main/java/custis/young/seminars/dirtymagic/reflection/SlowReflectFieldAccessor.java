package custis.young.seminars.dirtymagic.reflection;

import java.lang.reflect.Field;

public class SlowReflectFieldAccessor implements FieldAccessor {

    private final String fieldName;

    public SlowReflectFieldAccessor(Field field) {
        this.fieldName = field.getName();
    }

    @Override
    public Object get(Object target) {
        try {
            final Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException x) {
            throw new RuntimeException("IllegalAccessException", x);
        } catch (NoSuchFieldException x) {
            throw new RuntimeException("NoSuchFieldException", x);
        }
    }
}
