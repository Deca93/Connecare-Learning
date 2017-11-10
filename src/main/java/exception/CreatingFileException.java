package exception;

/**
 * Created by Andrea De Castri on 09/11/2017.
 *
 */
public class CreatingFileException extends Exception {

    @Override
    public String getMessage() {
        return "Impossible creating new file or dir";
    }

}
