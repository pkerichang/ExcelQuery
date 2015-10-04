package main;

import data.PostProcess;
import data.Vector;
import io.ExcelReader;
import io.ExcelWriter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * The main launcher for this project.
 */
public class Launcher extends Application {

    /**
     * Runs the main application
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("QExcel");
        Button btn = new Button();
        btn.setText("Parse database.xls");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Starting Conversion...");
                ExcelReader reader = new ExcelReader("database.xls", 6);
                List<Vector> data = reader.getData();
                if (data == null) {
                    System.out.println(reader.getErrMsg());
                } else {
                    PostProcess.postProcess(data);
                    ExcelWriter writer = new ExcelWriter("foo.xls");
                    writer.writeAll(PostProcess.toOutput(data));
                    writer.close();
                }
                System.out.println("Done.");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
