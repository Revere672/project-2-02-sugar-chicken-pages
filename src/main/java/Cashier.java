
import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Cashier {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button cashier;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;

    // Buttons for all of the serving size options
    @FXML
    private Button button_bowl;

    @FXML
    private Button button_plate;

    @FXML
    private Button button_big_plate;

    @FXML
    private Button button_cub_meal;

    @FXML
    private Button button_carte;

    @FXML
    private Button button_confirm;

    // Buttons for all of the beverage options
    @FXML
    private Button button_water_cup;

    @FXML
    private Button button_fountain_drink;

    @FXML
    private Button button_water_bottle;

    @FXML
    private Button button_gatorade;

    @FXML
    private Button button_juice;

    @FXML
    private Button button_milk;

    // Buttons for all of the appetizer options
    @FXML
    private Button button_chicken_egg_roll;

    @FXML
    private Button button_veggie_spring_roll;

    @FXML
    private Button button_rangoon;

    @FXML
    private Button button_apple_pie_roll;

    // Buttons for all of the side options
    @FXML
    private Button button_fried_rice;

    @FXML
    private Button button_white_rice;

    @FXML
    private Button button_chow_mein;

    @FXML
    private Button button_super_greens;

    // Buttons for all of the protein options
    @FXML
    private Button button_bourbon_chicken;

    @FXML
    private Button button_orange_chicken;

    @FXML
    private Button button_teriyaki_chicken;

    @FXML
    private Button button_angus_steak;

    @FXML
    private Button button_kung_pao_chicken;

    @FXML
    private Button button_honey_sesame_chicken;

    @FXML
    private Button button_honey_walnut_shrimp;

    @FXML
    private Button button_beijing_beef;

    @FXML
    private Button button_mushroom_chicken;

    @FXML
    private Button button_string_bean_chicken;

    @FXML
    private Button button_broccoli_beef;

    @FXML
    private Button button_black_pepper_chicken;

    @FXML
    private Button button_sweet_fire_chicken;

    public void updateButtonStates() {
        System.out.println(GUIRunner.isManager);
        boolean isManager = GUIRunner.isManager;
        cashier.setDisable(!isManager);
        inventory.setDisable(!isManager);
        employees.setDisable(!isManager);
        order_history.setDisable(!isManager);
    }

    public void switch_to_cashier1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/cashier1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switch_to_cashier2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/cashier2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void changeToEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("employees");
    }

    @FXML
    private void changeToInventory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory");
    }

    @FXML
    private void changeToOrderHistory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("order_history");
    }
}