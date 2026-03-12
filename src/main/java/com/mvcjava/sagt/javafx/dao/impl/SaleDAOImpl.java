/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.impl;

import com.mvcjava.sagt.javafx.config.DatabaseManager;
import com.mvcjava.sagt.javafx.dao.interfaces.SaleDAO;
import com.mvcjava.sagt.javafx.dao.model.SaleHeader;
import com.mvcjava.sagt.javafx.dao.model.SaleDetail;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import com.mvcjava.sagt.javafx.exception.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaleDAOImpl implements SaleDAO {

    @Override
    public List<HeaderSaleWithClient> findAllHeaders() {
        List<HeaderSaleWithClient> result = new ArrayList<>();

        String sql = "SELECT "
                + "vc.id, "
                + "vc.numero_factura, "
                + "vc.fecha, "
                + "vc.id_cliente, "
                + "vc.total, "
                + "vc.metodo_pago::text AS metodo_pago, "
                + "vc.cargado_por, "
                + "c.razon_social AS cliente_razon_social "
                + "FROM app.ventas_cabecera vc "
                + "LEFT JOIN app.clientes c ON vc.id_cliente = c.id "
                + "ORDER BY vc.fecha DESC";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SaleHeader header = mapResultSetToHeader(rs);
                String clientName = rs.getString("cliente_razon_social");
                result.add(new HeaderSaleWithClient(header, clientName));
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Error al obtener cabeceras de venta.", ex);
        }

        return result;
    }


    @Override
    public List<DetailSaleWithProduct> findDetailBySaleId(UUID idVenta) {
        List<DetailSaleWithProduct> result = new ArrayList<>();

        String sql = "SELECT "
                + "vd.id, "
                + "vd.id_venta, "
                + "vd.id_producto, "
                + "vd.precio_unitario, "
                + "vd.subtotal, "
                + "vd.cantidad, "
                + "p.nombre AS producto_nombre "
                + "FROM app.ventas_detalle vd "
                + "LEFT JOIN app.productos p ON vd.id_producto = p.id "
                + "WHERE vd.id_venta = ? "
                + "ORDER BY p.nombre";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idVenta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SaleDetail detail = mapResultSetToDetalle(rs);
                    String productName = rs.getString("producto_nombre");
                    result.add(new DetailSaleWithProduct(detail, productName));
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException (
                    "Error al obtener detalle de venta con id: " + idVenta, ex);
        }

        return result;
    }

    private SaleHeader mapResultSetToHeader(ResultSet rs) throws SQLException {
        SaleHeader vc = new SaleHeader();
        vc.setId((UUID) rs.getObject("id"));
        vc.setBillNumber(rs.getString("numero_factura"));
        vc.setDate(rs.getTimestamp("fecha"));
        vc.setClientId((UUID) rs.getObject("id_cliente"));
        vc.setTotal(rs.getFloat("total"));
        vc.setPaymentMethod(PaymentMethod.fromString(rs.getString("metodo_pago")));
        vc.setLoadedBy((UUID) rs.getObject("cargado_por"));
        return vc;
    }

    private SaleDetail mapResultSetToDetalle(ResultSet rs) throws SQLException {
        SaleDetail vd = new SaleDetail();
        vd.setId((UUID) rs.getObject("id"));
        vd.setSaleId((UUID) rs.getObject("id_venta"));
        System.out.println(rs.getObject("id_producto"));
        vd.setProductId((UUID) rs.getObject("id_producto"));
        vd.setUnitPrice(rs.getFloat("precio_unitario"));
        vd.setSubtotal(rs.getFloat("subtotal"));
        vd.setAmmount(rs.getInt("cantidad"));
        return vd;
    }
}
