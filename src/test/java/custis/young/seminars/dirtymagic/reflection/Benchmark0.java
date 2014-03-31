package custis.young.seminars.dirtymagic.reflection;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class Benchmark0 {

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
            ".*" + Benchmark0.class.getSimpleName() + ".*"});
    }

    private double x = Math.PI;

    @GenerateMicroBenchmark
    public void baseline() {
        // do nothing
    }

    @GenerateMicroBenchmark
    public void measureWrong() {
        Math.log(x);
    }

    @GenerateMicroBenchmark
    public double measureRight() {
        return Math.log(x);
    }
}
