/**
 * Created by gorz on 23.02.14.
 */
public class Calculator1 {

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

    private double op[], input;
    private OPERATION lastOperation;
    private int lastAction;
    private boolean isValueInput;

    public Calculator1() {
        op = new double[2];
        clear();
    }

    public void setInput(double in) {
        input = in;
        isValueInput = true;
    }

    public void setOperation(OPERATION operation) {
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
    }

    public void calculateFunction(FUNCTION f) {
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                }
                op[0] = executeFunction(f, op[1], op[0]);
                break;
            case OPERATION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                } else {
                    op[1] = op[0];
                }
                op[1] = executeFunction(f, op[1], op[0]);
                break;
            case FUNCTION_ACTION:
                if(isValueInput) {
                    op[1] = input;
                    isValueInput = false;
                } else {

                }
                op[1] = executeFunction(f, op[1], op[0]);
                break;
            case EQUAL_ACTION:
                if(isValueInput) {
                    op[0] = input;
                    isValueInput = false;
                } else {

                }
                op[0] = executeFunction(f, op[0], op[1]);
                break;
        }
        lastAction = FUNCTION_ACTION;
    }

    public void calculate() {
        switch (lastAction) {
            case NO_ACTION:
                if(isValueInput) {
                    op[1] = input;
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
    }

    public double executeFunction(FUNCTION f, double op1, double op2) {
        switch (f) {
            case SQRT:
                return Math.sqrt(op1);
            case BACKWARD:
                return 1/op1;
            case PERCENT:
                return op2*op1/100;
        }
        return 0;
    }

    public void executeOperation() {
        switch (lastOperation) {
            case DIV:
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
            case NONE:
                op[0] = op[1];
        }
    }

    public void changeSign() {
        op[0] *= -1;
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
