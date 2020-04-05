package philosophersproblem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ForkRepository {
    Logger LOGGER = LogManager.getLogger();

    long eat(Fork leftFork, Fork rightFork, long time, boolean takeRightForkFirst);
}
