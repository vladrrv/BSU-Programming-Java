package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;

public class ShipItem implements Serializable {
    private String title;
    private int quantity;
    private double price;

    ShipItem(String title, int quantity, double price) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    Element createElement(Document doc) {
        Element
                element = doc.createElement("item"),
                title = doc.createElement("title"),
                quantity = doc.createElement("quantity"),
                price = doc.createElement("price");
        title.setTextContent(getTitle());
        quantity.setTextContent(String.valueOf(getQuantity()));
        price.setTextContent(String.valueOf(getPrice()));
        element.appendChild(title);
        element.appendChild(quantity);
        element.appendChild(price);
        return element;
    }
}
