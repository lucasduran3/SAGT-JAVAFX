/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface ClientService {
    Set<Client> getAll();
    Client getById(UUID id);
    void createClient(Client client) throws BusinessException;
    void updateClient(UUID id, Map<String, Object> updates) throws BusinessException;
    void deleteClient(UUID id) throws BusinessException;
    void saveChanges(Set<Client> newClients, Map<UUID, Map<String, Object>> clientsToUpdate, Set<Client> clientsToDelete) throws BusinessException;
}
