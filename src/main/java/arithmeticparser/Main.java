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

    private static String expression = "3 + a * 2 / b - 5";
    private static Logger logger = LogManager.getLogger();
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        PrimitiveParser parser = new PrimitiveParser();
        String expressionInPolishNotation = parser.createReversePolishNotation(expression);
        CompletableFuture<Map<String, Integer>>[] parametersAndValues = getParametersFromClient(parser);
        logger.info("I am going to calculate");
        calculate(parser, expressionInPolishNotation, parametersAndValues);
    }

    private static void calculate(PrimitiveParser parser, String expressionInPolishNotation,
                                  CompletableFuture<Map<String, Integer>>[] parametersAndValues) {
        logger.info("I so close to calculating");
        CompletableFuture.allOf(parametersAndValues).thenRunAsync(() -> {
            logger.info("I am calculating");
            Calculator calculator = new Calculator();
            try {
                logger.info(calculator.calculate(replaceParametersWithValues(parser, expressionInPolishNotation,
                        parametersAndValues)));
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
            parser.closeResources();
            executorService.shutdown();
        }, executorService);
    }

    private static CompletableFuture<Map<String, Integer>>[] getParametersFromClient(PrimitiveParser parser) {
        logger.info("Please enter parameters for expression " + expression);
        CompletableFuture<Map<String, Integer>>[] allParameters = new CompletableFuture[parser.getParameters().size()];
        for (int i = 0; i < parser.getParameters().size(); i++) {
            logger.info(i +" parameter: ");
            String parameterName = parser.getParameters().get(i);
            allParameters[i] = parser.specifyParameter(parameterName)
                    .exceptionally(e->{logger.error(e.getMessage());
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
