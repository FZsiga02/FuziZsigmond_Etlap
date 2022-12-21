package hu.petrik.etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

public class EtlapController2 {
    @FXML
    private TextField nevField;
    @FXML
    private TextArea leirasArea;
    @FXML
    private MenuButton menuButton;
    @FXML
    private MenuItem eloetelMenuItem;
    @FXML
    private MenuItem foetelMenuItem;
    @FXML
    private MenuItem desszertMenuItem;
    @FXML
    private Spinner<Integer> arSpinner;
    @FXML
    private Button hozzaadasButton;
    @FXML
    private javafx.scene.control.Button closeButton;

    public void initialize(){
        arSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(500,10000,1000, 10));
    }

    @FXML
    public void menuItem1Select(ActionEvent actionEvent) {
        menuButton.setText(eloetelMenuItem.getText());
    }

    @FXML
    public void menuItem2Select(ActionEvent actionEvent) {
        menuButton.setText(foetelMenuItem.getText());
    }

    @FXML
    public void menuItem3Select(ActionEvent actionEvent) {
        menuButton.setText(desszertMenuItem.getText());
    }

    @FXML
    public void hozzaadasClick(ActionEvent actionEvent) {
        String nev = nevField.getText().trim();
        String leiras = leirasArea.getText().trim();
        int ar = arSpinner.getValue();
        String kategoria = menuButton.getText().trim();
        Etlap etel = new Etlap(nev, leiras, ar, kategoria);
        EtlapDB db = null;
        try {
            db = new EtlapDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (db.createEtlap(etel)){
                alert(Alert.AlertType.WARNING, "Sikeres felvétel!", "");
            }else{
                alert(Alert.AlertType.WARNING, "Sikertelen felvétel!", "");
            }
        } catch (SQLException e) {
            Platform.runLater(() -> {
                alert(Alert.AlertType.WARNING, "Hiba történt az adatbázis kapcsolat kialakításakor!",
                        e.getMessage());
            });
        }
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }
}
