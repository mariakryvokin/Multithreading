package arithmeticparser;

import java.util.Arrays;
import java.util.Optional;

public enum Operators {
    ADD("+",1), SUBSTRACT("-",1), MULTIPLY("*",2), DIVIDE("/",2);

    private String sign;
    private int priority;

    Operators(String sign, int priority) {
        this.sign = sign;
        this.priority = priority;
    }

    public String getSign() {
        return sign;
    }

    public int getPriority() {
        return priority;
    }

    /**
     *
     * @param operator
     * @return positive number if first operator has greater priory, negative - lower, zero - equal
     */
    public int comparePriority(Operators operator){
        return this.getPriority()-operator.getPriority();
    }

    public static Optional<Operators> getBySign(String sign){
        return Arrays.stream(Operators.values()).filter(o->o.getSign().equals(sign)).findFirst();
    }

}
