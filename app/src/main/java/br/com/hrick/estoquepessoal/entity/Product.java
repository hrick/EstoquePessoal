package br.com.hrick.estoquepessoal.entity;

import com.j256.ormlite.field.DatabaseField;

import java.sql.Date;

/**
 * Created by henrique.pereira on 15/09/2017.
 */

public class Product {

    public static final String RESPONSIBLE_NUMBER = "responsibleNumber";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String QUANTITY = "quantity";
    public static final String NAME = "name";
    public static final String ID = "id";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = QUANTITY)
    private int quantity;
    @DatabaseField(columnName = RESPONSIBLE_NUMBER)
    private String responsibleNumber;
    @DatabaseField(columnName = EXPIRATION_DATE)
    private Date expirationDate;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getResponsibleNumber() {
        return responsibleNumber;
    }

    public void setResponsibleNumber(String responsibleNumber) {
        this.responsibleNumber = responsibleNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
