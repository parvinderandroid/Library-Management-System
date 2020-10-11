import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;

import javafx.stage.Modality;
import javafx.stage.Stage;


class SignupScreen {
	
	private String username;
	private String password;
	private String cPassword;
	private String gender;
	private int age;
	
	void display() {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Library Management System");
		
		//Create Grid
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(10);
		grid.setVgap(8);
		
		//Name Label
		Label nameLabel = new Label("Username:");
		GridPane.setConstraints(nameLabel, 0, 0);
		
		//Text Field
		TextField nameField = new TextField();
		nameField.setPromptText("Username");
		GridPane.setConstraints(nameField, 1, 0);
		
		//Password Label
		Label passLabel = new Label("Password:");
		GridPane.setConstraints(passLabel, 0, 1);
		
		//Password Field
		PasswordField passField = new PasswordField();
		passField.setPromptText("Password");
		GridPane.setConstraints(passField, 1, 1);
		
		//Confirm Password Label
		Label passCLabel = new Label("Confirm Password:");
		GridPane.setConstraints(passCLabel, 0, 2);
		
		//Confirm Password Button
		PasswordField passCField = new PasswordField();
		passCField.setPromptText("Re-type Password");
		GridPane.setConstraints(passCField, 1, 2);
		
		//Age Label
		Label ageLabel = new Label("Age:");
		GridPane.setConstraints(ageLabel, 0, 3);
		
		//Age Field
		Spinner<Integer> ageField = new Spinner<>(0, Integer.MAX_VALUE, 1);
		ageField.setPromptText("Age");
		GridPane.setConstraints(ageField, 1, 3);
		
		//Gender Label
		Label genderLabel = new Label("Gender:");
		GridPane.setConstraints(genderLabel, 0, 4);
		
		//Gender selection drop down list
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setPromptText("Select");
		comboBox.getItems().addAll("Male", "Female", "Other");
		GridPane.setConstraints(comboBox, 1, 4);
		
		//Create Button
		Button createButton = new Button("Create account");
		GridPane.setConstraints(createButton, 1, 6);
		createButton.setOnAction(e -> {
			username = nameField.getText().replace("~", "").replace(" ", "");
			password = passField.getText();
			cPassword = passCField.getText();
			age = ageField.getValue();
			gender = comboBox.getValue();
			try {
				if(username == null || username.equals(""))
					AlertBox.display("Error", "Username can not be empty");
				else if(SignupStorage.usernameExists(username))
					AlertBox.display("Error", "Username already taken");
				else if(password == null || password.equals(""))
					AlertBox.display("Error", "Password can not be empty");
				else if(cPassword == null || cPassword.equals(""))
					AlertBox.display("Error", "Confirm your password");
				else if(!password.equals(cPassword))
					AlertBox.display("Error", "Passwords do not match");
				else if(age < 18)
					AlertBox.display("Error", "Students below 18 are not allowed");
				else if(gender==null || gender.equals(""))
					AlertBox.display("Error", "Gender not selected");
				else {
					SignupStorage.writeData(username, password, age, gender);
					AlertBox.display("Success", "Congratulations! Account successfully created");
					window.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Add all nodes to the grid
		grid.getChildren().addAll(nameLabel, nameField, passLabel, passField, passCLabel, passCField, ageLabel, ageField, genderLabel, comboBox, createButton);
		
		Scene scene = new Scene(grid, 300, 250);
		
		window.setScene(scene);
		window.showAndWait();
		
	}
	
}