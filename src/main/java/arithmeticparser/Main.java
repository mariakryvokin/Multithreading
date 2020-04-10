package arithmeticparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
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
        CompletableFuture<Map<String, Integer>>[] parametersAndValues = getParametersFromClient(parser);
        LOGGER.info("I am going to calculate");
        calculate(parser, expressionInPolishNotation, parametersAndValues);
    }

    private static void calculate(PrimitiveParser parser, String expressionInPolishNotation,
                                  CompletableFuture<Map<String, Integer>>[] parametersAndValues) {
        LOGGER.info("I so close to calculating");
        CompletableFuture.allOf(parametersAndValues).thenRunAsync(() -> {
            LOGGER.info("I am calculating");
            Calculator calculator = new Calculator();
            try {
                LOGGER.info(calculator.calculate(replaceParametersWithValues(parser, expressionInPolishNotation,
                        parametersAndValues)));
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(e.getMessage(), e);
            }finally {
                parser.closeResources();
                EXECUTOR_SERVICE.shutdown();
            }
        }, EXECUTOR_SERVICE);
    }

    private static CompletableFuture<Map<String, Integer>>[] getParametersFromClient(PrimitiveParser parser) {
        LOGGER.info("Please enter parameters for EXPRESSION {}", EXPRESSION);
        CompletableFuture<Map<String, Integer>>[] allParameters = new CompletableFuture[parser.getParameters().size()];
        for (int i = 0; i < parser.getParameters().size(); i++) {
            LOGGER.info(i +" parameter: ");
            String parameterName = parser.getParameters().get(i);
            allParameters[i] = parser.specifyParameter(parameterName)
                    .exceptionally(e->{
                        LOGGER.error(e.getMessage());
                    return Collections.singletonMap(parameterName, 1);});
        }
        return allParameters;
    }

    private static String replaceParametersWithValues(PrimitiveParser parser, String expressionInPolishNotation,
                                                      CompletableFuture<Map<String, Integer>>[] allParameters)
            throws InterruptedException, ExecutionException {
        for (int i = 0; i < parser.getParameters().size(); i++) {
            String parameterName = parser.getParameters().get(i);
            expressionInPolishNotation = expressionInPolishNotation.replace(parameterName,
                    String.valueOf(allParameters[i].get().get(parameterName)));
        }
        return expressionInPolishNotation;
    }
}
