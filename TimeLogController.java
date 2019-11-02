package timelogproject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class TimeLogController implements Initializable {

    private final String URL = "jdbc:sqlite:timelog.db";
    private Connection connection;

    @FXML
    private ComboBox selectCategory;
    @FXML
    private TextArea Entrydescription;
    @FXML
    private TextField starttime;
    @FXML
    private TextField endtime;
    @FXML
    private TextField totalduration;
    @FXML
    private Slider taskPriority;
    @FXML
    private TextField dodate;
    @FXML
    private TextField duedate;
    @FXML
    private TextField tasktitle;
    @FXML
    private TextArea taskdescription;
    @FXML
    private ColorPicker colorPicker;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectCategory.setItems(FXCollections.observableArrayList("Work", "Reading", "Lunch", "Meeting", "Travelling",
                "Exercise", "Social Media", "Shopping", "Sleeping"));

        try {
            initializeDatabaseConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            showAlert(AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        }
    }

    @FXML
    private void saveAction() {
        try {
            String taskTitle = tasktitle.getText();
            int startTime = Integer.parseInt(starttime.getText());
            int endTime = Integer.parseInt(endtime.getText());
            int totalDuration = endTime - startTime;
            String entryDescription = Entrydescription.getText();
            String taskDescription = taskdescription.getText();
            String doDate = dodate.getText();
            String dueDate = duedate.getText();
            double taskpriority = taskPriority.getValue();
            String colorValue = colorPicker.getValue().toString();

            String query = "INSERT INTO timelog VAlUES("
                    +"'" + taskTitle +"' "
                    +", "+ startTime
                    +", "+ endTime
                    +", '"+ colorValue + "' "
                    +", '"+ totalDuration + "' "
                    +", '"+ entryDescription + "' "
                    +", '"+ taskDescription + "' "
                    +", '"+ doDate + "' "
                    +", '"+ dueDate + "' "
                    +"," + taskpriority
                    + ")";
            Statement statement = connection.createStatement();
            statement.execute(query);
            showAlert(AlertType.INFORMATION, "Task created successfully.", ButtonType.OK);
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        }
    }

    @FXML
    private void clearAllAction() {
        tasktitle.setText("");
        starttime.setText("");
        endtime.setText("");
        Entrydescription.setText("");
        taskdescription.setText("");
        dodate.setText("");
        duedate.setText("");
        taskPriority.setValue(0);
        totalduration.setText("");
    }

    private void showAlert(AlertType alertType, String message, ButtonType buttonType) {
        Alert alert = new Alert(alertType, message, buttonType);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void initializeDatabaseConnection() throws SQLException, ClassNotFoundException {
        connection = DriverManager.getConnection(URL);
    }

}
