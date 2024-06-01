package com.example.j200_labshvidkiy.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "address", schema = "j200_mysql")
public class AddressEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "addressid", nullable = false)
    private int addressid;
    @Basic
    @Column(name = "ip", nullable = false, length = 25)
    private String ip;
    @Basic
    @Column(name = "mac", nullable = false, length = 20)
    private String mac;
    @Basic
    @Column(name = "model", nullable = false, length = 100)
    private String model;
    @Basic
    @Column(name = "address", nullable = false, length = 200)
    private String address;
    @Basic
    @Column(name = "client", nullable = true)
    private Integer client;
    @ManyToOne
    @JoinColumn(name = "client", referencedColumnName = "clientid", insertable = false, updatable = false)
    private ClientEntity clientByClient;

    public int getAddressid() {
        return addressid;
    }

    public void setAddressid(int addressid) {
        this.addressid = addressid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressEntity that = (AddressEntity) o;

        if (addressid != that.addressid) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (mac != null ? !mac.equals(that.mac) : that.mac != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = addressid;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        return result;
    }

    public ClientEntity getClientByClient() {
        return clientByClient;
    }

    public void setClientByClient(ClientEntity clientByClient) {
        this.clientByClient = clientByClient;
    }
}
