/**
 * Created by gorz on 23.02.14.
 */
public class Calculator {

    private static final int NO_ACTION = 0;
    private static final int OPERATION_ACTION = 1;
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

    private double op[], memory;
    private OPERATION lastOperation;
    private int lastAction, whereResult;
    private boolean isValueInput;

    public Calculator() {
        op = new double[3];
        memory = 0;
        clear();
    }

    public void setInput(double in) {
        op[2] = in;
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
                    op[0] = op[2];
                    op[1] = op[2];
                    isValueInput = false;
                }
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = op[2];
                    isValueInput = false;
                    executeOperation();
                }
                op[1] = op[0];
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = op[2];
                    op[1] = op[2];
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
        if (!isValueInput) {
            op[2] = op[0];
        }
        setInput(executeFunction(f, op[2], op[0]));
        whereResult = 2;
    }

    public void calculate() throws CalculatorException {
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[1] = op[2];
                    op[0] = op[2];
                    isValueInput = false;
                }
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = op[2];
                    isValueInput = false;
                } else {

                }
                executeOperation();
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = op[2];
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
            case NONE:
                return;
        }
        op[0] = result;
    }

    public void changeSign() {
        op[whereResult] *= -1;
    }

    enum MEMORY {
        CLEAR,
        SET,
        READ,
        PLUS,
        MINUS
    };

    public void memoryAction(MEMORY action) {
        int position = isValueInput ? 2 : whereResult;
        switch(action) {
            case CLEAR:
                memory = 0;
                break;
            case READ:
                setInput(memory);
                whereResult = 2;
                break;
            case SET:
                memory = op[position];
                break;
            case PLUS:
                memory += op[position];
                break;
            case MINUS:
                memory -= op[position];
                break;
        }
    }

    public void clear() {
        op[0] = 0;
        op[1] = 0;
        op[2] = 0;
        whereResult = 0;
        lastOperation = OPERATION.NONE;
        isValueInput = false;
        lastAction = NO_ACTION;
    }
}
