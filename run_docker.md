🔧 Script to Run Docker with Your Configurations
From your project root (where docker-compose.yml is located), run:

bash
# Ensure you're in the project root
cd path/to/trace-file

# Build and start all services in the background
docker-compose up -d

-----------------------------------------------------
# ✅ What Happens
* Prometheus starts and scrapes metrics from your Spring Boot app (/actuator/prometheus).

* Loki starts and ingests logs.

* Tempo starts and ingests traces.

* Grafana starts, mounts grafana/provisioning/ into /etc/grafana/provisioning, and auto‑loads:

	* Datasources (Prometheus, Loki, Tempo)</br>
	* Dashboards (unified-observability.json, sse-webflux-performance.json)</br>
	* Alerting rules (alerting-rules.yaml)</br>
	* Contact points (contact-points.yaml)</br>

----------------------------------------------------------

# 🔍 Useful Commands
Check logs of a service:

bash
>docker-compose logs grafana

Restart Grafana only:

bash
>docker-compose restart grafana

Stop all services:

bash
>docker-compose down

--------------------------------------
# Stop docker
docker-compose down 

# Rremove old containers
docker rm -f prometheus grafana loki tempo alloy 

# Start fresh
docker-compose up -d

docker-compose down -v          # clear out the bad auto-created "tempo.yaml" directory
docker-compose up -d
docker ps                       # all 5 containers should show "Up", not "Restarting"
docker logs tempo_tracefile     # should show Tempo actually starting, no config errors

------------------------