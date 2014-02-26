import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by gorz on 25.02.14.
 */
public class Listeners {

    public static class onNumber implements ActionListener {

        private GUI gui;

        public onNumber(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton)e.getSource();
            gui.onInput(source.getText());
        }
    }

    public static class onOperation implements ActionListener {

        private Calculator.OPERATION operation;
        private GUI gui;

        public onOperation(GUI gui, Calculator.OPERATION op) {
            operation = op;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onOperationButtonClick(operation);
        }
    }

    public static class onEqual implements ActionListener {

        private GUI gui;

        public onEqual(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onEqualButtonClick();
        }
    }

    public static class onFunction implements ActionListener {

        private Calculator.FUNCTION function;
        private GUI gui;

        public onFunction(GUI gui, Calculator.FUNCTION function) {
            this.function = function;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onFunctionButtonClick(function);
        }
    }

    public static class onBackspace implements ActionListener {

        private GUI gui;

        public onBackspace(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onBackspaceButtonClick();
        }
    }

    public static class onChangeSign implements ActionListener {

        private GUI gui;

        public onChangeSign(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onChangeSignButtonClick();
        }
    }

    public static class onClear implements ActionListener {

        private GUI gui;

        public onClear(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onClearButtonClick();
        }

    }

    public static class onClearAll implements ActionListener {

        private GUI gui;

        public onClearAll(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onClearAllButtonClick();
        }

    }

    public static class onMemory implements ActionListener {

        private GUI gui;
        private Calculator.MEMORY action;

        public onMemory(GUI gui, Calculator.MEMORY action) {
            this.gui = gui;
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.onMemoryButtonClick(action);
        }

    }

    public static class onKeyPress implements KeyListener {

        private GUI gui;

        public onKeyPress(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case ',':
                    gui.onInput(e.getKeyChar()+"");
                    return;
                case '.':
                    gui.onInput(",");
                    return;
                case '+':
                    gui.onOperationButtonClick(Calculator.OPERATION.PLUS);
                    return;
                case '-':
                    gui.onOperationButtonClick(Calculator.OPERATION.MINUS);
                    return;
                case '*':
                    gui.onOperationButtonClick(Calculator.OPERATION.MUL);
                    return;
                case '/':
                    gui.onOperationButtonClick(Calculator.OPERATION.DIV);
                    return;
                case '%':
                    gui.onFunctionButtonClick(Calculator.FUNCTION.PERCENT);
                    return;
            }
            switch (e.getKeyCode()) {
                case KeyEvent.VK_EQUALS:
                case KeyEvent.VK_ENTER:
                    gui.onEqualButtonClick();
                    return;
                case KeyEvent.VK_ESCAPE:
                    gui.onClearAllButtonClick();
                    return;
                case KeyEvent.VK_BACK_SPACE:
                    gui.onBackspaceButtonClick();

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }

}
