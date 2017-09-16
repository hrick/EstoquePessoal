package br.com.hrick.estoquepessoal.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by henrique.pereira on 14/09/2017.
 */

public class Stock  {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PATH_PICTURE = "pathPicture";
    public static final String LATITUDE = "locationStockLatitude";
    public static final String LONGITUDE = "locationStocklongitude";
    public static final String RESPONSIBLE_NUMBER = "responsibleNumber";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = RESPONSIBLE_NUMBER)
    private String responsibleNumber;
    @DatabaseField(columnName = LATITUDE)
    private Double locationStockLatitude;
    @DatabaseField(columnName = LONGITUDE)
    private Double locationStocklongitude;
    @DatabaseField(columnName = PATH_PICTURE)
    private String pathPicture;
    @ForeignCollectionField()
    private ForeignCollection<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathPicture() {
        return pathPicture;
    }

    public void setPathPicture(String pathPicture) {
        this.pathPicture = pathPicture;
    }

    public ForeignCollection<Product> getProducts() {
        return products;
    }

    public void setProducts(ForeignCollection<Product> products) {
        this.products = products;
    }



    public String getResponsibleNumber() {
        return responsibleNumber;
    }

    public void setResponsibleNumber(String responsibleNumber) {
        this.responsibleNumber = responsibleNumber;
    }

    public Double getLocationStockLatitude() {
        return locationStockLatitude;
    }

    public void setLocationStockLatitude(Double locationStockLatitude) {
        this.locationStockLatitude = locationStockLatitude;
    }

    public Double getLocationStocklongitude() {
        return locationStocklongitude;
    }

    public void setLocationStocklongitude(Double locationStocklongitude) {
        this.locationStocklongitude = locationStocklongitude;
    }
}
