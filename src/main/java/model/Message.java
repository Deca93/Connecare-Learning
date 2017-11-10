package model;

import model.interfaces.IMessage;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Andrea De Castri on 10/11/2017.
 *
 */
@XmlRootElement
public class Message implements IMessage {

    private boolean isError;
    private String message;

    public Message(boolean isError, String message){
        super();
        this.isError = isError;
        this.message = message;
    }

    @Override
    public boolean isError() {
        return this.isError;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
