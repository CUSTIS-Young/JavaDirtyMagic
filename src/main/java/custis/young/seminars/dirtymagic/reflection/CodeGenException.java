package custis.young.seminars.dirtymagic.reflection;

public class CodeGenException extends RuntimeException {

    /*package-local*/ CodeGenException(Exception cause) {
        super("An exception while codegeneration", cause);
    }
}
