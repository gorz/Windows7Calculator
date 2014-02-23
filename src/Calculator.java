import org.omg.CORBA.NO_IMPLEMENT;

/**
 * Created by gorz on 18.02.14.
 */
public class Calculator {

    enum OPERATION {
        DIV,
        MUL,
        MINUS,
        PLUS,
        NONE
    };

    enum FUNCTION {
        SQRT,
        PERCENT,
        INVERSE
    }

    private double memory;
    private double lastValue;
    private double value;
    private double input;
    private OPERATION lastOperation;

    public Calculator() {
        clear();
        memory = 0;
    }

    public double readMemory() {
        return memory;
    }

    public void setMemory(double val) {
        memory = val;
    }

    public void addToMemory(double val) {
        memory += val;
    }

    public void subFromMemory(double val) {
        memory -= val;
    }

    public void putValue(double d) {
        input = d;
    }

    public double getValue() {
        return value;
    }

    public void changeSign() {
        value *= -1;
    }

    public void changeOperation(OPERATION operation) {
        input = value;
        lastOperation = operation;
    }

    public double executeOperation() throws CalculatorException {
        switch (lastOperation) {
            case DIV:
                if(input == 0) {
                    throw new CalculatorException("Division by 0");
                }
                value /= input;
                break;
            case MUL:
                value *= input;
                break;
            case PLUS:
                value += input;
                break;
            case MINUS:
                value -= input;
                break;
            case NONE:
                value = input;
        }
        input = value;
        return value;
    }

    public double executeFunction(FUNCTION f) throws CalculatorException {
        switch (f) {
            case SQRT:
                if(input < 0) {
                    throw new CalculatorException("Error");
                }
                input = Math.sqrt(input);
                break;
            case PERCENT:
                input = value * (input/100);
                break;
            case INVERSE:
                if(input == 0) {
                    throw new CalculatorException("Division by 0");
                }
                input = 1/input;
                break;
        }
        return input;
    }

    public double getResult() throws CalculatorException{
        double t = input;
        double r = executeOperation();
        input = t;
        return r;
    }

    public void clear() {
        lastValue = 0;
        value = 0;
        input = 0;
        lastOperation = OPERATION.NONE;
    }

}
