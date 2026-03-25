/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.impl;

import com.mvcjava.sagt.javafx.dao.impl.SaleDAOImpl;
import com.mvcjava.sagt.javafx.dao.interfaces.SaleDAO;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import com.mvcjava.sagt.javafx.service.interfaces.SaleService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class SaleServiceImpl implements SaleService {
    
    private final SaleDAO saleDAO;
    
    public SaleServiceImpl() {
        this.saleDAO = new SaleDAOImpl();
    }

    @Override
    public List<HeaderSaleWithClient> getAllHeaders() {
        return saleDAO.findAllHeaders();
    }

    @Override
    public List<DetailSaleWithProduct> getDetailBySaleId(UUID saleId) {
        return saleDAO.findDetailBySaleId(saleId);
    }

    @Override
    public void updateHeader(UUID id, Map<String, Object> updates) throws BusinessException {
        if (updates == null || updates.isEmpty()) return;
        
        if (updates.containsKey("numero_factura")) {
            String billNumber = updates.get("numero_factura").toString().trim();
            if (billNumber.isEmpty()) {
                throw new BusinessException("El número de factura no puede estar vacío");
            }
            if (billNumber.length() > 20) {
                throw new BusinessException("El numero de factura no puede superar los 20 carácteres");
            }
            boolean exists = saleDAO.billNumberExists(id, billNumber);
            if (exists) {
                throw new BusinessException("Ya existe otra venta con el número de fáctura: " + billNumber);
            }
            
            //normalizar a minusculas
            updates.put("numero_factura", billNumber.toLowerCase());
            
            //validar metodo de pago
            if (updates.containsKey("metodo_pago")) {
                String raw = updates.get("metodo_pago").toString();
                try {
                    PaymentMethod.fromString(raw);
                    updates.put("metodo_pago", raw.trim().toLowerCase());
                } catch (IllegalArgumentException ex) {
                    throw new BusinessException("Metodo de pago inválido: " + raw);
                }
            }
        }
        
        saleDAO.updateHeader(id, updates);
    }

    @Override
    public void updateDetail(UUID id, Map<String, Object> updates) throws BusinessException {
        if (updates == null || updates.isEmpty()) return;
        saleDAO.updateDetail(id, updates);
    }

    @Override
    public void saveChanges(Map<UUID, Map<String, Object>> headersToUpdate, Map<UUID, Map<String, Object>> detailsToUpdate) throws BusinessException {
        for (Map.Entry<UUID, Map<String, Object>> entry : headersToUpdate.entrySet()) {
            updateHeader(entry.getKey(), entry.getValue());
        }
        
        for (Map.Entry<UUID, Map<String, Object>> entry : detailsToUpdate.entrySet()) {
            updateDetail(entry.getKey(), entry.getValue());
        }
    }
}
