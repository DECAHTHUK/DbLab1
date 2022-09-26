package MyLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logg3r {
    private static final Logger LOGGER = Logger.getLogger("Logg3r");

    public static void logNew(Level level, String message) {
        LOGGER.log(level, message);
    }
}
