
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CashierController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switch_to_cashier1(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/cashier1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        DisplayReceipt.updateRecipt(scene);
        stage.setScene(scene);
        stage.show();
    }

    
    public void switch_to_cashier2(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/cashier2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        DisplayReceipt.updateRecipt(scene);
        stage.setScene(scene);
        stage.show();
    }

    public void sendToReceipt(ActionEvent event) throws IOException{
        scene = ((Node)event.getSource()).getScene();
        DisplayReceipt.updateRecipt(scene);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DisplayReceipt.load();
        } catch (SQLException | ClassNotFoundException ex) {
        }
    }

    //Buttons for all of the serving size options
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

    //Buttons for all of the beverage options
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

    //Buttons for all of the appetizer options
    @FXML
    private Button button_chicken_egg_roll;

    @FXML
    private Button button_veggie_spring_roll;

    @FXML
    private Button button_rangoon;

    @FXML
    private Button button_apple_pie_roll;

    //Buttons for all of the side options
    @FXML
    private Button button_fried_rice;

    @FXML
    private Button button_white_rice;

    @FXML
    private Button button_chow_mein;

    @FXML
    private Button button_super_greens;

    //Buttons for all of the protein options
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

}