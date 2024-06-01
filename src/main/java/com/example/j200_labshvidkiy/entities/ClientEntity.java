package com.example.j200_labshvidkiy.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "client", schema = "j200_mysql", catalog = "")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "clientid", nullable = false)
    private int clientid;
    @Basic
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;
    @Basic
    @Column(name = "clienttype", nullable = false, length = 20)
    private String clienttype;
    @Basic
    @Column(name = "added", nullable = false)
    private LocalDate added;
    @OneToMany(mappedBy = "clientByClient", fetch = FetchType.EAGER)
    private Collection<AddressEntity> addressesByClientid;

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) {
        this.clientid = clientid;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public LocalDate getAdded() {
        return added;
    }

    public void setAdded(LocalDate added) {
        this.added = added;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntity client = (ClientEntity) o;

        if (clientid != client.clientid) return false;
        if (clientName != null ? !clientName.equals(client.clientName) : client.clientName != null) return false;
        if (clienttype != null ? !clienttype.equals(client.clienttype) : client.clienttype != null) return false;
        if (added != null ? !added.equals(client.added) : client.added != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clientid;
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        result = 31 * result + (clienttype != null ? clienttype.hashCode() : 0);
        result = 31 * result + (added != null ? added.hashCode() : 0);
        return result;
    }

    public Collection<AddressEntity> getAddressesByClientid() {
        return addressesByClientid;
    }

    public void setAddressesByClientid(Collection<AddressEntity> addressesByClientid) {
        this.addressesByClientid = addressesByClientid;
    }
}
