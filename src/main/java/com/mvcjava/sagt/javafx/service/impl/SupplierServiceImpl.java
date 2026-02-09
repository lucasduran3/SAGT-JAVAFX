/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.impl;

import com.mvcjava.sagt.javafx.dao.impl.SupplierDAOImpl;
import com.mvcjava.sagt.javafx.dao.interfaces.SupplierDAO;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import com.mvcjava.sagt.javafx.service.interfaces.SupplierService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 *
 * @author lucas
 */
public class SupplierServiceImpl implements SupplierService {
    private final SupplierDAO supplierDAO;
    
    public SupplierServiceImpl() {
        this.supplierDAO = new SupplierDAOImpl();
    }

    @Override
    public List<Supplier> getAll() {
        return supplierDAO.findAll();
    }

    @Override
    public void createSupplier(Supplier supplier) throws BusinessException {
        boolean alreadyExists = supplierDAO.alreadyExists(supplier.getName(), supplier.getPhone());
        
        Pattern phonePattern = Pattern.compile("^\\d{8,12}$");
        boolean validPhone = phonePattern.matcher(supplier.getPhone()).matches();
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$");
        boolean validEmail = emailPattern.matcher(supplier.getEmail()).matches();
        
        if (alreadyExists) {
            throw new BusinessException("Este proveedor ya existe.");
        } else if (!validPhone) {
            throw new BusinessException("El numero telefono no tiene el formato correcto.");
        } else if (!validEmail) {
            throw new BusinessException("El email no tiene el formato correcto");
        }
        
        supplierDAO.addSupplier(supplier);
    }

    @Override
    public void deleteSupplier(UUID id) throws BusinessException {
        Supplier currentSupplier = supplierDAO.getSupplier(id);
        if (currentSupplier == null) {
            throw new BusinessException("El proveedor que desea eliminar no existe.");
        }
        
        supplierDAO.deleteSupplier(id);
    }

    @Override
    public Supplier getSupplier(UUID id) {
        return supplierDAO.getSupplier(id);
    }

    @Override
    public void updateSupplier(UUID id, Map<String, Object> updates) throws BusinessException {
        Supplier currentSupplier = supplierDAO.getSupplier(id);
        if (currentSupplier == null) {
            throw new BusinessException("El proveedor que desea actualizar no existe.");
        }
        
        String newName = updates.containsKey("nombre") ? (String) updates.get("nombre")
                : currentSupplier.getName();
        
        String newPhone = updates.containsKey("telefono") ? (String) updates.get("telefono")
                : currentSupplier.getPhone();
        
        boolean dataChanged = updates.containsKey("nombre") || updates.containsKey("telefono");
        
        if (dataChanged) {
            boolean exists = supplierDAO.alreadyExists(newName, newPhone);
            if (exists) {
                throw new BusinessException("Ya existe un proveedor con el mismo nombre y telefono.");
            }
        }
        
        supplierDAO.updateSupplier(id, updates);
    }
}
