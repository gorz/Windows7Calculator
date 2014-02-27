/**
 * Created by gorz on 23.02.14.
 */
public class Calculator {

    private static final int ACCUMULATOR = 0;
    private static final int ARGUMENT = 1;
    private static final int INPUT = 2;

    private static final int NO_ACTION = 0;
    private static final int OPERATION_ACTION = 1;
    private static final int EQUAL_ACTION = 3;

    private static final String OVERFLOW = "Переполнение";
    private static final String DIVISION_BY_ZERO = "Деление на ноль не возможно";
    private static final String ILLEGAL_VALUE = "Недопустимый ввод";

    enum OPERATION {DIV, MUL, MINUS, PLUS, NONE}

    enum FUNCTION {SQRT, PERCENT, BACKWARD}

    enum MEMORY {CLEAR, SET, READ, PLUS, MINUS}

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
        op[INPUT] = in;
        isValueInput = true;
    }

    public void clearResult() {
        if (lastAction == OPERATION_ACTION) {
            op[ARGUMENT] = 0;
        } else {
            op[whereResult] = 0;
        }
    }

    public double getResult() throws CalculatorException {
        if (Double.isInfinite(op[whereResult])) {
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
                if (isValueInput) {
                    op[ACCUMULATOR] = op[INPUT];
                    op[ARGUMENT] = op[INPUT];
                    isValueInput = false;
                }
                break;
            case OPERATION_ACTION:
                if (isValueInput) {
                    op[ARGUMENT] = op[INPUT];
                    isValueInput = false;
                    executeOperation();
                }
                op[ARGUMENT] = op[ACCUMULATOR];
                break;
            case EQUAL_ACTION:
                if (isValueInput) {
                    op[ACCUMULATOR] = op[INPUT];
                    op[ARGUMENT] = op[INPUT];
                    isValueInput = false;
                } else {
                    op[ARGUMENT] = op[ACCUMULATOR];
                }
                break;
        }
        lastOperation = operation;
        lastAction = OPERATION_ACTION;
        whereResult = 0;
    }

    public void calculateFunction(FUNCTION f) throws CalculatorException {
        if (!isValueInput) {
            op[INPUT] = op[ACCUMULATOR];
        }
        setInput(executeFunction(f, op[INPUT], op[ACCUMULATOR]));
        whereResult = 2;
    }

    public void calculate() throws CalculatorException {
        switch (lastAction) {
            case NO_ACTION:
                if (isValueInput) {
                    op[ARGUMENT] = op[INPUT];
                    op[ACCUMULATOR] = op[INPUT];
                    isValueInput = false;
                }
                break;
            case OPERATION_ACTION:
                if (isValueInput) {
                    op[ARGUMENT] = op[INPUT];
                    isValueInput = false;
                } else {

                }
                executeOperation();
                break;
            case EQUAL_ACTION:
                if (isValueInput) {
                    op[ACCUMULATOR] = op[INPUT];
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
                if (op1 < 0) {
                    throw new CalculatorException(ILLEGAL_VALUE);
                }
                result = Math.sqrt(op1);
                if (result == 0 && op1 != 0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case BACKWARD:
                if (op1 == 0) {
                    throw new CalculatorException(DIVISION_BY_ZERO);
                }
                result = 1 / op1;
                if (result == 0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case PERCENT:
                result = op2 * op1 / 100;
                if (result == 0 && op1 != 0 && op2 != 0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
        }
        return result;
    }

    public void executeOperation() throws CalculatorException {
        double result = 0;
        switch (lastOperation) {
            case DIV:
                if (op[ARGUMENT] == 0) {
                    throw new CalculatorException(DIVISION_BY_ZERO);
                }
                result = op[ACCUMULATOR] / op[ARGUMENT];
                if (result == 0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case MUL:
                result = op[ACCUMULATOR] * op[ARGUMENT];
                if (result == 0 && op[ACCUMULATOR] != 0 && op[ARGUMENT] != 0) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case PLUS:
                result = op[ACCUMULATOR] + op[ARGUMENT];
                if ((op[ACCUMULATOR] != 0 && op[ARGUMENT] != 0) && result == 0
                        && !(op[ACCUMULATOR] != op[ARGUMENT]
                        && Math.abs(op[ACCUMULATOR]) == Math.abs(op[ARGUMENT]))) {

                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case MINUS:
                result = op[ACCUMULATOR] - op[ARGUMENT];
                if (result == 0.0 && op[ACCUMULATOR] != op[ARGUMENT]) {
                    throw new CalculatorException(OVERFLOW);
                }
                break;
            case NONE:
                return;
        }
        op[ACCUMULATOR] = result;
    }

    public void changeSign() {
        setInput(-1 * op[whereResult]);
        whereResult = 2;
    }

    public void memoryAction(MEMORY action) {
        int position = isValueInput ? 2 : whereResult;
        switch (action) {
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
        op[ACCUMULATOR] = 0;
        op[ARGUMENT] = 0;
        op[INPUT] = 0;
        whereResult = 0;
        lastOperation = OPERATION.NONE;
        isValueInput = false;
        lastAction = NO_ACTION;
    }
}

/*      + 5 + 5 + 5 + 5 + 5 + 5 +
        + 5 + 5 = + 5 = + 5 = + 5
        + 5 + 5 + 5 = 15 = 20 = 25
        + 5 + 5 = 10 7 + 3 = 10
        + 1 + 1 = 2 0.2 + 3 =
        + 0.3 + 0.2.35 / 147
        + 0 в начале
        + 0 с
        + % 1
        + 48-2%
        + 0,,,,
        + 56 = = =
        + -56====
        + -%
        + 0%
        + -000
        + -=
        + —-=
        + 0779
        + -,=
        + -√
        + 0.=
        + 56*√*√
        + 56*√===
        + 56*√√√√=
        + 56*6√*9√=
        + 0,05 + 0,01 = - 0 ,
        + -65√
        + СЕ
        + C
        + Backspace
        + m+
        + m-
        + ms
        + mc
        + mr
        + ограничение на ввод
        + работа с процентами
        + 150/20% =
        + 150/20% % % =
        + 150/20% % % = % % %
        + 150/20% % % = % = % =
        + 250+-+-+
        + +-
        + 5 + 5 = 9 =
        + 9+9++++
150-20 +- %
        + кнопка 1/x
        + Percent.Text = %;
        + 9===
        + m+ m-
        + 9 %
        + MR (при нуле в памяти)
        + 100-20% % = % % %
        + 150-25 % % % % % =
        + бесконечность! (проверка на переполнение как вверх, так и вниз)
        + Горячие клавиши!
        + Enter это и есть равно
        + 5 1/x ,
        + 0.222 <-<-<-<- ...
        + 0.0 +-
        + ,54 +- <-<-<-
        + 5*0.0001 =====
        + 125/20%234
        + √2 M+ - 10 % M- = M- MR*/
