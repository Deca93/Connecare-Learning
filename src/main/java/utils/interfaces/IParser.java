package utils.interfaces;

import smile.data.AttributeDataset;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Andrea De Castri on 11/11/2017.
 *
 */
public interface IParser {

    /**
     * Parse a text file. Format accepted: CSV, ARFF.
     * CSV must have the columns titles and it must be delimited from COMMA.
     * @return AttributeDataset
     * @throws IOException
     * @throws ParseException
     */
    AttributeDataset parse() throws IOException, ParseException;

    /**
     * Returns the list of the independent variables
     * @return Independent variables
     */
    List<String> getIndependentVariables();

    /**
     * Returns the independent variables in a single string
     * @return Independent variables
     */
    String getIndependentVariablesToString();

    /**
     * Returna the dependent variable
     * @return Dependent variable
     */
    String getDependentVariable();

}
