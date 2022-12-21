package hu.petrik.etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EtlapController {
    private EtlapDB db;
    @FXML
    private TableView<Etlap> etlapTable;
    @FXML
    private TableColumn<Etlap, String> nevCol;
    @FXML
    private TableColumn<Etlap, String> kategoriaCol;
    @FXML
    private TableColumn<Etlap, Integer> arCol;
    @FXML
    private Spinner<Integer> szazalekInput;
    @FXML
    private Spinner<Integer> fixInput;
    @FXML
    private ListView<String> leiras;
    @FXML
    private Button ujEtel;
    @FXML
    private Button etelTorles;

    @FXML
    private void initialize() {
        nevCol.setCellValueFactory(new PropertyValueFactory<>("nev"));
        kategoriaCol.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        arCol.setCellValueFactory(new PropertyValueFactory<>("ar"));
        szazalekInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50,5,5));
        fixInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 3000,50,50));
        try {
            db = new EtlapDB();
            etlapBeolvas();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    private void etlapBeolvas() throws SQLException {
        List<Etlap> etelek = db.etlapBeolvas();
        etlapTable.getItems().clear();
        etlapTable.getItems().addAll(etelek);
    }

    @FXML
    public void rendezesLista(Event event) {
        etlapTable.getOnSort();
    }

    @FXML
    public void leirasMutatas(Event event) {
        leiras.getItems().clear();
        leiras.getItems().add(getSelectedEtel().getLeiras());
    }

    private Etlap getSelectedEtel() {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert(Alert.AlertType.WARNING,
                    "Előbb válasszon ki egy ételt a táblázatból", "");
            return null;
        }
        return etlapTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void ujEtelClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("etlap2-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 300);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Étel létrehozása");
        stage.setScene(scene);
        EtlapController2 controller = fxmlLoader.getController();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                stage.close();
                try {
                    etlapBeolvas();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    @FXML
    public void etelTorlesClick(ActionEvent actionEvent) {
        Etlap selected = getSelectedEtel();
        if (selected == null) return;

        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,"Biztos, hogy törölni szeretné a kiválasztott ételt?","");
        if (optionalButtonType.isEmpty() || !optionalButtonType.get().equals(ButtonType.OK) && !optionalButtonType.get().equals(ButtonType.YES)){
            return;
        }
        try {
            if (db.deleteEtel(selected.getId())) {
                alert(Alert.AlertType.WARNING, "Sikeres Törlés!", "");
            }else{
                alert(Alert.AlertType.WARNING, "Sikertelen törlés!", "");
            }
            etlapBeolvas();
        } catch (SQLException e) {
            sqlAlert(e);
        }
        leiras.getItems().clear();
    }
}