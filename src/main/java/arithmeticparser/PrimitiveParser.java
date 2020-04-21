package arithmeticparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimitiveParser {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<String> parameters = new ArrayList<>();
    private static Logger logger = LogManager.getLogger();
    private Scanner scanner = new Scanner(System.in);

    public String createReversePolishNotation(String expression) {
        String result = "";
        Stack<Operators> operators = new Stack<>();
        expression = expression.replaceAll(" ", "");
        for (int i = 0; i < expression.length(); i++) {
            String partOfExpression = String.valueOf(expression.charAt(i));
            boolean isOperator = Arrays.stream(Operators.values()).anyMatch(o -> o.getSign().equals(partOfExpression));
            if (isOperator) {
                result = addOperatorToStackOrResult(result, operators, partOfExpression);
            } else {
                if (Character.isAlphabetic(partOfExpression.charAt(0))) {
                    parameters.add(partOfExpression);
                }
                result += partOfExpression;
            }
        }
        while (!operators.isEmpty()) {
            result += operators.pop().getSign();
        }
        return result;

    }

    public List<String> getParameters() {
        return parameters;
    }

    private String addOperatorToStackOrResult(String result, Stack<Operators> operators, String partOfExpression) {
        if (!operators.isEmpty() && operators.peek().comparePriority(
                Operators.getBySign(partOfExpression).get()) >= 0) {
            result += operators.pop().getSign();
        }
        operators.push(Operators.getBySign(partOfExpression).get());
        return result;
    }

    public CompletableFuture<Map<String, Integer>> specifyParameter() {
        return CompletableFuture.supplyAsync(() -> getValueFromClient(), executorService);
    }


    private Map<String, Integer> getValueFromClient(){
        Map<String, Integer> parametersAndValues =  new HashMap<>();
            for (int i = 0; i < parameters.size(); i++) {
                logger.info("specify parameter {}", parameters.get(i));
                String lineWithParameter = scanner.nextLine().replaceAll(" ", "");
                try {
                    verifyInput(parameters.get(i), lineWithParameter);
                    parametersAndValues.put(parameters.get(i),Integer.valueOf(lineWithParameter.split("=")[1]));
                } catch (WrongInputException e) {
                    logger.error(e.getMessage());
                    i--;
                }
            }
        return parametersAndValues;
    }

    private void verifyInput(String parameterName, String lineWithParameter) {
        if (!lineWithParameter.matches(parameterName + "=\\d")) {
            throw new WrongInputException("specify parameter in format: {paremeter_name} = {number_value}");
        }
    }

    public void closeResources() {
        executorService.shutdown();
        scanner.close();
    }
}
