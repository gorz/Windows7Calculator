import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

}
