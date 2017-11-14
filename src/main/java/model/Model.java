package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.interfaces.IModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.FIELD)
public class Model implements IModel{

    private String id;
    private String name;
    private String providerId;
    private String provider;
    private String description;
    private String xVariables;
    private String yVariable;
    private boolean isTrainable;
    private boolean hasGlobalAccess;
    private boolean isOnlineTrainable;

    private Date creationDate;
    private Date lastUpdateDate;

    public Model(String id, String name, String providerId, String provider, String description, String xVariables, String yVariable,
                 boolean isTrainable, boolean hasGlobalAccess, boolean isOnlineTrainable, Date creationDate, Date lastUpdate){
        super();
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.provider = provider;
        this.description = description;
        this.xVariables = xVariables;
        this.yVariable = yVariable;
        this.isTrainable = isTrainable;
        this.hasGlobalAccess = hasGlobalAccess;
        this.isOnlineTrainable = isOnlineTrainable;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdate;
    }

    @Override
    @JsonProperty("id")
    public String getId(){
        return this.id;
    }

    @Override
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @Override
    @JsonProperty("providerId")
    public String getProviderId() {
        return this.providerId;
    }

    @Override
    @JsonProperty("provider")
    public String getProvider(){
        return this.provider;
    }

    @Override
    @JsonProperty("description")
    public String getDescription(){
        return this.description;
    }

    @Override
    @JsonProperty("independentVariables")
    public String getIndependentVariables() {
        return this.xVariables;
    }

    @Override
    @JsonProperty("dependentVariable")
    public String getDependentVariable() {
        return this.yVariable;
    }

    @Override
    @JsonProperty("trainable")
    public boolean isTrainable() {
        return this.isTrainable;
    }

    @Override
    @JsonProperty("globalAccess")
    public boolean getGlobalAccess() {
        return this.hasGlobalAccess;
    }

    @Override
    @JsonProperty("onlineTrainable")
    public boolean isOnlineTrainable() {
        return this.isOnlineTrainable;
    }

    @Override
    @JsonProperty("creationDate")
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    @JsonProperty("lastUpdate")
    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

}
