/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Supplier;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface SupplierDAO {
    Supplier getSupplier(UUID id);
    List<Supplier> findAll();
    void addSupplier(Supplier supplier);
    void updateSupplier(UUID id, Map<String, Object> updates);
    void deleteSupplier(UUID id);
    boolean alreadyExists(String name, String phone);
}
