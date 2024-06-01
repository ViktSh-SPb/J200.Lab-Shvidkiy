package com.example.j200_labshvidkiy.repositories;

import com.example.j200_labshvidkiy.Dto.AddressDto;
import com.example.j200_labshvidkiy.Dto.ClientDto;
import com.example.j200_labshvidkiy.entities.AddressEntity;
import com.example.j200_labshvidkiy.entities.ClientEntity;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AddressRepository {
    @PersistenceContext
    private EntityManager em;

    public void create(AddressEntity entity) {
        em.persist(entity);
        em.flush();
    }

    public void update (AddressEntity entity){
        em.merge(entity);
        em.flush();
    }

    public AddressEntity findAddressById(int id){
        AddressEntity address = em.find(AddressEntity.class, id);
        return address;
    }

    public void remove(AddressEntity address){
        em.merge(address);
        em.remove(address);
    }
}
