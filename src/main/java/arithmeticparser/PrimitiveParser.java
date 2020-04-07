package arithmeticparser;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimitiveParser {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<String> parameters = new ArrayList<>();
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

    public Future<Integer> specifyParameter(String parameterName) {
        System.out.println("specify parameter " + parameterName);
        return executorService.submit(() -> {
            String lineWithParameter = scanner.nextLine().replaceAll(" ", "");
            String[] variableAndValue = lineWithParameter.split("=");
            return Integer.valueOf(variableAndValue[1]);
        });
    }

    public void closeResources() {
        scanner.close();
        executorService.shutdown();
    }
}
