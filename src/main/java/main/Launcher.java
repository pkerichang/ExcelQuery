package main;

import data.PostProcess;
import gui.LoggerUI;
import gui.ProgressForm;
import io.ExcelWriter;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The main launcher for this project.
 */
public class Launcher extends Application {

    private LoggerUI log;

    @Override
    public void init() {
        log = new LoggerUI();
    }

    /**
     * Runs the main application
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Properties prop = loadProperties();
        String dbFile = getDBName(prop);

        primaryStage.setTitle("QExcel");
        primaryStage.setScene(new Scene(makeGridPane(primaryStage, dbFile), 400, 250));
        primaryStage.sizeToScene();
        primaryStage.setOnCloseRequest(we -> saveProperties(prop));
        primaryStage.show();
    }

    @SuppressWarnings("FieldCanBeLocal")
    private String PROP_FILE = "qexcel.config";

    private Properties loadProperties() {
        File f = new File(PROP_FILE);
        Properties prop = new Properties();
        if (f.exists()) {
            try {
                prop.loadFromXML(new FileInputStream(f));
            } catch (IOException e) {
                prop.clear();
            }
        }
        return prop;
    }

    private void saveProperties(Properties prop) {
        try {
            prop.storeToXML(new FileOutputStream(new File(PROP_FILE)), "");
        } catch (IOException ignored) {
        }
    }

    private String getDBName(Properties prop) {
        String key = "database";
        if (prop.containsKey(key)) {
            return prop.getProperty(key);
        } else {
            File test = new File("database.xls");
            if (test.exists()) {
                String ans;
                try {
                    ans = test.getCanonicalPath();
                } catch (IOException e) {
                    return "";
                }
                prop.setProperty(key, ans);
                return ans;
            } else {
                test = new File("../database.xls");
                if (test.exists()) {
                    String ans;
                    try {
                        ans = test.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                    prop.setProperty(key, ans);
                    return ans;
                }
                return "";
            }
        }
    }


    /**
     * Run the main routine.
     *
     * @param stage  the parent stage.
     * @param dbFile the database file name.
     */
    public void runMain(Stage stage, String dbFile) {
        log.logMsg("Start loading database...");
        ProgressForm pForm = new ProgressForm(stage.getScene().getWindow(), "Loading Database");
        String temp;
        try {
            String dir = new File(dbFile).getParentFile().getCanonicalPath();
            temp = new File(dir, "out.xls").getCanonicalPath();
        } catch(IOException e) {
            temp = "out.xls";
        }
        String output = temp;
        LoadDBTask task = new LoadDBTask(dbFile);
        pForm.activateProgressBar(task);
        task.setOnSucceeded(event -> {
            pForm.getDialogStage().close();
            ExcelWriter writer = new ExcelWriter(output);
            writer.writeAll(PostProcess.toOutput(task.getValue()));
            writer.close();
            log.logMsg("Loading done.");
        });

        pForm.getDialogStage().show();
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    private GridPane makeGridPane(Stage stage, String dbFile) {
        Label label1 = new Label("DB File: ");

        TextField textField = new TextField(dbFile);
        textField.setPrefColumnCount(20);

        Button dbBtn = new Button();
        dbBtn.setText("...");
        dbBtn.setOnAction(event -> {
            File file = new FileChooser().showOpenDialog(stage);
            if (file != null) {
                try {
                    textField.setText(file.getCanonicalPath());
                } catch (IOException e) {
                    String msg = "Cannot read selected file: " + e.getMessage();
                    log.logMsg(msg);
                }
            }
        });

        Button runBtn = new Button();
        runBtn.setText("Run");
        runBtn.setOnAction(event -> runMain(stage, textField.getText()));

        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(0, 10, 0, 10));

        grid.add(label1, 0, 0);
        grid.add(textField, 1, 0);
        GridPane.setFillWidth(textField, true);
        grid.add(dbBtn, 2, 0);
        grid.add(log, 0, 1, 3, 1);
        GridPane.setFillWidth(log, true);
        GridPane.setFillHeight(log, true);
        grid.add(runBtn, 0, 2, 3, 1);
        GridPane.setFillWidth(runBtn, true);
        GridPane.setHalignment(runBtn, HPos.CENTER);
        return grid;
    }
}
