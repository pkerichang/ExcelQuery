package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 * A log window UI inside a scroll pane.
 */
public class LoggerUI extends ScrollPane {

    private final TextArea logArea;

    /**
     * Create a new LoggerUI instance.
     */
    public LoggerUI() {
        super();
        logArea = new TextArea();
        logArea.setPrefColumnCount(30);
        logArea.setPrefRowCount(10);
        logArea.setWrapText(true);

        setContent(logArea);
        setFitToWidth(true);
    }

    /**
     * Logs the given message.
     *
     * @param msg the message to log.
     */
    public void logMsg(String msg) {
        logArea.appendText(msg);
        logArea.appendText("\n");
    }

    /**
     * Clears the log window.
     */
    public void clear() {
        logArea.clear();
    }
}
