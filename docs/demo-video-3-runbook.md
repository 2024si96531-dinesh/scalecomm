# Demo Video 3 Runbook: Scalability and Fault Recovery

This runbook is for recording Demo Video 3 and proving:

- service scalability,
- fault isolation and recovery behavior,
- optional advanced extension support (Minikube starter path).

## 1) Start Demo Profile

Start stack with the Demo 3 override:

```bash
docker compose -f compose.yaml -f compose.demo3.yaml up -d --build
```

Why this profile exists:

- `web-bff` host port binding is removed so the service can scale to multiple replicas.
- traffic still reaches `web-bff` through `api-gateway`.

Get a token:

```bash
TOKEN=$(curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin1","password":"secret123"}' \
  | sed -n 's/.*"accessToken"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')

echo "Token length: ${#TOKEN}"
```

## 2) Scalability Segment (Horizontal Replicas)

Scale `web-bff` to three replicas:

```bash
docker compose -f compose.yaml -f compose.demo3.yaml up -d --scale web-bff=3
```

Verify replica count:

```bash
docker compose -f compose.yaml -f compose.demo3.yaml ps web-bff
```

Generate repeated dashboard calls and show which replica served each request:

```bash
bash scripts/demo3-dashboard-burst.sh "$TOKEN" 20
```

Expected talking points:

- dashboard responses include `servedBy` (container hostname),
- requests are served by multiple `web-bff` replicas,
- this demonstrates horizontal scalability behind the gateway.

## 3) Fault Isolation Segment

Stop only `health-record-service`:

```bash
docker compose stop health-record-service
```

Call dashboard route through gateway:

```bash
curl -sS "http://localhost:8080/api/v1/dashboard?patientId=p-001" \
  -H "Authorization: Bearer $TOKEN"
```

Expected result:

- `patient` and `appointments` sections remain available,
- `records` section returns an error object,
- request still succeeds as aggregated partial response.

This proves the failure is isolated to one downstream dependency and does not crash the full request path.

Also show other independent flows remain healthy:

```bash
curl -sS "http://localhost:8080/api/v1/operations-summary?patientId=p-001" \
  -H "Authorization: Bearer $TOKEN"
```

## 4) Recovery Segment

Restart the failed service:

```bash
docker compose start health-record-service
```

Wait until healthy logs appear:

```bash
docker compose logs health-record-service --tail=80
```

Call dashboard again:

```bash
curl -sS "http://localhost:8080/api/v1/dashboard?patientId=p-001" \
  -H "Authorization: Bearer $TOKEN"
```

Expected result:

- `records` returns normal data again,
- no gateway restart and no BFF restart required.

## 5) Optional Advanced Extension (Minikube)

If you want to record an advanced extension segment, use:

- `docs/minikube-deployment-starter.md`

This provides a starter Kubernetes deployment path for selected components.

## 6) Suggested Recording Timeline

1. Intro (20-30s): architecture + demo goals.
2. Scale up (60-90s): scale `web-bff`, run burst script, show changing `servedBy`.
3. Inject failure (60-90s): stop `health-record-service`, show partial dashboard response.
4. Recovery (45-60s): start service, show dashboard fully restored.
5. Optional Minikube extension (45-60s): show manifests and `kubectl` apply/get commands.

## 7) Teardown

```bash
docker compose -f compose.yaml -f compose.demo3.yaml down
```

For full cleanup:

```bash
docker compose -f compose.yaml -f compose.demo3.yaml down -v
```
