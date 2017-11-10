package utils;

import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.ArffParser;
import smile.data.parser.DelimitedTextParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public class Parser {

    private Parser(){
        super();
    }

    public static AttributeDataset retrieveData(InputStream inputStream, FileType type, int responseIndex) throws IOException, ParseException {
        switch (type){
            case CSV:
                return parseCsvFile(inputStream, responseIndex);
            case ARFF:
                return parseArffFile(inputStream, responseIndex);
            default:
                return null;
        }
    }

    /**
     * Retrieve data from file
     * @param inputStream Input file for model creation
     * @param responseIndex Index of dependent variable
     */
    private static AttributeDataset parseCsvFile(InputStream inputStream, int responseIndex) throws IOException, ParseException {
        // Read the first line and take dependent variable title.
        /*Scanner scanner = new Scanner(inputStream);
        String[] firstLine = scanner.nextLine().split(",");
        int numVariables = firstLine.length;
        String yTitle = firstLine[numVariables-1];*/

        // Accept only file with Columns names, Nominal attribute for dependent variable (because used in a classification)
        // Data must be delimited by comma
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setColumnNames(true);
        parser.setDelimiter(",");
        parser.setResponseIndex(new NominalAttribute("Y"), responseIndex);
        AttributeDataset dataset = parser.parse("Dataset", inputStream);
        return dataset;
    }

    private static AttributeDataset parseArffFile(InputStream inputStream, int responseIndex) throws IOException, ParseException {
        ArffParser parser = new ArffParser();
        parser.setResponseIndex(responseIndex);
        AttributeDataset dataset = parser.parse(inputStream);
        return dataset;
    }

}
