package custis.young.seminars.dirtymagic.reflection;

class Primitives {
    private Primitives() {} // static use only

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getWrapperClass(Class<T> type) {
        return (Class<T>) findPrm(type).getWrapperClass();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDefaultValue(Class<T> type) {
        return (T) findPrm(type).getDefaultValue();
    }

    private static Prm findPrm(Class<?> type) {
        for (Prm p : Prm.VALUES) {
            if (p.type == type) {
                return p;
            }
        }
        throw new IllegalArgumentException(
            (type != null) ? "Not a primitive: " + type
                           : "Parameter 'type' must not be null");
    }

    private static enum Prm {
        BOOLEAN(boolean.class, Boolean.class, false),
        CHAR(char.class, Character.class, '\u0000'),
        BYTE(byte.class, Byte.class, (byte) 0),
        SHORT(short.class, Short.class, (short) 0),
        INT(int.class, Integer.class, 0),
        LONG(long.class, Long.class, 0L),
        FLOAT(float.class, Float.class, 0f),
        DOUBLE(double.class, Double.class, 0D),
        VOID(void.class, Void.class, null);

        private static final Prm[] VALUES = Prm.values();

        private final Class<?> type;
        private final Class<?> wrapperClass;
        private final Object defaultValue;

        private Prm(Class<?> type, Class<?> wrapperClass, Object defaultValue) {
            this.type = type;
            this.wrapperClass = wrapperClass;
            this.defaultValue = defaultValue;
        }

        public Class<?> getType() {
            return type;
        }

        public Class<?> getWrapperClass() {
            return wrapperClass;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }
    }
}
