package custis.young.seminars.dirtymagic.tricks;

import org.testng.annotations.Test;

public class ThrowerTest {

    @Test(expectedExceptions = Exception.class)
    public void thrower1() {
        Thrower1.sneakyThrow(new Exception());
    }

    @Test(expectedExceptions = Exception.class)
    public void thrower2() {
        Thrower2.sneakyThrow(new Exception());
    }

    @Test(expectedExceptions = Exception.class)
    public void thrower3() {
        Thrower3.sneakyThrow(new Exception());
    }

    @Test(expectedExceptions = Exception.class)
    public void thrower4() {
        Thrower4.sneakyThrow(new Exception());
    }
}
