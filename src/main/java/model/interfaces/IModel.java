package model.interfaces;

import java.util.Date;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public interface IModel {

    /**
     * Returns the model id
     * @return Model id
     */
    String getId();

    /**
     * Returns the model name
     * @return Model name
     */
    String getName();

    /**
     * Returns the provider id
     * @return Provider id
     */
    String getProviderId();

    /**
     * Returns the provider name
     * @return Provider name
     */
    String getProvider();

    /**
     * Returns the description of the model
     * @return Model description
     */
    String getDescription();

    /**
     * Returns the label of the independent variables
     * @return Independent variables labels
     */
    String getIndependentVariables();

    /**
     * Returns the label of the dependent variable
     * @return Dependent variables label
     */
    String getDependentVariable();

    /**
     * Returns true if the model is trainable
     * @return True if model is trainable
     */
    boolean isTrainable();

    /**
     * Returns the flag for global access to the model
     * @return True if the model can be used/trained from everyone
     */
    boolean hasGlobalAccess();

    /**
     * Indicates if the model is trainable with new data
     * @return True if the model can be trainable with data after testing
     */
    boolean isOnlineTrainable();

    /**
     * Returns the date of the model creation
     * @return Date model creation
     */
    Date getCreationDate();

    /**
     * Returns the date of the last update
     * @return Date of last update
     */
    Date getLastUpdateDate();

}
