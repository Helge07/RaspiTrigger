package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorController implements Initializable {

	private enum Operation {
		NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE
	}

	private Operation nextOperation = Operation.NONE;

	@FXML
	Button eval;

	@FXML
	private Label lastResult;

	@FXML
	private Label input;

	@FXML
	private Button add;

	@FXML
	private Button subtract;

	@FXML
	private Button multiply;

	@FXML
	private Button divide;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		switchEvalVisible();
	}

	@FXML
	private void handleInput(ActionEvent event) {
		


		
		Object newCharacter = ((Button) event.getSource()).getText();
		if (input.getText().contains(",") && newCharacter.equals(",")) {
			return;
		}
		input.setText(input.getText() + newCharacter);
		switchEvalVisible();
	}

	@FXML
	void prepareOperation(ActionEvent event) {
		if (event.getSource() == add) {
			nextOperation = Operation.ADD;
		} else if (event.getSource() == subtract) {
			nextOperation = Operation.SUBTRACT;
		} else if (event.getSource() == multiply) {
			nextOperation = Operation.MULTIPLY;
		} else if (event.getSource() == divide) {
			nextOperation = Operation.DIVIDE;
		}
		if (!input.getText().isBlank() && !lastResult.getText().isBlank()) {
			evaluate();
		} else if (!input.getText().isBlank()) {
			lastResult.setText(input.getText());
			input.setText("");
		}
		switchEvalVisible();
	}

	@FXML
	private void clear() {
		input.setText("");
		lastResult.setText("");
	}

	@FXML
	private void evaluate() {
		Float lastResultFloat = parseFloatFromLabel(lastResult);
		Float inputFloat = parseFloatFromLabel(input);
		Float result = 0f;
		switch (nextOperation) {
		case ADD:
			result = lastResultFloat + inputFloat;
			break;
		case SUBTRACT:
			result = lastResultFloat - inputFloat;
			break;
		case MULTIPLY:
			result = lastResultFloat * inputFloat;
			break;
		case DIVIDE:
			if (inputFloat == 0) {
				lastResult.setText("Error");
				input.setText("");
				nextOperation = Operation.NONE;
				return;
			}
			result = lastResultFloat / inputFloat;
			break;
		default:
			return;
		}
		input.setText("");
		lastResult.setText(String.valueOf(result).replace(".", ","));
		nextOperation = Operation.NONE;
		switchEvalVisible();
	}

	private float parseFloatFromLabel(Label label) {
		String labelText = label.getText();
		if (labelText.isBlank()) {
			return 0f;
		}
		return Float.parseFloat(labelText.replace(",", "."));
	}

	private void switchEvalVisible() {
		if (nextOperation == Operation.NONE || input.getText().isBlank() || lastResult.getText().isBlank()
				|| lastResult.getText().equals("Error")) {
			eval.setVisible(false);
		} else {
			eval.setVisible(true);
		}
	}
}