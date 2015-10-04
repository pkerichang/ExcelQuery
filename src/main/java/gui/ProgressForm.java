package gui;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * A modal dialog showing current progress.
 */
public class ProgressForm {
    private final Stage dialogStage;
    private final Label lab;
    private final ProgressBar pb;

    /**
     * Create a new ProgressForm instance.
     *
     * @param owner the owner of this dialog
     * @param title the dialog title.
     */
    public ProgressForm(Window owner, String title) {
        dialogStage = new Stage();
        pb = new ProgressBar();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle(title);
        dialogStage.initOwner(owner);

        // PROGRESS BAR
        lab = new Label();

        pb.setProgress(-1F);

        final VBox vb = new VBox();
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(lab, pb);

        Scene scene = new Scene(vb);
        dialogStage.setScene(scene);
    }

    /**
     * bind the progress bar
     *
     * @param task the task to bind the progress bar to.
     */
    public void activateProgressBar(final Task<?> task) {
        lab.textProperty().bind(task.messageProperty());
        pb.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    /**
     * Get the stage of this dialog.
     *
     * @return the dialog stage.
     */
    public Stage getDialogStage() {
        return dialogStage;
    }
}
