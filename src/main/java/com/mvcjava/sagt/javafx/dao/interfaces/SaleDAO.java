/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface SaleDAO {
    List<HeaderSaleWithClient> findAllHeaders();
    List<DetailSaleWithProduct> findDetailBySaleId(UUID saleId);
}
