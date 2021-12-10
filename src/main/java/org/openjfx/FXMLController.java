package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    TextField robotsTextField;
    @FXML
    TextField positionsTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void onClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("simulatorScene.fxml"));
        Parent registerPageParent = loader.load();
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        SimulatorScene simulatorScene = loader.getController();
        simulatorScene.setItems(Integer.parseInt(robotsTextField.getText()), Integer.parseInt(positionsTextField.getText()));
        appStage.show();
    }

}
