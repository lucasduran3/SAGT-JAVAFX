/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import com.mvcjava.sagt.javafx.dao.model.SaleDetail;
import com.mvcjava.sagt.javafx.dao.model.SaleHeader;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface SaleService {
    List<HeaderSaleWithClient> getAllHeaders();
    List<DetailSaleWithProduct> getDetailBySaleId(UUID saleId);
    void updateHeader(UUID id, Map<String, Object> updates) throws BusinessException;
    void updateDetail(UUID id, Map<String, Object> updates) throws BusinessException;    
    UUID createHeader(SaleHeader header) throws BusinessException;
    UUID createDetail(SaleDetail detail) throws BusinessException;
    void deleteHeader(UUID saleId) throws BusinessException;
    void deleteDetail(UUID detailId) throws BusinessException;
    
    void saveChanges(
            List<SaleHeader> newSales,
            List<SaleDetail> newDetails,
            Map<UUID, Map<String, Object>> headersToUpdate,
            Map<UUID, Map<String, Object>> detailsToUpdate,
            List<UUID> headersToDelete,
            List<UUID> detailsToDelete
    ) throws BusinessException;
}
