/**
 * Created by gorz on 23.02.14.
 */
public class Calculator {

    private static final int NO_ACTION = 0;
    private static final int OPERATION_ACTION = 1;
    private static final int FUNCTION_ACTION = 2;
    private static final int EQUAL_ACTION = 3;

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
        BACKWARD
    }

    private double op[], input, memory;
    private OPERATION lastOperation;
    private int lastAction;
    private boolean isValueInput;

    public Calculator() {
        op = new double[2];
        memory = 0;
        clear();
    }

    public void setInput(double in) {
        input = in;
        isValueInput = true;
    }

    public void clearResult() {
        op[0] = 0;
    }

    public double setOperation(OPERATION operation) throws CalculatorException {
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[0] = input;
                    op[1] = input;
                    isValueInput = false;
                }
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                    executeOperation();
                } else {
                    op[1] = op[0];
                }
                break;
            case FUNCTION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                    executeOperation();
                } else {
                    executeOperation();
                }
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = input;
                    op[1] = input;
                    isValueInput = false;
                } else {
                    op[1] = op[0];
                }
                break;
        }
        lastOperation = operation;
        lastAction = OPERATION_ACTION;
        return op[0];
    }

    public double calculateFunction(FUNCTION f) throws CalculatorException {
        double result = 0;
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                }
                op[0] = executeFunction(f, op[1], op[0]);
                result = op[0];
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                } else {
                    op[1] = op[0];
                }
                op[1] = executeFunction(f, op[1], op[0]);
                result = op[1];
                break;
            case FUNCTION_ACTION:
                if(isValueInput) {
                    op[0] = executeFunction(f, input, op[0]);
                    isValueInput = false;
                } else {
                    op[0] = executeFunction(f, op[0], op[0]);
                }
                result = op[0];
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = executeFunction(f, input, op[0]);
                    isValueInput = false;
                } else {
                    op[0] = executeFunction(f, op[0], op[0]);
                }
                result = op[0];
                break;
        }
        lastAction = FUNCTION_ACTION;
        return result;
    }

    public double calculate() throws CalculatorException {
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    op[0] = input;
                    isValueInput = false;
                }
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                } else {

                }
                executeOperation();
                break;
            case FUNCTION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                    executeOperation();
                } else {
                    executeOperation();
                }
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = input;
                    isValueInput = false;
                } else {

                }
                executeOperation();
                break;
        }
        lastAction = EQUAL_ACTION;
        return op[0];
    }

    public double executeFunction(FUNCTION f, double op1, double op2) throws CalculatorException {
        switch (f) {
            case SQRT:
                if(op1 < 0) {
                    throw new CalculatorException("Error");
                }
                return Math.sqrt(op1);
            case BACKWARD:
                if(op1 == 0) {
                    throw new CalculatorException("Division by 0");
                }
                return 1/op1;
            case PERCENT:
                return op2*op1/100;
        }
        return 0;
    }

    public void executeOperation() throws CalculatorException{
        switch (lastOperation) {
            case DIV:
                if(op[1] == 0) {
                    throw new CalculatorException("Division by 0");
                }
                op[0] /= op[1];
                break;
            case MUL:
                op[0] *= op[1];
                break;
            case PLUS:
                op[0] += op[1];
                break;
            case MINUS:
                op[0] -= op[1];
                break;
        }
    }

    public double changeSign() {
        op[0] *= -1;
        return op[0];
    }

    public void memorySet() {
        if(isValueInput) {
            memory = input;
        } else {
            memory = op[0];
        }
    }

    public void memoryPlus() {
        if(isValueInput) {
            memory += input;
        } else {
            memory += op[0];
        }
    }

    public void memoryMinus() {
        if(isValueInput) {
            memory -= input;
        } else {
            memory -= op[0];
        }
    }

    public double memoryRead() {
        setInput(memory);
        return memory;
    }

    public void memoryClear() {
        memory = 0;
    }

    public void clear() {
        op[0] = 0;
        op[1] = 0;
        input = 0;
        lastOperation = OPERATION.NONE;
        isValueInput = false;
        lastAction = NO_ACTION;
    }
}
