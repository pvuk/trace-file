# Trace File Management System

**Author:** Pulipati Venkata UdayKiran  
**Origin:** pvuk (UdayKiran Pulipati) · GitHub  
**Generated with:** Copilot4Eclipse  

---

## 📑 Table of Contents
- [Description](#-description)
- [Features](#-features)
- [Technologies Used](#-technologies-used)
- [How to Use](#-how-to-use)
- [API Endpoints](#-api-endpoints)
- [Observability Stack](#-observability-stack)
- [Author](#-author)

---

## 📖 Description
The Trace File Management System is a **Spring Boot-based application** designed to handle file uploads, metadata management, and notification tracking.  
It provides RESTful APIs for uploading files with associated metadata, managing notifications, and querying archived data.  
The project leverages **reactive programming with Spring WebFlux** for asynchronous operations and integrates with a relational database (Oracle DB) for persistence.  

Additionally, the system is instrumented with **OpenTelemetry** to provide distributed tracing, metrics, and logs. These are visualized and correlated using the **Grafana observability stack**.

The system is instrumented with **Spring Boot Actuator + Micrometer** for metrics, **Loki** for logs, and can be extended with **OpenTelemetry + Tempo** for distributed tracing. Dashboards are unified in **Grafana**.

---

## 🚀 Features
- **File Upload with Metadata**: Supports multipart file uploads along with JSON metadata.
- **Notification Management**: Tracks notifications with streaming updates using Server-Sent Events (SSE).
- **Archived Data Search**: Enables querying archived notifications based on date ranges.
- **Database Integration**: Uses JDBC for database operations and supports partitioning for efficient data management.
- **Reactive Programming**: Implements non-blocking, asynchronous operations using Reactor.
- **Observability**:
  - **Metrics** via Actuator → Prometheus (collected via OpenTelemetry and stored in Prometheus) → Grafana.
  - **Logs** via Alloy → Loki (aggregated in Loki for fast, cost-efficient querying) → Grafana.
  - **Traces** via OpenTelemetry → Tempo (stored in Grafana Tempo) → Grafana.
  - **Unified Dashboards** in Grafana to correlate metrics, logs, and traces.

---

## 🛠 Technologies Used
- **Backend**
  - Java 21 (core programming language)
  - Spring Boot (application framework)
  - Spring WebFlux (reactive programming)
  - Spring Data JPA
  - Maven (dependency management and build tool)
  - SQL (Oracle JDBC for database operations)

- **Frontend**
  - JavaScript (optional frontend scripting)

- **Observability**
  - OpenTelemetry (instrumentation for metrics, logs, traces)
  - Micrometer + Prometheus (metrics storage)  
  - Grafana (visualization and dashboards)
  - Loki (log aggregation)
  - Tempo (distributed tracing)
  - Alloy (unified telemetry pipeline agent)

# 🧩 How They Work Together
	Metrics → Spring Boot Actuator → scraped by Prometheus → visualized in Grafana
	Logs → collected by Alloy → stored in Loki → queried in Grafana
	Traces → OpenTelemetry + Tempo
	
---

## 📦 How to Use
1. Clone the repository:
   ```bash
   git clone https://github.com/pvuk/trace-file.git
2. Configure DB in `application.yml`  
3. Run app: `mvn spring-boot:run`  
4. Prometheus scrapes metrics from `/actuator/prometheus`  
5. Logs flow into Loki via Alloy  
6. Traces flow into Tempo via OpenTelemetry exporter  
7. View everything in Grafana dashboards
