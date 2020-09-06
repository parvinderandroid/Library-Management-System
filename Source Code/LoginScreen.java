import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;

import javafx.stage.Modality;
import javafx.stage.Stage;

class LoginScreen {
	
	private String username;
	private String password;
	
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
		
		//Name Field
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
		
		//Login Button
		Button loginButton = new Button("Log in");
		GridPane.setConstraints(loginButton, 1, 2);
		loginButton.setOnAction(e -> {
			username = nameField.getText().replace("~", "");
			password = passField.getText();
			try {
				if(!SignupStorage.usernameExists(username))
					AlertBox.display("Error", "Username doesn't exist");
				else if(!SignupStorage.passwordMatches(username, password))
					AlertBox.display("Error", "Wrong Password");
				else {
					new StudentDatabaseScreen().display(username);
					window.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Add labels, fields and loginButton to the grid
		grid.getChildren().addAll(nameLabel, nameField, passLabel, passField, loginButton);
		
		Scene scene = new Scene(grid, 300, 200);
		
		window.setScene(scene);
		window.showAndWait();
		
	}
	
}