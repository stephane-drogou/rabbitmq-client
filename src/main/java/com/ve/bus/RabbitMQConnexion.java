package com.ve.bus;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.rabbitmq.IncomingRabbitMQMetadata;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.inject.Singleton;

@Singleton
public class RabbitMQConnexion {
    
    private List<String> msgsToSend = new ArrayList<>();

    public void envoyerMessage(String msg) {
        msgsToSend.add(msg);
        Log.info("envoyerMessage : on envoie le message : " + msg);
    }

    @Outgoing("service-test-out")
    public Message<String> send() throws InterruptedException {
        
        while (msgsToSend.isEmpty())
        {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                Log.error("Probleme sur le sleep : " + e.getMessage());
                throw e;
            }
        }

        String msgToSend = msgsToSend.remove(0);

        Log.info("send : on envoie le message : " + msgToSend);
        return Message.of(msgToSend,Metadata.of(new OutgoingRabbitMQMetadata.Builder()
                                                .withRoutingKey("key-test")
                                                .withTimestamp(ZonedDateTime.now())
                                                .build()));
    }

    @Incoming("service-test-in")
    public CompletionStage<Void> consume(Message<byte[]> msg) {
        final Optional<IncomingRabbitMQMetadata> metadata = msg.getMetadata(IncomingRabbitMQMetadata.class);
        
        Log.info("on a reçu un message");
        metadata.ifPresent(meta -> {
            final String routingKey = meta.getRoutingKey();
            if (routingKey != null) {

                switch(routingKey) {
                    case "test":
                        String payload = new String(msg.getPayload(), StandardCharsets.UTF_8);
                        if (payload != null)
                            Log.info("On a reçu un message test : " + payload);
                        break;
                    default:
                        Log.error("On a reçu un message inconnu");
                }
            }
        });
        return msg.ack();
        
    }
}
