import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.Arc2D;
import java.text.DecimalFormat;

/**
 * Created by gorz on 18.02.14.
 */
public class GUI extends JFrame {

    enum BUTTONS {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        MEMORY_CLEAR, MEMORY_READ, MEMORY_SET, MEMORY_PLUS, MEMORY_MINUS,
        BACKSPACE, CLEAR, CLEAR_ALL, PLUS_MINUS, SQRT, DIV, MUL, PLUS,
        MINUS, PERCENT, INVERSE, DOT, EQUAL
    };

    private String BUTTONS_SYMBOLS[] = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "MC", "MR", "MS", "M+", "M-",
            "←", "CE", "C", "±", "√", "/", "*", "+",
            "-", "%", "1/x", ",", "="
    };

    private JButton buttons[];
    private Text inputField;
    private Font resultFont, errorFont;

    private Formatter formatter;

    private boolean isValueTyped, isBlocked;
    private StringBuilder currentValue;
    private Calculator calculator;

    public GUI() {
        super("Calculator");
        calculator = new Calculator();
        formatter = new Formatter();
        initGui();
        initListeners();
        currentValue = new StringBuilder("0");
        isValueTyped = true;
        isBlocked = false;
        updateInputField();
    }

    private void setBlocked(boolean f) {
        isBlocked = f;
        if(f) {
            inputField.setFont(errorFont);
        } else {
            inputField.setFont(resultFont);
        }
    }

    private void removeLastCharacter() {
        if(!isValueTyped) {
            return;
        }
        currentValue.deleteCharAt(currentValue.length()-1);
        if(currentValue.length() == 0) {
            currentValue.append("0");
        } else if(currentValue.length() == 1 && currentValue.charAt(0) == '-') {
            currentValue.setCharAt(0, '0');
        } else if(currentValue.length() == 2 && currentValue.indexOf("-0") == 0) {
            currentValue.setLength(1);
            currentValue.setCharAt(0, '0');
        }
    }

    private void putCharacter(String c) {
        boolean valueHasDot = currentValue.indexOf(",") != -1;
        boolean valueHasMinus = currentValue.indexOf("-") == 0;
        int numbersInValue = currentValue.length() - (valueHasDot ? 1 : 0) - (valueHasMinus ? 1 : 0);
        if(numbersInValue == 16) {
            return;
        }
        isValueTyped = true;
        boolean isDot = c.equals(",");
        if(currentValue.length() == 1 && currentValue.charAt(0) == '0' && !isDot) {
            currentValue.replace(0, 1, c);
            return;
        }
        if(isDot && valueHasDot) {
            return;
        }
        currentValue.append(c);
    }

    private void changeSign() {
        char c = currentValue.charAt(0);
        if(currentValue.length() == 1 && c == '0') {
            return;
        }
        if(c == '-') {
            currentValue.deleteCharAt(0);
        } else {
            currentValue.insert(0, '-');
        }
    }

    private void clear() {
        isValueTyped = false;
        currentValue.setLength(1);
        currentValue.setCharAt(0, '0');
    }

    private void showError(String error) {
        setBlocked(true);
        inputField.setText(error);
    }

    private void showResults(double result) {
        inputField.setText(formatter.format(result));
    }

    private void setMemoryFlag(boolean f) {
        inputField.setMemoryFlagVisible(f);
    }

    private void updateInputField() {
        if(!isValueTyped) {
            return;
        }
        inputField.setText(currentValue.toString());
    }

    private double getValue() {
        return Double.valueOf(currentValue.toString().replace(',', '.'));
    }

    public static void main(String[] args) {
        new GUI();
    }

    public void onInput(String ch) {
        if(isBlocked) {
            return;
        }
        putCharacter(ch);
        updateInputField();
    }

    public void onOperationButtonClick(Calculator.OPERATION operation) {
        if(isBlocked) {
            return;
        }
        try {
            if(isValueTyped) {
                calculator.setInput(getValue());
            }
            calculator.setOperation(operation);
            showResults(calculator.getResult());
        } catch (CalculatorException e) {
            showError(e.getMessage());
            calculator.clear();
        }
        clear();
    }

    public void onEqualButtonClick() {
        if(isBlocked) {
            return;
        }
        try {
            if(isValueTyped) {
                calculator.setInput(getValue());
            }
            calculator.calculate();
            showResults(calculator.getResult());
        } catch (CalculatorException e) {
            showError(e.getMessage());
            calculator.clear();
        }
        clear();
    }

    public void onFunctionButtonClick(Calculator.FUNCTION function) {
        if(isBlocked) {
            return;
        }
        try {
            if(isValueTyped) {
                calculator.setInput(getValue());
            }
            calculator.calculateFunction(function);
            showResults(calculator.getResult());
        } catch (CalculatorException e) {
            showError(e.getMessage());
            calculator.clear();
        }
        clear();
    }

    public void onBackspaceButtonClick() {
        if(isBlocked) {
            return;
        }
        removeLastCharacter();
        updateInputField();
    }

    public void onChangeSignButtonClick() {
        if(isBlocked) {
            return;
        }
        if(isValueTyped) {
            changeSign();
            updateInputField();
        } else {
            try {
                calculator.changeSign();
                showResults(calculator.getResult());
            } catch (CalculatorException e) {
                showError(e.getMessage());
                clear();
                calculator.clear();
            }
        }
    }

    //TODO: fix
    public void onClearButtonClick() {
        clear();
        calculator.clearResult();
        isValueTyped = true; //may be bug
        updateInputField();
        setBlocked(false);
    }

    //TODO: fix
    public void onClearAllButtonClick() {
        calculator.clear();
        onClearButtonClick();
    }

    public void onMemoryButtonClick(Calculator.MEMORY action) {
        if(isValueTyped) {
            calculator.setInput(getValue());
            clear();
        }
        calculator.memoryAction(action);
        if(action != Calculator.MEMORY.READ) {
            setMemoryFlag(calculator.getMemory() != 0.0);
            return;
        }
        try {
            showResults(calculator.getResult());
        } catch (CalculatorException e) {
            showError(e.getMessage());
            calculator.clear();
        }
        clear();
    }

    private void initListeners() {
        KeyListener onKey = new Listeners.onKeyPress(this);
        for(int i=0; i<buttons.length; i++) {
            buttons[i].addKeyListener(onKey);
        }
        buttons[BUTTONS.PLUS.ordinal()].addActionListener(
                new Listeners.onOperation(this, Calculator.OPERATION.PLUS)
        );
        buttons[BUTTONS.MINUS.ordinal()].addActionListener(
                new Listeners.onOperation(this, Calculator.OPERATION.MINUS)
        );
        buttons[BUTTONS.DIV.ordinal()].addActionListener(
                new Listeners.onOperation(this, Calculator.OPERATION.DIV)
        );
        buttons[BUTTONS.MUL.ordinal()].addActionListener(
                new Listeners.onOperation(this, Calculator.OPERATION.MUL)
        );

        buttons[BUTTONS.EQUAL.ordinal()].addActionListener(
                new Listeners.onEqual(this)
        );

        buttons[BUTTONS.SQRT.ordinal()].addActionListener(
                new Listeners.onFunction(this, Calculator.FUNCTION.SQRT)
        );
        buttons[BUTTONS.PERCENT.ordinal()].addActionListener(
                new Listeners.onFunction(this, Calculator.FUNCTION.PERCENT)
        );
        buttons[BUTTONS.INVERSE.ordinal()].addActionListener(
                new Listeners.onFunction(this, Calculator.FUNCTION.BACKWARD)
        );

        ActionListener onNumberClick =
                new Listeners.onNumber(this);

        //первыми в массиве кнопок всегду идут кнопки с цифрами
        for(int i=0; i<10; i++) {
            buttons[i].addActionListener(onNumberClick);
        }
        buttons[BUTTONS.DOT.ordinal()].addActionListener(onNumberClick);

        buttons[BUTTONS.BACKSPACE.ordinal()].addActionListener(
                new Listeners.onBackspace(this)
        );

        buttons[BUTTONS.PLUS_MINUS.ordinal()].addActionListener(
                new Listeners.onChangeSign(this)
        );

        buttons[BUTTONS.CLEAR.ordinal()].addActionListener(
                new Listeners.onClear(this)
        );

        buttons[BUTTONS.CLEAR_ALL.ordinal()].addActionListener(
                new Listeners.onClearAll(this)
        );

        buttons[BUTTONS.MEMORY_CLEAR.ordinal()].addActionListener(
                new Listeners.onMemory(this, Calculator.MEMORY.CLEAR)
        );

        buttons[BUTTONS.MEMORY_SET.ordinal()].addActionListener(
                new Listeners.onMemory(this, Calculator.MEMORY.SET)
        );

        buttons[BUTTONS.MEMORY_READ.ordinal()].addActionListener(
                new Listeners.onMemory(this, Calculator.MEMORY.READ)
        );

        buttons[BUTTONS.MEMORY_PLUS.ordinal()].addActionListener(
                new Listeners.onMemory(this, Calculator.MEMORY.PLUS)
        );

        buttons[BUTTONS.MEMORY_MINUS.ordinal()].addActionListener(
                new Listeners.onMemory(this, Calculator.MEMORY.MINUS)
        );
    }

    private void initGui() {
        Container pane = getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());

        inputField = new Text();
        resultFont = new Font(inputField.getFont().getFontName(), Font.PLAIN, 14);
        errorFont = new Font(inputField.getFont().getFontName(), Font.PLAIN, 11);
        inputField.setFont(resultFont);
        inputField.setDisabledTextColor(Color.BLACK);
        inputField.setEnabled(false);
        inputField.setHorizontalAlignment(JTextField.RIGHT);

        Dimension size = new Dimension(34, 26);
        Insets zeroMargin = new Insets(0, 0, 0, 0);

        buttons = new JButton[BUTTONS.values().length];
        for(int i=0; i<buttons.length; i++) {
            buttons[i] = new JButton(BUTTONS_SYMBOLS[i]);
            buttons[i].setPreferredSize(size);
            buttons[i].setMargin(zeroMargin);
        }

        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.insets = new Insets(3,3,3,3);
        constraints.gridwidth = 5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(inputField, constraints);

        constraints.fill = GridBagConstraints.NONE;

        constraints.gridwidth = 1;

        constraints.gridy = 1;

        constraints.gridx = 0;
        pane.add(buttons[BUTTONS.MEMORY_CLEAR.ordinal()], constraints);

        constraints.gridx = 1;
        pane.add(buttons[BUTTONS.MEMORY_READ.ordinal()], constraints);

        constraints.gridx = 2;
        pane.add(buttons[BUTTONS.MEMORY_SET.ordinal()], constraints);

        constraints.gridx = 3;
        pane.add(buttons[BUTTONS.MEMORY_PLUS.ordinal()], constraints);

        constraints.gridx = 4;
        pane.add(buttons[BUTTONS.MEMORY_MINUS.ordinal()], constraints);

        constraints.gridy = 2;

        constraints.gridx = 0;
        pane.add(buttons[BUTTONS.BACKSPACE.ordinal()], constraints);

        constraints.gridx = 1;
        pane.add(buttons[BUTTONS.CLEAR.ordinal()], constraints);

        constraints.gridx = 2;
        pane.add(buttons[BUTTONS.CLEAR_ALL.ordinal()], constraints);

        constraints.gridx = 3;
        pane.add(buttons[BUTTONS.PLUS_MINUS.ordinal()], constraints);

        constraints.gridx = 4;
        pane.add(buttons[BUTTONS.SQRT.ordinal()], constraints);

        constraints.gridy = 3;

        constraints.gridx = 0;
        pane.add(buttons[BUTTONS.SEVEN.ordinal()], constraints);

        constraints.gridx = 1;
        pane.add(buttons[BUTTONS.EIGHT.ordinal()], constraints);

        constraints.gridx = 2;
        pane.add(buttons[BUTTONS.NINE.ordinal()], constraints);

        constraints.gridx = 3;
        pane.add(buttons[BUTTONS.DIV.ordinal()], constraints);

        constraints.gridx = 4;
        pane.add(buttons[BUTTONS.PERCENT.ordinal()], constraints);

        constraints.gridy = 4;

        constraints.gridx = 0;
        pane.add(buttons[BUTTONS.FOUR.ordinal()], constraints);

        constraints.gridx = 1;
        pane.add(buttons[BUTTONS.FIVE.ordinal()], constraints);

        constraints.gridx = 2;
        pane.add(buttons[BUTTONS.SIX.ordinal()], constraints);

        constraints.gridx = 3;
        pane.add(buttons[BUTTONS.MUL.ordinal()], constraints);

        constraints.gridx = 4;
        pane.add(buttons[BUTTONS.INVERSE.ordinal()], constraints);

        constraints.gridy = 5;

        constraints.gridx = 0;
        pane.add(buttons[BUTTONS.ONE.ordinal()], constraints);

        constraints.gridx = 1;
        pane.add(buttons[BUTTONS.TWO.ordinal()], constraints);

        constraints.gridx = 2;
        pane.add(buttons[BUTTONS.THREE.ordinal()], constraints);

        constraints.gridx = 3;
        pane.add(buttons[BUTTONS.MINUS.ordinal()], constraints);

        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridheight = 2;
        constraints.gridx = 4;
        pane.add(buttons[BUTTONS.EQUAL.ordinal()], constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridy = 6;

        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        pane.add(buttons[BUTTONS.ZERO.ordinal()], constraints);

        constraints.fill = GridBagConstraints.NONE;

        constraints.gridwidth = 1;

        constraints.gridx = 2;
        pane.add(buttons[BUTTONS.DOT.ordinal()], constraints);

        constraints.gridx = 3;
        pane.add(buttons[BUTTONS.PLUS.ordinal()], constraints);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
