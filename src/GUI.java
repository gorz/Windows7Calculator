import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

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
            "-", "%", "1/x", ".", "="
    };

    private JButton buttons[];
    private JTextField inputField;

    private boolean isValueTyped;
    private StringBuilder currentValue;
    private Calculator1 calculator;

    public GUI() {
        super("Calculator");
        calculator = new Calculator1();
        initGui();
        initListeners();
        currentValue = new StringBuilder("0");
        isValueTyped = true;
        updateInputField();
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
        }
    }

    private void putCharacter(String c) {
        isValueTyped = true;
        boolean isDot = c.equals(".");
        if(currentValue.length() == 1 && currentValue.charAt(0) == '0' && !isDot) {
            currentValue.replace(0, 1, c);
            return;
        }
        if(isDot && currentValue.indexOf(".") != -1) {
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
        inputField.setText(error);
    }

    private void showResults(double result) {
        inputField.setText(doubleToString(result));
    }

    private void updateInputField() {
        if(!isValueTyped) {
            return;
        }
        inputField.setText(currentValue.toString());
    }

    private double getValue() {
        return Double.valueOf(currentValue.toString());
    }

    private static String doubleToString(double d) {
        String value = Double.toString(d);
        if(((long) d) == d) {
            value = value.split("\\.")[0];
        }
        if(value.equals("-0")) {
            value = "0";
        }
        return value;
    }

    public static void main(String[] args) {
        new GUI();
    }

    private void initListeners() {
        ActionListener onOperation = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JButton source = (JButton)event.getSource();
                int buttonIndex = BUTTONS.EQUAL.ordinal();
                for(int i = 0; i<buttons.length; i++) {
                    if(buttons[i] == source) {
                        buttonIndex = i;
                        break;
                    }
                }
                Calculator1.OPERATION operation = Calculator1.OPERATION.NONE;
                switch(BUTTONS.values()[buttonIndex]) {
                    case DIV: operation = Calculator1.OPERATION.DIV; break;
                    case MUL: operation = Calculator1.OPERATION.MUL; break;
                    case PLUS: operation = Calculator1.OPERATION.PLUS; break;
                    case MINUS: operation = Calculator1.OPERATION.MINUS; break;
                }
                try {
                    if(isValueTyped) {
                        calculator.setInput(getValue());
                    }
                    showResults(calculator.setOperation(operation));
                    clear();
                } catch (CalculatorException e) {
                    showError(e.getMessage());
                    clear();
                    calculator.clear();
                } catch (NumberFormatException e) {

                }
            }
        };

        buttons[BUTTONS.PLUS.ordinal()].addActionListener(onOperation);
        buttons[BUTTONS.MINUS.ordinal()].addActionListener(onOperation);
        buttons[BUTTONS.DIV.ordinal()].addActionListener(onOperation);
        buttons[BUTTONS.MUL.ordinal()].addActionListener(onOperation);

        buttons[BUTTONS.EQUAL.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    if(isValueTyped) {
                        calculator.setInput(getValue());
                    }
                    showResults(calculator.calculate());
                    clear();
                } catch (CalculatorException e) {
                    showError(e.getMessage());
                    clear();
                    calculator.clear();
                } catch (NumberFormatException e) {

                }


            }
        });

        ActionListener onFunction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JButton source = (JButton)event.getSource();
                int buttonIndex = BUTTONS.EQUAL.ordinal();
                for(int i = 0; i<buttons.length; i++) {
                    if(buttons[i] == source) {
                        buttonIndex = i;
                        break;
                    }
                }
                Calculator1.FUNCTION function = Calculator1.FUNCTION.SQRT;
                switch(BUTTONS.values()[buttonIndex]) {
                    case SQRT: function = Calculator1.FUNCTION.SQRT; break;
                    case INVERSE: function = Calculator1.FUNCTION.BACKWARD; break;
                    case PERCENT: function = Calculator1.FUNCTION.PERCENT; break;
                }
                try {
                    if(isValueTyped) {
                        calculator.setInput(getValue());
                    }
                    showResults(calculator.calculateFunction(function));
                    clear();
                } catch (CalculatorException e) {
                    showError(e.getMessage());
                    clear();
                    calculator.clear();
                } catch (NumberFormatException e) {

                }
            }
        };
        buttons[BUTTONS.SQRT.ordinal()].addActionListener(onFunction);
        buttons[BUTTONS.PERCENT.ordinal()].addActionListener(onFunction);
        buttons[BUTTONS.INVERSE.ordinal()].addActionListener(onFunction);

        ActionListener onNumberClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton)e.getSource();
                putCharacter(source.getText());
                updateInputField();
            }
        };
        //первіми в массиве кнопок всегду идут кнопки с цифрами
        for(int i=0; i<10; i++) {
            buttons[i].addActionListener(onNumberClick);
        }
        buttons[BUTTONS.DOT.ordinal()].addActionListener(onNumberClick);

        buttons[BUTTONS.BACKSPACE.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeLastCharacter();
                updateInputField();
            }
        });

        buttons[BUTTONS.PLUS_MINUS.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isValueTyped) {
                    changeSign();
                    updateInputField();
                } else {
                    showResults(calculator.changeSign());
                }
            }
        });

        buttons[BUTTONS.CLEAR.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                calculator.clearResult();
                isValueTyped = true;
                updateInputField();
            }
        });

        buttons[BUTTONS.CLEAR_ALL.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculator.clear();
                clear();
                isValueTyped = true;
                updateInputField();
            }
        });

        /*buttons[BUTTONS.MEMORY_CLEAR.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculator.setMemory(0);
            }
        });

        buttons[BUTTONS.MEMORY_SET.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isValueTyped) {
                    calculator.setMemory(getValue());
                } else {
                    calculator.setMemory(calculator.getValue());
                }
            }
        });

        buttons[BUTTONS.MEMORY_READ.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(calculator.readMemory());
                updateInputField();
            }
        });

        buttons[BUTTONS.MEMORY_PLUS.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculator.addToMemory(getValue());
            }
        });

        buttons[BUTTONS.MEMORY_MINUS.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculator.subFromMemory(getValue());
            }
        });*/
    }

    private void initGui() {
        Container pane = getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());

        inputField = new JTextField();
        Font font = new Font(inputField.getFont().getFontName(), Font.PLAIN, 14);
        inputField.setFont(font);
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
