package arithmeticparser;

import java.util.concurrent.Future;

public class Main {

    private static String expression = "3 + a * 2 / b - 5";

    public static void main(String[] args) throws Throwable {
        Parser parser = new PrimitiveParser();
        String expressionInPolishNotation = parser.createReversePolishNotation(expression);
        System.out.println("Please enter parameters for expression " + expression);
        for (int i = 0; i < ((PrimitiveParser) parser).getParameters().size(); i++) {
            String parameter = ((PrimitiveParser) parser).getParameters().get(i);
            try{
                Future<Integer> value = ((PrimitiveParser) parser).specifyParameter(parameter);
                expressionInPolishNotation = expressionInPolishNotation.replace(parameter, String.valueOf(value.get()));
            }catch (Exception e){
                System.out.println("specify parameter in format: {paremeter_name} = {number_value}");
                i--;
            }
        }
        ((PrimitiveParser) parser).finalize();
        System.out.println(expressionInPolishNotation);
        Calculator calculator = new Calculator();
        System.out.println(calculator.calculate(expressionInPolishNotation));

    }
}
