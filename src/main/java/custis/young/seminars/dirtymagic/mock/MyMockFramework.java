package custis.young.seminars.dirtymagic.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MyMockFramework {

    private static ThreadLocal<MethodCall> lastMethodCallThreadLocal = new ThreadLocal<>();

    @SuppressWarnings("unchecked")
    public static <T> T mock(Class<T> iface) {

        final ClassLoader classLoaderToUse = iface.getClassLoader();

        final Object proxy = Proxy.newProxyInstance(classLoaderToUse,
                                                    new Class[]{iface},
                                                    new MockInvocationHandler());
        return (T) proxy;
    }

    public static <T> MockBehaviorDefinition<T> when(T mockCall) {
        return new MockBehaviorDefinition<>();
    }

    public static void thenReturn(Object retVal) {
        final MethodCall methodCall = lastMethodCallThreadLocal.get();
        if (methodCall != null) {
            final Object mock = methodCall.getMock();
            final MockInvocationHandler mockInvocationHandler = (MockInvocationHandler) Proxy.getInvocationHandler(mock);
            mockInvocationHandler.setBehavior(new MockBehavior(methodCall, retVal));
        }
    }

    public static class MockBehaviorDefinition<T> {

        public void thenReturn(T retVal) {
            final MethodCall methodCall = lastMethodCallThreadLocal.get();
            if (methodCall != null) {
                final Object mock = methodCall.getMock();
                final MockInvocationHandler mockInvocationHandler = (MockInvocationHandler) Proxy.getInvocationHandler(mock);
                mockInvocationHandler.setBehavior(new MockBehavior(methodCall, retVal));
            }
        }
    }

    private static class MockInvocationHandler implements InvocationHandler {

        private MockBehavior behavior;

        public void setBehavior(MockBehavior behavior) {
            this.behavior = behavior;
        }

        @Override
        public Object invoke(Object mock, Method method, Object[] args) throws Throwable {
            final MethodCall methodCall = new MethodCall(mock, method, args);

            lastMethodCallThreadLocal.set(methodCall);

            if (behavior != null) {
                if (behavior.getMethodCall().equals(methodCall)) {
                    return behavior.getRetVal();
                }
            }

            return null;
        }
    }

    private static class MockBehavior {

        private final MethodCall methodCall;
        private final Object retVal;

        private MockBehavior(MethodCall methodCall, Object retVal) {
            this.methodCall = methodCall;
            this.retVal = retVal;
        }

        public MethodCall getMethodCall() {
            return methodCall;
        }

        public Object getRetVal() {
            return retVal;
        }
    }

    private static class MethodCall {

        private final Object mock;
        private final Method method;
        private final Object[] args;

        private MethodCall(Object mock, Method method, Object[] args) {
            this.mock = mock;
            this.method = method;
            this.args = args;
        }

        public Object getMock() {
            return mock;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getArgs() {
            return args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final MethodCall that = (MethodCall) o;
            return mock == that.mock && method.equals(that.method) && Arrays.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            int result = mock.hashCode();
            result = 31 * result + method.hashCode();
            result = 31 * result + (args != null ? Arrays.hashCode(args) : 0);
            return result;
        }
    }
}
