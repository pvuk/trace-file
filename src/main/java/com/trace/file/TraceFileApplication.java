package com.trace.file;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * ⚡ Spring MVC vs WebFlux
Spring MVC (starter-web) → blocking, thread-per-request. Best for CRUD apps, admin dashboards, synchronous APIs.

Spring WebFlux (starter-webflux) → non-blocking, reactive (Flux, Mono). Best for high-concurrency, streaming, SSE, WebSockets.</br>
-----------------------------------------------
🔄 What SSE Means
SSE = Server-Sent Events.  
It’s a web technology where the server pushes data to the client over a single long-lived HTTP connection. Unlike normal REST calls (client → server → response → close), SSE keeps the connection open and streams updates continuously.

Protocol: Built on plain HTTP, using the text/event-stream MIME type.

Direction: One-way (server → client). The client can’t send messages back over the same channel — that’s what WebSockets are for.

Use case: Perfect for notifications, dashboards, live feeds, or anything where the server needs to keep the browser updated in real time.</br>
--------------------
🔄 SSE vs WebSockets — Direction of Communication</br>
SSE (Server-Sent Events):</br>

One-way channel: The server can continuously send data to the client (browser).

The client cannot send messages back over the same SSE connection.

Example: Your Angular app opens an EventSource to /notifications. The server streams updates like:

Code
data: {"fileId":52,"assignedBy":"AdminMaker","read":false}
The browser receives it, but if you want to tell the server “mark this as read,” you must make a separate HTTP POST/PUT request.

WebSockets:</br>

Two-way channel: Both client and server can send messages to each other over the same connection.

Example: Your Angular app connects via new WebSocket("ws://..."). You can send "markRead:52" directly over that socket, and the server can reply immediately.

📊 Analogy</br>
Think of SSE like a radio broadcast:

. The station (server) keeps sending songs (events).

. The listener (client) can only listen.

. If the listener wants to request a song, they must call the station separately (HTTP request).

Whereas WebSockets are like a telephone call:

. Both sides can talk back and forth on the same line.
---------------------------

 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Sunday 23-August-2026 16:10:34
 */
@SpringBootApplication
@EnableScheduling
public class TraceFileApplication {

	public static void main(String[] args) {
//		SpringApplication.run(TraceFileApplication.class, args);
		//OR
		new SpringApplicationBuilder(TraceFileApplication.class)
		.properties("spring.webflux.base-path=/trace-file")
		.run(args);
	}

}
