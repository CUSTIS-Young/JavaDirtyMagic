package custis.young.seminars.dirtymagic.reflection;

import java.lang.reflect.Field;

public interface FieldAccessorFactory {

    FieldAccessor createFieldAccessor(Field field);
}
