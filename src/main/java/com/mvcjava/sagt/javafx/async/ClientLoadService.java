/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.service.impl.ClientServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.ClientService;
import java.util.Set;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class ClientLoadService extends Service<Set<Client>> {
    private ClientService clientService;
    
    public ClientLoadService() {
        this.clientService = new ClientServiceImpl();
    }

    @Override
    protected Task<Set<Client>> createTask() {
        return new Task() {
            @Override
            protected Set<Client> call() throws Exception {
                return clientService.getAll();
            }
        };
    }
}
