import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {

    private Connection connection;
    private TextField idField, lastNameField, firstNameField, miField, addressField, cityField, stateField, telephoneField, emailField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize database connection
        initializeDatabase();

        // Create UI components
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Form components
        idField = new TextField();
        lastNameField = new TextField();
        firstNameField = new TextField();
        miField = new TextField();
        addressField = new TextField();
        cityField = new TextField();
        stateField = new TextField();
        telephoneField = new TextField();
        emailField = new TextField();

        Button viewButton = new Button("View");
        Button insertButton = new Button("Insert");
        Button updateButton = new Button("Update");

        // Button actions
        viewButton.setOnAction(e -> viewStaff());
        insertButton.setOnAction(e -> insertStaff());
        updateButton.setOnAction(e -> updateStaff());

        // Form layout
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(5);

        form.addRow(0, new Label("ID:"), idField, viewButton);
        form.addRow(1, new Label("Last Name:"), lastNameField);
        form.addRow(2, new Label("First Name:"), firstNameField);
        form.addRow(3, new Label("MI:"), miField);
        form.addRow(4, new Label("Address:"), addressField);
        form.addRow(5, new Label("City:"), cityField);
        form.addRow(6, new Label("State:"), stateField);
        form.addRow(7, new Label("Telephone:"), telephoneField);
        form.addRow(8, new Label("Email:"), emailField);

        // Button bar
        HBox buttonBar = new HBox(10);
        buttonBar.getChildren().addAll(insertButton, updateButton);
        form.add(buttonBar, 1, 9);

        root.setCenter(form);

        // Create scene and stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Staff Management App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish connection to SQLite database file
            connection = DriverManager.getConnection("jdbc:sqlite:staff.db");

            // Create Staff table if not exists
            createStaffTable();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to connect to database.");
        }
    }

    private void createStaffTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Staff (" +
                "id CHAR(9) PRIMARY KEY," +
                "lastName VARCHAR(15)," +
                "firstName VARCHAR(15)," +
                "mi CHAR(1)," +
                "address VARCHAR(20)," +
                "city VARCHAR(20)," +
                "state CHAR(2)," +
                "telephone CHAR(10)," +
                "email VARCHAR(40)" +
                ")";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewStaff() {
        String id = idField.getText().trim();
        if (!id.isEmpty()) {
            try {
                String sql = "SELECT * FROM Staff WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    lastNameField.setText(resultSet.getString("lastName"));
                    firstNameField.setText(resultSet.getString("firstName"));
                    miField.setText(resultSet.getString("mi"));
                    addressField.setText(resultSet.getString("address"));
                    cityField.setText(resultSet.getString("city"));
                    stateField.setText(resultSet.getString("state"));
                    telephoneField.setText(resultSet.getString("telephone"));
                    emailField.setText(resultSet.getString("email"));
                } else {
                    showAlert("Error", "Staff member not found.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to fetch data.");
            }
        } else {
            showAlert("Error", "Please enter an ID.");
        }
    }

    private void insertStaff() {
        String id = idField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String mi = miField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();

        try {
            String sql = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, mi);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, city);
            preparedStatement.setString(7, state);
            preparedStatement.setString(8, telephone);
            preparedStatement.setString(9, email);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "Staff member inserted successfully.");
            } else {
                showAlert("Error", "Failed to insert staff member.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to insert staff member.");
        }
    }

    private void updateStaff() {
        String id = idField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String mi = miField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();

        try {
            String sql = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, " +
                    "state = ?, telephone = ?, email = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, mi);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, city);
            preparedStatement.setString(6, state);
            preparedStatement.setString(7, telephone);
            preparedStatement.setString(8, email);
            preparedStatement.setString(9, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Staff member updated successfully.");
            } else {
                showAlert("Error", "Failed to update staff member.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update staff member.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connection != null) {
            connection.close();
        }
    }
}
