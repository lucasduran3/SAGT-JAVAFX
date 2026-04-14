/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.SaleHeader;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import java.sql.Date;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final ObjectProperty<Date> date = new SimpleObjectProperty<>();
    private final StringProperty clientName = new SimpleStringProperty();
    private final FloatProperty total = new SimpleFloatProperty();
    private final ObjectProperty<PaymentMethod> paymentMethod = new SimpleObjectProperty<>();
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    
    private final HeaderSaleWithClient source;
    private final SaleHeader header;
    
    private boolean isNew;
    
    public SaleViewModel(HeaderSaleWithClient dto) {
        this.source = dto;
        this.isNew = false;
        this.header = dto.getHeader();
        
        this.id.set(dto.getHeader().getId());
        this.billNumber.set(dto.getHeader().getBillNumber());
        this.date.set(dto.getHeader().getDate());
        this.clientName.set(dto.getClientCompanyName());
        this.total.set(dto.getHeader().getTotal());
        this.paymentMethod.set(dto.getHeader().getPaymentMethod());
        
        this.setupSync();
    }
    
    private void setupSync() {
        this.billNumber.addListener((obs, old, val) -> header.setBillNumber(val));
        this.date.addListener((obs, old, val) -> header.setDate(val));
        this.total.addListener((obs, old, val) -> header.setTotal(val.floatValue()));
        this.paymentMethod.addListener((obs, old, val) -> header.setPaymentMethod(val));
    }
    
    public ObjectProperty<UUID> idProperty() { return this.id; }
    public StringProperty billNumberProperty() { return this.billNumber; }
    public ObjectProperty<Date> dateProperty() { return this.date; }
    public StringProperty clientNameProperty() { return this.clientName; }
    public FloatProperty totalProperty() { return this.total; }
    public ObjectProperty<PaymentMethod> paymentMethodProperty() { return this.paymentMethod; }
    public BooleanProperty selectedProperty() { return this.selected; }
    
    public UUID getId() { return this.id.get(); }
    public String getBillNumber() { return this.billNumber.get(); }
    public Date getDate() { return this.date.get(); }
    public String getClientName() { return this.clientName.get(); }
    public Float getTotal() { return this.total.get(); }
    public PaymentMethod getPaymentMethod() { return this.paymentMethod.get(); }
    public boolean isSelected() { return this.selected.get(); }
    
    public HeaderSaleWithClient getSource() { return this.source; }
    public SaleHeader getHeader() { return this.header; }
    
    public boolean getIsNew() { return isNew; }
    public void setIsNew(boolean isNew) { this.isNew = isNew; }
}
