/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import java.sql.Timestamp;
import java.util.UUID;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lucas
 */
public class SaleViewModel {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty billNumber = new SimpleStringProperty();
    private final ObjectProperty<Timestamp> date = new SimpleObjectProperty<>();
    private final StringProperty clientName = new SimpleStringProperty();
    private final FloatProperty total = new SimpleFloatProperty();
    private final StringProperty paymentMethod = new SimpleStringProperty();
    
    private final HeaderSaleWithClient source;
    
    public SaleViewModel(HeaderSaleWithClient dto) {
        this.source = dto;
        this.id.set(dto.getHeader().getId());
        this.billNumber.set(dto.getHeader().getBillNumber());
        this.date.set(dto.getHeader().getDate());
        this.clientName.set(dto.getClientCompanyName());
        this.total.set(dto.getHeader().getTotal());
        this.paymentMethod.set(dto.getHeader().getPaymentMethod().name());
    }
    
    public ObjectProperty<UUID> idProperty() { return this.id; }
    public StringProperty billNumberProperty() { return this.billNumber; }
    public ObjectProperty<Timestamp> dateProperty() { return this.date; }
    public StringProperty clientNameProperty() { return this.clientName; }
    public FloatProperty totalProperty() { return this.total; }
    public StringProperty paymentMethodProperty() { return this.paymentMethod; }
    
    public UUID getId() { return this.id.get(); }
    public String getBillNumber() { return this.billNumber.get(); }
    public Timestamp getDate() { return this.date.get(); }
    public String getClientName() { return this.clientName.get(); }
    public Float getTotal() { return this.total.get(); }
    public String getPaymentMethod() { return this.paymentMethod.get(); }
    
    public HeaderSaleWithClient getSource() { return this.source; }
}
