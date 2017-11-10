package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import smile.data.parser.DelimitedTextParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea De Castri on 10/11/2017.
 *
 */
public class CsvBuilder {

    private static final String NEW_LINE_SEPARATOR = "\n";

    public static byte[] createCsv(List<String> independentLabels, double[][] data, int[] y) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        CSVPrinter csvPrinter = new CSVPrinter(stringBuilder, csvFileFormat);

        independentLabels.add("Prediction");
        csvPrinter.printRecord(independentLabels);

        for(int i=0; i<data.length; i++){
            List<Object> list = new ArrayList<>();
            for(int j=0; j<data[i].length; j++){
                list.add(data[i][j]);
            }
            list.add(y[i]);
            csvPrinter.printRecord(list);
        }

        return csvPrinter.getOut().toString().getBytes();
    }

}
