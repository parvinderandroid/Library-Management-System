import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

class ConfirmBox {
	
	private static boolean answer;
	
	static boolean display(String title, String message) {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label(message);
		
		//Yes Button
		Button yesButton = new Button("Yes");
		yesButton.setOnAction( e-> {
			answer = true;
			window.close();
		});
		
		//No Button
		Button noButton = new Button("No");
		noButton.setOnAction(e -> {
			answer = false;
			window.close();
		});
		
		//Horizontal stacking of yesButton and noButton
		HBox buttonLayout = new HBox(10);
		buttonLayout.getChildren().addAll(label, yesButton, noButton);
		buttonLayout.setAlignment(Pos.CENTER);
		
		//Create a Border Pane layout
		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(10, 10, 10, 10));
		//Set label at top
		layout.setTop(label);
		//Set buttonLayout at bottom
		layout.setBottom(buttonLayout);
		
		BorderPane.setMargin(label, new Insets(10));
		
		Scene scene = new Scene(layout);
		
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
		
	}
	
}