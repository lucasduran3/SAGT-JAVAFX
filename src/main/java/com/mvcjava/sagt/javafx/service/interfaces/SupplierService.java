/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import java.util.UUID;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lucas
 */
public interface SupplierService {
    Supplier getSupplier(UUID id);
    List<Supplier> getAll();
    void createSupplier(Supplier supplier) throws BusinessException;
    void updateSupplier(UUID id, Map<String, Object> updates) throws BusinessException;
    void deleteSupplier(UUID id) throws BusinessException;
}
