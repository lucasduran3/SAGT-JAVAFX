/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.service.impl.ClientServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.ClientService;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class ClientSaveService extends Service<Void>{
    private final ClientService clientService;
    
    private Set<Client> newClients;
    private Map<UUID, Map<String, Object>> clientsToUpdate;
    private Set<Client> clientsToDelete;
    
    public ClientSaveService() {
        clientService = new ClientServiceImpl();
    }
    
    public void setData(Set<Client> newClients, Map<UUID, Map<String, Object>> clientsToUpdate, Set<Client> clientsToDelete) {
        this.newClients = newClients;
        this.clientsToDelete = clientsToDelete;
        this.clientsToUpdate = clientsToUpdate;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task() {
            @Override
            protected Void call() throws Exception {
                clientService.saveChanges(newClients, clientsToUpdate, clientsToDelete);
                return null;
            }  
        };
    }
    
    
}
