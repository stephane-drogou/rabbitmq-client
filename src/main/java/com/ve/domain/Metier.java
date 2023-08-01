package com.ve.domain;

//import com.ve.bus.RabbitMQConnexion;

import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;

@ApplicationScoped
public class Metier {
    //@Inject
    //private RabbitMQConnexion bus;

    public void sendMessage(String msg) {
        //bus.envoyerMessage(msg);
    }
}
