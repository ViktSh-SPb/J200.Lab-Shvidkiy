package com.example.j200_labshvidkiy.repositories;

import com.example.j200_labshvidkiy.Dto.ClientDto;
import com.example.j200_labshvidkiy.entities.AddressEntity;
import com.example.j200_labshvidkiy.entities.ClientEntity;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class ClientRepository {
    @PersistenceContext
    private EntityManager em;

    public ClientEntity findClientById(int id){
        ClientEntity client = em.find(ClientEntity.class, id);
        return client;
    }
    public List<ClientEntity> findAll(){
        return new ArrayList<>(em.createNativeQuery("select * from j200_mysql.client;", ClientEntity.class).getResultList());
    }

    public void create(ClientEntity entity) {
        em.persist(entity);
        em.flush();
    }

    public void update(ClientEntity entity){
        em.merge(entity);
        em.flush();
    }

    public void remove(ClientEntity client){
        em.merge(client);
        em.remove(client);
    }
}
