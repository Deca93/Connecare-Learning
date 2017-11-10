package utils;

import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.ArffParser;
import smile.data.parser.DelimitedTextParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public class Parser {

    private Parser(){
        super();
    }

    public static AttributeDataset retrieveTrainingData(InputStream inputStream, FileType type, int responseIndex) throws IOException, ParseException {
        switch (type){
            case CSV:
                return parseCsvTrainingFile(inputStream, responseIndex);
            case ARFF:
                return parseArffTrainingFile(inputStream, responseIndex);
            default:
                return null;
        }
    }

    public static AttributeDataset retrieveTestingData(InputStream inputStream, FileType type) throws IOException, ParseException {
        switch (type){
            case CSV:
                return parseCsvTestingFile(inputStream);
            case ARFF:
                return parseArffTestingFile(inputStream);
            default:
                return null;
        }
    }

    /**
     * Retrieve data from file
     * @param inputStream Input file for model creation
     * @param responseIndex Index of dependent variable
     */
    private static AttributeDataset parseCsvTrainingFile(InputStream inputStream, int responseIndex) throws IOException, ParseException {
        // Accept only file with Columns names, Nominal attribute for dependent variable (because used in a classification)
        // Data must be delimited by comma
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setColumnNames(true);
        parser.setDelimiter(",");
        parser.setResponseIndex(new NominalAttribute("Y"), responseIndex);
        AttributeDataset dataset = parser.parse("Dataset", inputStream);
        return dataset;
    }

    private static AttributeDataset parseArffTrainingFile(InputStream inputStream, int responseIndex) throws IOException, ParseException {
        ArffParser parser = new ArffParser();
        parser.setResponseIndex(responseIndex);
        AttributeDataset dataset = parser.parse(inputStream);
        return dataset;
    }

    private static AttributeDataset parseCsvTestingFile(InputStream inputStream) throws IOException, ParseException {
        // Data must be delimited by comma
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setColumnNames(true);
        parser.setDelimiter(",");
        AttributeDataset dataset = parser.parse("Dataset", inputStream);
        return dataset;
    }

    private static AttributeDataset parseArffTestingFile(InputStream inputStream) throws IOException, ParseException {
        ArffParser parser = new ArffParser();
        AttributeDataset dataset = parser.parse(inputStream);
        return dataset;
    }

}
