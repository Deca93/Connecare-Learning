package model;

import model.interfaces.IModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
@XmlRootElement(name = "model")
public class Model implements IModel{

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public String getId(){
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getProviderId() {
        return this.providerId;
    }

    @Override
    public String getProvider(){
        return this.provider;
    }

    @Override
    public String getDescription(){
        return this.description;
    }

    @Override
    public String getIndependentVariables() {
        return this.xVariables;
    }

    @Override
    public String getDependentVariable() {
        return this.yVariable;
    }

    @Override
    public boolean isTrainable() {
        return this.isTrainable;
    }

    @Override
    public boolean hasGlobalAccess() {
        return this.hasGlobalAccess;
    }

    @Override
    public boolean isOnlineTrainable() {
        return this.isOnlineTrainable;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

}
