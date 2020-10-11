import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

class AlertBox {
	
	static void display(String title, String message) {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(150);
		
		Label label = new Label(message);
		
		//Close Button
		Button closeButton = new Button("Close this window");
		closeButton.setOnAction(e -> window.close());
		
		//Vertical stacking of label and closeButton
		VBox layout = new VBox(20);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setSpacing(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		
		window.setScene(scene);
		window.showAndWait();
		
	}
}