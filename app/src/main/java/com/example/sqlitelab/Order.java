package com.example.sqlitelab;

public class Order {
    private int id;
    private String orderNumber;
    private String clientName;
    private String receptionDate;
    private double cost;
    private String status;

    public Order(int id, String orderNumber, String clientName, String receptionDate, double cost, String status) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.clientName = clientName;
        this.receptionDate = receptionDate;
        this.cost = cost;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getReceptionDate() { return receptionDate; }
    public void setReceptionDate(String receptionDate) { this.receptionDate = receptionDate; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}