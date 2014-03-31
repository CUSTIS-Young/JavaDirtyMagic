package custis.young.seminars.dirtymagic.reflection;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

class CodeGen {
    private CodeGen() {}

    public static byte[] definePublicClass(String name, Class<?> superType, Class<?> iface, ClassBody classBody) throws CodeGenException {
        return definePublicClass(name, superType, new Class<?>[]{iface}, classBody);
    }

    public static byte[] definePublicClass(String name, Class<?> superType, Class<?>[] interfaces, ClassBody classBody) throws CodeGenException {

        final String superTypeName = Type.getType(superType).getInternalName();
        final String[] interfaceNames = toNames(interfaces);

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);

        cw.visit(V1_7, ACC_PUBLIC, name.replaceAll("[.]", "/"), null, superTypeName, interfaceNames);
        classBody.writeTo(cw);
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static abstract class ClassBody {
        protected ClassBody() {}

        private ClassWriter cw;

        private void writeTo(ClassWriter classWriter) throws CodeGenException {
            this.cw = classWriter;
            try {
                write();
            } catch (Exception x) {
                throw new CodeGenException(x);
            }
            this.cw = null;
        }

        protected abstract void write() throws Exception;

        protected void definePublicNoArgConstructor(ByteCode constructorBody) {

            final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            constructorBody.writeTo(mv);
            mv.visitMaxs(0, 0); // ignored, since we use COMPUTE_MAXS + COMPUTE_FRAMES options
            mv.visitEnd();
        }

        protected void definePublicMethod(Method method, ByteCode methodBody) {

            final String descriptor = Type.getType(method).getDescriptor();
            final String[] exceptionNames = toNames(method.getExceptionTypes());

            final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, method.getName(), descriptor, null, exceptionNames);
            mv.visitCode();
            methodBody.writeTo(mv);
            mv.visitMaxs(0, 0); // ignored, since we use COMPUTE_MAXS + COMPUTE_FRAMES options
            mv.visitEnd();
        }
    }

    public static abstract class ByteCode {

        private MethodVisitor mv;

        private void writeTo(MethodVisitor methodVisitor) throws CodeGenException {
            this.mv = methodVisitor;
            try {
                write();
            } catch (Exception x) {
                throw new CodeGenException(x);
            }
            this.mv = null;
        }

        protected abstract void write() throws Exception;

        // --------------------------------------------------------------------

        /**
         * Push object reference in local variable {@code varNum} onto the stack
         */
        protected void OBJECT_LOAD(int varNum) {
            mv.visitVarInsn(ALOAD, varNum);
        }

        // --------------------------------------------------------------------

        protected void CONSTANT(String str, Object... args) {
            mv.visitLdcInsn(String.format(str, args));
        }

        // --------------------------------------------------------------------

        protected void CHECKED_CAST(Class<?> toType) {
            final String toTypeInternalName = Type.getType(toType).getInternalName();
            mv.visitTypeInsn(CHECKCAST, toTypeInternalName);
        }

        protected void BOX(Class<?> primitiveType) {
            final Type wrapperType = Type.getType(Primitives.getWrapperClass(primitiveType));
            final String primitiveDescriptor = Type.getType(primitiveType).getDescriptor();
            mv.visitMethodInsn(INVOKESTATIC, wrapperType.getInternalName(), "valueOf", "(" + primitiveDescriptor + ")" + wrapperType.getDescriptor());
        }

        protected void UNBOX(Class<?> primitiveType) {
            final String wrapperInternalName = Type.getType(Primitives.getWrapperClass(primitiveType)).getInternalName();
            final String primitiveDescriptor = Type.getType(primitiveType).getDescriptor();
            mv.visitTypeInsn(CHECKCAST, wrapperInternalName);
            mv.visitMethodInsn(INVOKEVIRTUAL, wrapperInternalName, primitiveType.getSimpleName() + "Value", "()" + primitiveDescriptor);
        }

        // --------------------------------------------------------------------

        protected void GET_FIELD(Field field) {
            final String owner = Type.getType(field.getDeclaringClass()).getInternalName();
            final String descriptor = Type.getType(field.getType()).getDescriptor();
            mv.visitFieldInsn(GETFIELD, owner, field.getName(), descriptor);
        }

        protected void PUT_FIELD(Field field) {
            final String owner = Type.getType(field.getDeclaringClass()).getInternalName();
            final String descriptor = Type.getType(field.getType()).getDescriptor();
            mv.visitFieldInsn(PUTFIELD, owner, field.getName(), descriptor);
        }

        // --------------------------------------------------------------------

        protected void INVOKE(Constructor<?> constructor) {
            final String typeName = Type.getType(constructor.getDeclaringClass()).getInternalName();
            mv.visitMethodInsn(INVOKESPECIAL, typeName, "<init>", Type.getType(constructor).getDescriptor());
        }

        // --------------------------------------------------------------------

        protected void VOID_RETURN() {
            mv.visitInsn(RETURN);
        }

        protected void OBJECT_RETURN() {
            mv.visitInsn(ARETURN);
        }

        // --------------------------------------------------------------------

    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static String[] toNames(Class<?>[] types) {
        if (types.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            final Class type = types[i];
            names[i] = Type.getType(type).getInternalName();
        }
        return names;
    }
}
