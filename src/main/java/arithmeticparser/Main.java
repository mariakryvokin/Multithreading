package arithmeticparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Future;

public class Main {

    private static String expression = "3 + a * 2 / b - 5";
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args){
        PrimitiveParser parser = new PrimitiveParser();
        String expressionInPolishNotation = parser.createReversePolishNotation(expression);
        System.out.println("Please enter parameters for expression " + expression);
        for (int i = 0; i < parser.getParameters().size(); i++) {
            String parameter = parser.getParameters().get(i);
            try{
                Future<Integer> value = parser.specifyParameter(parameter);
                expressionInPolishNotation = expressionInPolishNotation.replace(parameter, String.valueOf(value.get()));
            }catch (Exception e){
                logger.error(e.getMessage());
                i--;
            }
        }
        parser.closeResources();
        Calculator calculator = new Calculator();
        System.out.println(calculator.calculate(expressionInPolishNotation));

    }
}
