/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.enums.ClientType;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lucas
 */
public class ClientViewModel {
    private final Client client;
    private final StringProperty cuitCuil;
    private final StringProperty companyName;
    private final ObjectProperty<ClientType> clientType;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty address;
    private final StringProperty location;
    private final StringProperty province;
    private final ObjectProperty<Date> entryDate;
    private final BooleanProperty selected;
    
    private boolean isNew;

    public ClientViewModel(Client client) {
        this.client = client;

        this.cuitCuil = new SimpleStringProperty(client.getCuitCuil());
        this.companyName = new SimpleStringProperty(client.getCompanyName());
        this.clientType = new SimpleObjectProperty<>(client.getClientType());
        this.phone = new SimpleStringProperty(client.getPhone());
        this.email = new SimpleStringProperty(client.getEmail());
        this.address = new SimpleStringProperty(client.getAddress());
        this.location = new SimpleStringProperty(client.getLocation());
        this.province = new SimpleStringProperty(client.getProvince());
        this.entryDate = new SimpleObjectProperty<>(client.getEntryDate());
        this.selected = new SimpleBooleanProperty(false);
        
        this.isNew = false;

        setupSync();
    }

    private void setupSync() {
        cuitCuil.addListener((obs, oldVal, newVal) -> client.setCuitCuil(newVal));
        companyName.addListener((obs, oldVal, newVal) -> client.setCompanyName(newVal));
        clientType.addListener((obs, oldVal, newVal) -> client.setClientType(newVal));
        phone.addListener((obs, oldVal, newVal) -> client.setPhone(newVal));
        email.addListener((obs, oldVal, newVal) -> client.setEmail(newVal));
        address.addListener((obs, oldVal, newVal) -> client.setAddress(newVal));
        location.addListener((obs, oldVal, newVal) -> client.setLocation(newVal));
        province.addListener((obs, oldVal, newVal) -> client.setProvince(newVal));
        entryDate.addListener((obs, oldVal, newVal) -> client.setEntryDate(newVal));
    }

    public StringProperty cuitCuilProperty() {
        return cuitCuil;
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public ObjectProperty<ClientType> clientTypeProperty() {
        return clientType;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public StringProperty provinceProperty() {
        return province;
    }

    public ObjectProperty<Date> entryDateProperty() {
        return entryDate;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public UUID getId() {
        return client.getId();
    }

    public Date getEntryDate() {
        return client.getEntryDate();
    }

    public Client getModel() {
        return client;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClientViewModel other = (ClientViewModel) obj;
        return Objects.equals(client, other.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client);
    }
    
    public void setIsNew(boolean value) {
        this.isNew = value;
    }
    
    public boolean getIsNew() {
        return this.isNew;
    }
}
