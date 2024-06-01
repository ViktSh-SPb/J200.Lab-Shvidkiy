package com.example.j200_labshvidkiy.services;
import com.example.j200_labshvidkiy.dto.AddressDto;
import com.example.j200_labshvidkiy.dto.ClientDto;
import com.example.j200_labshvidkiy.entities.AddressEntity;
import com.example.j200_labshvidkiy.entities.ClientEntity;
import com.example.j200_labshvidkiy.repositories.ClientRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
@Singleton
public class ClientService {
    @EJB
    ClientRepository clientRepository;
    @EJB
    AddressService addressService;

    public void create(ClientDto client, Collection<AddressDto> addresses){
        ClientEntity clientity = new ClientEntity();
        clientity.setClientName(client.getClientName());
        clientity.setClienttype(client.getClientType());
        clientity.setAdded(LocalDate.parse(client.getAdded()));
        System.out.println("id: "+clientity.getClientid());
        clientRepository.create(clientity);
        System.out.println("id: "+clientity.getClientid());
        Collection<AddressEntity> addrentity = new ArrayList<>();
        for(AddressDto addr: addresses){
            addressService.createForClient(addr, clientity);
            System.out.println("ip: "+addr.getIp());
        }
    }

    public ClientDto getClientById(int id){
        ClientEntity entity = clientRepository.findClientById(id);
        return(ClientDto.builder()
                .clientId(entity.getClientid())
                .clientName(entity.getClientName())
                .clientType(entity.getClienttype())
                .added(entity.getAdded().toString())
                .build()
        );
    }

    public void update(ClientDto client, Collection<AddressDto> addresses){
        for(AddressDto addr: addresses){
            addressService.update(addr);
        }
        ClientEntity entity = new ClientEntity();
        ClientEntity entityDB = clientRepository.findClientById(client.getClientId());
        entity.setClientid(client.getClientId());
        entity.setClientName(client.getClientName());
        entity.setClienttype(client.getClientType());
        entity.setAdded(LocalDate.parse(client.getAdded()));
        if(!entityDB.equals(entity)){
            clientRepository.update(entity);
        }
    }

    public void removeById(Integer clientId){
        for(AddressEntity addr: clientRepository.findClientById(clientId).getAddressesByClientid()){
            addressService.remove(addr);
        }
        clientRepository.remove(clientRepository.findClientById(clientId));
    }

    public boolean findIp(ClientEntity client, String ip){
        Collection<AddressEntity> addresses = client.getAddressesByClientid();
        for (AddressEntity addr:addresses){
            if(addr.getIp().equals(ip)){
                return true;
            }
        }
        return false;
    }

    public String checkClient (ClientDto client, Collection<AddressDto> addresses){
        String msg = "ok";
        for(AddressDto addr:addresses){
            msg = addressService.checkAddress(addr);
            if(msg!="ok")break;
        }
        if(client.getAdded().isEmpty()||LocalDate.parse(client.getAdded()).isBefore(LocalDate.parse("1980-01-01"))||LocalDate.parse(client.getAdded()).isAfter(LocalDate.now())) msg="added";
        if(!client.getClientType().matches("(физическое лицо)|(юридическое лицо)")) msg="type";
        if(!client.getClientName().matches("[а-яА-Я- ,.]{1,100}")) msg="name";
        return msg;
    }
}
