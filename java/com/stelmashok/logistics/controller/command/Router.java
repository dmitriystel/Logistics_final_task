package com.stelmashok.logistics.controller.command;

public class Router {

    private String resultPage;
    private RoutingTypeHolder routingTypeHolder;

    public Router() {
    }

    public Router(String resultPage, RoutingTypeHolder routingTypeHolder) {
        this.resultPage = resultPage;
        this.routingTypeHolder = routingTypeHolder;
    }

    public String getResultPage() {
        return resultPage;
    }

    public RoutingTypeHolder getRoutingType() {
        return routingTypeHolder;
    }

    public void setRoutingType(RoutingTypeHolder routingTypeHolder) {
        this.routingTypeHolder = routingTypeHolder;
    }
}
