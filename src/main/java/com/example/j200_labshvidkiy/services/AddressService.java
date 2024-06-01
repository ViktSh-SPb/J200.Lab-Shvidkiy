package com.example.j200_labshvidkiy.services;

import com.example.j200_labshvidkiy.Dto.AddressDto;
import com.example.j200_labshvidkiy.entities.AddressEntity;
import com.example.j200_labshvidkiy.entities.ClientEntity;
import com.example.j200_labshvidkiy.repositories.AddressRepository;
import com.example.j200_labshvidkiy.repositories.ClientRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class AddressService {
    @EJB
    AddressRepository addressRepository;
    @EJB
    ClientRepository clientRepository;

    public void createForClient(AddressDto address, ClientEntity client) {
        AddressEntity entity = new AddressEntity();
        entity.setIp(address.getIp());
        entity.setMac(address.getMac());
        entity.setModel(address.getModel());
        entity.setAddress(address.getAddress());
        entity.setClient(client.getClientid());
        addressRepository.create(entity);
    }

    public Collection<AddressDto> getAddressesByClientId(int id) {
        ClientEntity entity = clientRepository.findClientById(id);
        Collection<AddressDto> addresses = new ArrayList<>();
        for (AddressEntity addr : entity.getAddressesByClientid()) {
            addresses.add(AddressDto.builder()
                    .addressid(addr.getAddressid())
                    .ip(addr.getIp())
                    .mac(addr.getMac())
                    .model(addr.getModel())
                    .address(addr.getAddress())
                    .build()
            );
        }
        return addresses;
    }

    public void update(AddressDto address){
        AddressEntity entity = new AddressEntity();
        AddressEntity entityDB = addressRepository.findAddressById(address.getAddressid());
        entity.setAddressid(address.getAddressid());
        entity.setIp(address.getIp());
        entity.setMac(address.getMac());
        entity.setModel(address.getModel());
        entity.setAddress(address.getAddress());
        entity.setClient(address.getClientid());
        System.out.println("кому добавляем адрес? "+ address.getClientid());
        if(!entityDB.equals(entity)){
            addressRepository.update(entity);
        }
    }

    public void removeById(Integer addressId){
        addressRepository.remove(addressRepository.findAddressById(addressId));
    }
    public void remove(AddressEntity address){
        addressRepository.remove(address);
    }

    public String checkAddress(AddressDto address){
        String msg = "ok";
        if(!address.getAddress().matches("[а-яА-Я0-9.,\\- ]{1,200}"))
            msg="address";
        if(!address.getModel().matches(".{1,100}"))
            msg="model";
        if(!address.getMac().matches("[a-fA-F0-9]{2}-[a-fA-F0-9]{2}-[a-fA-F0-9]{2}-[a-fA-F0-9]{2}-[a-fA-F0-9]{2}-[a-fA-F0-9]{2}"))
            msg="mac";
        if(address.getIp().matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")){
            String[] octets =address.getIp().split("\\.");
            for(String octet:octets){
                System.out.println("octet:"+ octet);
                if(Integer.parseInt(octet)>256) msg="ip";
            }
        } else msg="ip";
        return msg;
    }
}
