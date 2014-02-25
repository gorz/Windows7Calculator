/**
 * Created by gorz on 23.02.14.
 */
public class Calculator {

    private static final int NO_ACTION = 0;
    private static final int OPERATION_ACTION = 1;
    private static final int FUNCTION_ACTION = 2;
    private static final int EQUAL_ACTION = 3;

    private static final String OVERFLOW = "Переполнение";
    private static final String DIVISION_BY_ZERO = "Деление на ноль не возможно";
    private static final String ILLEGAL_VALUE = "Недопустимый ввод";

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
    private int lastAction, whereResult;
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
        op[whereResult] = 0;
    }

    public double getResult() throws CalculatorException{
        if(Double.isInfinite(op[whereResult])) {
            throw new CalculatorException(OVERFLOW);
        }
        return op[whereResult];
    }

    public double getMemory() {
        return memory;
    }

    public void setOperation(OPERATION operation) throws CalculatorException {
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
                }
                op[1] = op[0];
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
        whereResult = 0;
    }

    public void calculateFunction(FUNCTION f) throws CalculatorException {
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                }
                op[0] = executeFunction(f, op[1], op[0]);
                whereResult = 0;
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                } else {
                    op[1] = op[0];
                }
                op[1] = executeFunction(f, op[1], op[0]);
                whereResult = 1;
                break;
            case FUNCTION_ACTION:
                if(isValueInput) {
                    op[1] = executeFunction(f, input, op[0]);
                    isValueInput = false;
                } else {
                    op[1] = executeFunction(f, op[1], op[0]);
                }
                whereResult = 1;
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = executeFunction(f, input, op[0]);
                    isValueInput = false;
                } else {
                    op[0] = executeFunction(f, op[0], op[0]);
                }
                whereResult = 0;
                break;
        }
        lastAction = FUNCTION_ACTION;
    }

    public void calculate() throws CalculatorException {
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
        whereResult = 0;
    }

    public double executeFunction(FUNCTION f, double op1, double op2) throws CalculatorException {
        double result = 0;
        switch (f) {
            case SQRT:
                if(op1 < 0) {
                    throw new CalculatorException(ILLEGAL_VALUE);
                }
                result = Math.sqrt(op1);
                if(result == 0.0 && op1 != 0.0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case BACKWARD:
                if(op1 == 0) {
                    throw new CalculatorException(DIVISION_BY_ZERO);
                }
                result = 1/op1;
                if(result == 0.0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case PERCENT:
                result = op2*op1/100;
                if(result == 0.0 && op1 != 0.0 && op2 != 0.0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
        }
        return result;
    }

    public void executeOperation() throws CalculatorException{
        double result = 0;
        switch (lastOperation) {
            case DIV:
                if(op[1] == 0) {
                    throw new CalculatorException(DIVISION_BY_ZERO);
                }
                result = op[0] / op[1];
                if(result == 0.0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case MUL:
                result = op[0] * op[1];
                if(result == 0.0 && op[0] != 0.0 && op[1] != 0.0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case PLUS:
                result = op[0] + op[1];
                if(result == 0.0 && !(op[0] != op[1] && Math.abs(op[0]) == Math.abs(op[1]))) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case MINUS:
                result = op[0] - op[1];
                if(result == 0.0 && op[0] != op[1]) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
        }
        op[0] = result;
    }

    public void changeSign() {
        op[whereResult] *= -1;
    }

    public void memorySet() {
        if(isValueInput) {
            memory = input;
        } else {
            memory = op[whereResult];
        }
    }

    public void memoryPlus() {
        if(isValueInput) {
            memory += input;
        } else {
            memory += op[whereResult];
        }
    }

    public void memoryMinus() {
        if(isValueInput) {
            memory -= input;
        } else {
            memory -= op[whereResult];
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
        whereResult = 0;
        lastOperation = OPERATION.NONE;
        isValueInput = false;
        lastAction = NO_ACTION;
    }
}
