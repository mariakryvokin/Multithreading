package arithmeticparser;

import java.util.Stack;

public class Calculator {

    private Stack<Double> numbers = new Stack<>();

    public double calculate(String expression){
        for(int i = 0; i < expression.length(); i++){
            if( Character.isDigit(expression.charAt(i))){
                numbers.push(Double.valueOf(String.valueOf(expression.charAt(i))));
            }else{
                numbers.push(makeOperation(numbers.pop(),numbers.pop(), String.valueOf(expression.charAt(i))));
            }
        }
        return numbers.pop();
    }

    private double makeOperation(double firstOperand, double secondOperand, String operator){
        switch (operator){
            case "*" : return firstOperand * secondOperand;
            case "-" : return secondOperand - firstOperand;
            case "+" : return firstOperand + secondOperand;
            case "/" : return secondOperand / firstOperand;
            default: return 0;
        }
    }
}
