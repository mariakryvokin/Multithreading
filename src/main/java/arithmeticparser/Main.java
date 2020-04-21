package arithmeticparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final String EXPRESSION = "3 + a * 2 / b - 5";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        PrimitiveParser parser = new PrimitiveParser();
        String expressionInPolishNotation = parser.createReversePolishNotation(EXPRESSION);
        LOGGER.info("I am going to calculate");
        calculate(parser, expressionInPolishNotation);
    }

    private static void calculate(PrimitiveParser parser, String expressionInPolishNotation) {
        LOGGER.info("I so close to calculating");
        LOGGER.info("Please enter parameters for EXPRESSION {}", EXPRESSION);
        CompletableFuture<Map<String, Integer>> parametersAndValues = parser.specifyParameter();
        parametersAndValues.thenRunAsync(() -> {
            LOGGER.info("I am calculating");
            Calculator calculator = new Calculator();
            try {
                LOGGER.info(calculator.calculate(replaceParametersWithValues(expressionInPolishNotation,
                        parametersAndValues.get())));
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(e.getMessage(), e);
            }finally {
                parser.closeResources();
                EXECUTOR_SERVICE.shutdown();
            }
        }, EXECUTOR_SERVICE);
    }

    private static String replaceParametersWithValues(String expressionInPolishNotation,
                                                      Map<String, Integer> allParametersAndValues){
        for (String parameterName : allParametersAndValues.keySet()) {
            expressionInPolishNotation = expressionInPolishNotation.replace(parameterName,
                    String.valueOf(allParametersAndValues.get(parameterName)));
        }
        return expressionInPolishNotation;
    }
}
