import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

class IndexScreen {
	
	private Scene scene;
	private Scene adminScene;
	private Scene studentScene;
	private int attemptsRemaining = 5;
	
	void display() {
		
		Stage window = new Stage();
		window.setTitle("Library Management System");
		
		//Create Grid
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(10);
		grid.setVgap(20);
		grid.setAlignment(Pos.CENTER);
		
		//Admin Button
		Button adminButton = new Button("Administrator");
		GridPane.setConstraints(adminButton, 0, 0);
		adminButton.setOnAction(e -> window.setScene(adminScene));
		
		//Student Button
		Button studentButton = new Button("Student");
		GridPane.setConstraints(studentButton, 0, 1);
		studentButton.setOnAction(e -> window.setScene(studentScene));
		
		//Add the buttons to grid layout
		grid.getChildren().addAll(adminButton, studentButton);
		
		//Set dimensions
		scene = new Scene(grid, 500, 300);
		
		//Admin Layout
		GridPane adminLayout = new GridPane();
		adminLayout.setPadding(new Insets(10, 10, 10, 10));
		adminLayout.setHgap(10);
		adminLayout.setVgap(15);
		adminLayout.setAlignment(Pos.CENTER);
		
		//Admin Password Label
		Label label = new Label("Administrator Password:");
		GridPane.setConstraints(label, 0, 0);
		
		//Admin Password Field
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(200);
		GridPane.setConstraints(passwordField, 1, 0);
		
		//Admin Login Button
		Button loginButton = new Button("Log in");
		GridPane.setConstraints(loginButton, 1, 1);
		loginButton.setOnAction(e -> {
			if(passwordField.getText().equals("adminbit@123g")) {
				window.close();
				new AdminDatabaseScreen().display();
			}
			else {
				attemptsRemaining--;
				AlertBox.display("Error", "Incorrect Password\n" + "Attempts Remaining = " + attemptsRemaining);
				if(attemptsRemaining == 0) {
					passwordField.setPromptText("Maximum Attempts Made");
					passwordField.setDisable(true);
					loginButton.setDisable(true);
				}
			}
		});
		
		//Admin Back Button
		Button adminBackButton = new Button("Go Back");
		GridPane.setConstraints(adminBackButton, 1, 2);
		adminBackButton.setOnAction(e -> window.setScene(scene));
		
		//Add label and buttons to Admin Layout
		adminLayout.getChildren().addAll(label, passwordField, loginButton, adminBackButton);
		
		adminScene = new Scene(adminLayout, 500, 300);
		
		//Student Layout
		GridPane studentLayout = new GridPane();
		studentLayout.setPadding(new Insets(10, 10, 10, 10));
		studentLayout.setHgap(10);
		studentLayout.setVgap(15);
		studentLayout.setAlignment(Pos.CENTER);
		
		//Student Sign in button
		Button studentSigninButton = new Button("Sign in");
		GridPane.setConstraints(studentSigninButton, 0, 0);
		studentSigninButton.setOnAction(e -> new LoginScreen().display());
		
		//Student Signup button
		Button studentSignupButton = new Button("Sign up");
		GridPane.setConstraints(studentSignupButton, 0, 1);
		studentSignupButton.setOnAction(e -> new SignupScreen().display());
		
		//Student Back Button
		Button studentBack = new Button("Go Back");
		GridPane.setConstraints(studentBack, 0, 2);
		studentBack.setOnAction(e -> window.setScene(scene));
		
		//Add buttons to Student Layout
		studentLayout.getChildren().addAll(studentSigninButton, studentSignupButton, studentBack);
		
		studentScene = new Scene(studentLayout, 500, 300);
		
		window.setScene(scene);
		window.show();
		
	}
	
}