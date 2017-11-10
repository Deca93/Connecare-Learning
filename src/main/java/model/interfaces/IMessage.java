package model.interfaces;

/**
 * Created by Andrea De Castri on 10/11/2017.
 *
 */
public interface IMessage {

    /**
     * Returns if the response is an error
     * @return True if there is an error
     */
    boolean isError();

    /**
     * Returns the message of the response
     * @return Message of the response
     */
    String getMessage();

}
