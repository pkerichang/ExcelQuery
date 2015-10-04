package main;

import data.PostProcess;
import data.Vector;
import io.ExcelReader;
import io.ExcelWriter;

import java.util.List;

/**
 * Scratch ND4J api
 */
public class Scratch {
    /**
     * Run test.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
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

    }
}
