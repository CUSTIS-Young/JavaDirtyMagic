package custis.young.seminars.dirtymagic.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.concurrent.atomic.AtomicLong;

public class CodeGenFieldAccessorFactory implements FieldAccessorFactory {

    @Override
    public FieldAccessor createFieldAccessor(final Field field) {

        final Class superType;
        try {
            superType = Class.forName("sun.reflect.MagicAccessorImpl", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException x) {
            throw new CodeGenException(x);
        }

        final String className = composeName(FieldAccessor.class, field);
        final Class<?> targetType = field.getDeclaringClass();
        final Class<?> fieldType = field.getType();

        final byte[] classBytes = CodeGen.definePublicClass(className, superType, FieldAccessor.class, new CodeGen.ClassBody() {
            @Override protected void write() throws Exception {

                definePublicNoArgConstructor(new CodeGen.ByteCode() {
                    @Override protected void write() throws Exception {
                        OBJECT_LOAD(0);
                        INVOKE(Object.class.getConstructor());
                        VOID_RETURN();
                    }
                });

                definePublicMethod(FieldAccessor.class.getMethod("get", Object.class), new CodeGen.ByteCode() {
                    @Override protected void write() {
                        OBJECT_LOAD(1);                // target
                        CHECKED_CAST(targetType);
                        GET_FIELD(field);              // target.field
                        if (fieldType.isPrimitive()) {
                            BOX(fieldType);            // target.field
                        }
                        OBJECT_RETURN();
                    }
                });

                definePublicMethod(Object.class.getMethod("toString"), new CodeGen.ByteCode() {
                    @Override protected void write() throws Exception {
                        CONSTANT("Generated FieldAccessor for %s", field);
                        OBJECT_RETURN();
                    }
                });
            }
        });

        try {
            return (FieldAccessor) loadClass(className, classBytes, field).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException x) {
            throw new CodeGenException(x);
        }
    }

    private Class<?> loadClass(final String className, final byte[] classBytes, Member origin) throws ClassNotFoundException {

        final ClassLoader parentClassLoaderToUse = origin.getDeclaringClass().getClassLoader();

        final ClassLoader bridgeClassLoader = new ClassLoader(parentClassLoaderToUse) {
            @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
                assert className.equals(name);
                return defineClass(className, classBytes, 0, classBytes.length);
            }
        };

        return bridgeClassLoader.loadClass(className);
    }

    private static AtomicLong counter = new AtomicLong();

    private static String composeName(Class<?> type, Member member) {
        return type.getName()
               + "_of_" + member.getDeclaringClass().getSimpleName() + "_" + member.getName()
               + "_$" + counter.getAndIncrement();
    }
}
