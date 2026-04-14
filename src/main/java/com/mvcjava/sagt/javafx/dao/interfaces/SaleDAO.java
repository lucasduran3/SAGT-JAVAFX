/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dao.model.SaleDetail;
import com.mvcjava.sagt.javafx.dao.model.SaleHeader;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface SaleDAO {
    List<HeaderSaleWithClient> findAllHeaders();
    List<DetailSaleWithProduct> findDetailBySaleId(UUID saleId);
    boolean billNumberExists(UUID excludeId, String billNumber);
    void updateHeader(UUID id, Map<String, Object> updates);
    void updateDetail(UUID id, Map<String, Object> updates);
    UUID insertHeader(SaleHeader header);
    UUID insertDetail(SaleDetail detail);
    void deleteHeader(UUID saleId);
    void deleteDetail(UUID detailId);
    SaleHeader findSaleById(UUID saleId);
    SaleDetail findDetailById(UUID detailId);
}
