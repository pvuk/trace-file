package com.trace.file.notification.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trace.file.entity.Notification;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * 🔄 How This Works in Your Case:</br>
	Maker uploads → sink.tryEmitNext(notification) pushes event.
	
	Checker subscribes → gets live events plus last 50 missed events (replay buffer).
	
	If Checker was offline for multiple days, you still persist everything in DB, and on reconnect:
	
	Replay last N from sink (fast).
	
	Fetch older ones from DB (complete history).</br>

 * Options:</br>
	Sinks.many().multicast() → like EmitterProcessor (hot stream, no replay).
	
	Sinks.many().replay().limit(n) → like ReplayProcessor (replays last n events).
	
	Sinks.many().unicast() → single subscriber only.</br>

 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Wednesday 26-August-2026 21:06:13
 */
@Component
public class NotificationPublisher {
	private final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);
	/*
	 * 🚨 Why Deprecated?
		EmitterProcessor and ReplayProcessor were part of Reactor’s older “Processor” family.
		
		They had subtle issues with backpressure and lifecycle management.
		
		Reactor now recommends using Sinks instead.
	 */
    // Replay last 50 notifications
//    private final ReplayProcessor<Notification> processor = ReplayProcessor.create(50);//deprecated
//    private final FluxSink<Notification> sink = processor.sink();
    private final Sinks.Many<Notification> sink = Sinks.many().replay().all();

    public void publish(Notification notification) {
        log.info("Publishing notification: {}", notification);
//        sink.next(notif); // push into replay buffer
        sink.tryEmitNext(notification);//→ Confirms Maker is pushing.
    }

    public Flux<Notification> getStream() {
//        return processor; // hot stream + replay
        return sink.asFlux();
    }
}
