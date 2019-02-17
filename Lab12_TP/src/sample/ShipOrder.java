package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class ShipOrder implements Serializable {
    private List<ShipItem> list;
    private int id;
    private String shipTo, address, city, country;

    ShipOrder() {
        list = new ArrayList<>();
    }

    ShipOrder(List<ShipItem> list, int id, String shipTo, String address, String city, String country) {
        this.list = list;
        this.id = id;
        this.shipTo = shipTo;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    List<ShipItem> getList() {
        return list;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getShipTo() {
        return shipTo;
    }

    void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    String getCountry() {
        return country;
    }

    void setCountry(String country) {
        this.country = country;
    }

    Element createElement(Document doc) {
        Element root = doc.createElement("shiporder");
        String
                id = String.valueOf(getId()),
                shipto = getShipTo(),
                address = getAddress(),
                city = getCity(),
                country = getCountry();
        root.setAttribute("orderid", id);
        root.setAttribute("shipto", shipto);
        root.setAttribute("address", address);
        root.setAttribute("city", city);
        root.setAttribute("country", country);
        return root;
    }

    @Override
    public String toString() {
        return "Ship Order #"+getId();
    }
}
