package utils;

import smile.data.Attribute;
import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.ArffParser;
import smile.data.parser.DelimitedTextParser;
import utils.interfaces.IParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public class Parser implements IParser {

    private InputStream inputStream;
    private FileType fileType;
    private int responseIndex;
    private List<String> independentVariables;
    private String dependentVariable;

    public Parser(InputStream inputStream, FileType fileType, int responseIndex){
        this(inputStream, fileType);
        this.responseIndex = responseIndex;
    }

    public Parser(InputStream inputStream, FileType fileType){
        super();
        this.inputStream = inputStream;
        this.fileType = fileType;
        this.responseIndex = -1;
    }

    @Override
    public AttributeDataset parse() throws IOException, ParseException {
        switch (this.fileType){
            case CSV:
                return this.parseCsvFile();
            case ARFF:
                return this.parseArffFile();
            default:
                return null;
        }
    }

    @Override
    public List<String> getIndependentVariables() {
        return this.independentVariables;
    }

    @Override
    public String getIndependentVariablesToString() {
        String result = "";
        for(int i=0; i<this.independentVariables.size(); i++){
            result += this.independentVariables.get(i);
            if(i<this.independentVariables.size() - 1){
                result += ", ";
            }
        }
        return result;
    }

    @Override
    public String getDependentVariable() {
        return this.dependentVariable;
    }

    private AttributeDataset parseCsvFile() throws IOException, ParseException {
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setColumnNames(true);
        parser.setDelimiter(",");
        if(this.responseIndex != -1){
            parser.setResponseIndex(new NominalAttribute("Y"), responseIndex);
        }
        AttributeDataset dataset = parser.parse("Dataset", inputStream);
        this.parseColumnsNames(dataset);

        return dataset;
    }

    private AttributeDataset parseArffFile() throws IOException, ParseException {
        ArffParser parser = new ArffParser();
        if(this.responseIndex != -1){
            parser.setResponseIndex(this.responseIndex);
        }
        AttributeDataset dataset = parser.parse(this.inputStream);
        this.parseColumnsNames(dataset);

        return dataset;
    }

    private void parseColumnsNames(AttributeDataset dataset){
        // Parse the variables labels
        this.independentVariables = new ArrayList<>();
        Attribute[] attributes = dataset.attributes();
        for(Attribute attribute: attributes){
            String name = attribute.getName().replaceAll("\"", "");
            this.independentVariables.add(name);
        }
        if(this.responseIndex != -1){
            this.dependentVariable = dataset.response().attribute().getName().replaceAll("\"", "");;
        }
    }

}
