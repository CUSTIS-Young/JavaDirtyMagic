package custis.young.seminars.dirtymagic.reflection;

import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class Benchmark1 {

    /*
     * This benchmark uses OpenJDK's jmh (http://openjdk.java.net/projects/code-tools/jmh/).
     *
     * To run benchmark:
     *  1) obtain and build jmh, follow the steps described on website;
     *  2) run <Benchmark_Class>.main() with no arguments from IDE;
     *  3) (optional) examine results.
     */

    public static void main(String[] args) {
        org.openjdk.jmh.Main.main(new String[]{
            ".*" + Benchmark1.class.getSimpleName() + ".*"});
    }

    private Bean bean = new Bean();

    private Field beanNameField;

    {
        try {
            beanNameField = Bean.class.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @GenerateMicroBenchmark
    public void baseline() {
        // no op
    }

    @GenerateMicroBenchmark
    public Object getter() {
        return bean.getName();
    }

    private FieldAccessor reflectAccessor = new ReflectFieldAccessorFactory().createFieldAccessor(beanNameField);

    @GenerateMicroBenchmark
    public Object reflection() {
        return reflectAccessor.get(bean);
    }

    private FieldAccessor codeGenAccessor = new CodeGenFieldAccessorFactory().createFieldAccessor(beanNameField);

    @GenerateMicroBenchmark
    public Object codeGen() {
        return codeGenAccessor.get(bean);
    }


    private FieldAccessor unsafeAccessor = new UnsafeFieldAccessorFactory().createFieldAccessor(beanNameField);

    @GenerateMicroBenchmark
    public Object unsafe() {
        return unsafeAccessor.get(bean);
    }

    private FieldAccessor slowReflectFieldAccessor = new SlowReflectFieldAccessor(beanNameField);

    @GenerateMicroBenchmark
    public Object slowReflection() {
        return slowReflectFieldAccessor.get(bean);
    }
}
