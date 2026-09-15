# ✅ How Grafana Picks Configs
Since you mounted ./grafana/provisioning:/etc/grafana/provisioning in your compose file:

Datasources (datasources.yaml)

Dashboards (unified-observability.json, sse-webflux-performance.json)

Alerting rules (alerting-rules.yaml)

Contact points (contact-points.yaml)

…are all auto‑loaded when Grafana starts. You don’t need to import them manually.

-------------------------------------------------------

✅ Verifying Grafana Provisioning Inside the UI
1. Once Grafana is up (http://localhost:3000):

	Login → default user/password: admin / admin (or whatever you set in docker-compose.yml).

2. Check Datasources:

	Go to Configuration → Data Sources.
	
	You should see Prometheus, Loki, Tempo already listed.

3. Check Dashboards:

	Go to Dashboards → Manage.
	
	You should see Unified Observability and SSE/WebFlux Performance dashboards auto‑loaded.

4. Check Alerts:

	Go to Alerting → Alert rules.
	
	You should see your rules (High SSE Latency, Low Active Connections).

5. Check Contact Points:

	Go to Alerting → Contact points.
	
	You should see email-alerts and slack-alerts.

👉 Once you confirm those are visible, you’ll know Grafana successfully picked up your provisioning configs.

----------------------------------