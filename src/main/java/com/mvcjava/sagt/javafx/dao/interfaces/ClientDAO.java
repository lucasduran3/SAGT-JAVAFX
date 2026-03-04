/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Client;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface ClientDAO {
    Set<Client> findAll();
    Client findById(UUID id);
    void addClient(Client client);
    void updateClient(UUID id, Map<String, Object> updates);
    void deleteClient(UUID id);
    boolean alreadyExists(UUID id, String cuit_cuil);
}
