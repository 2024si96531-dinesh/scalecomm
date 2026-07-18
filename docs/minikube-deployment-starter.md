# Minikube Deployment Starter (Optional Extension)

This is an optional advanced extension for Demo Video 3.

Scope:

- deploy selected stateless components (`api-gateway`, `web-bff`) on Minikube,
- keep remaining dependencies in Docker Compose for local hybrid testing.

This is intentionally a starter path, not a full production Kubernetes migration.

## 1) Prerequisites

- Minikube installed
- Docker Desktop running
- `kubectl` available

Start required dependency services on the host (compose side):

```bash
docker compose up -d mysql rabbitmq redis auth-service patient-service appointment-service health-record-service
```

## 2) Start Minikube

```bash
minikube start --cpus=4 --memory=8192
```

Point Docker CLI to Minikube daemon so images are built inside cluster runtime:

```bash
eval "$(minikube docker-env)"
```

## 3) Build Images

From workspace root:

```bash
docker build -f docker/Dockerfile.spring --build-arg MODULE_PATH=bff/web-bff -t scalecomm/web-bff:demo3 .
docker build -f docker/Dockerfile.spring --build-arg MODULE_PATH=gateway/api-gateway -t scalecomm/api-gateway:demo3 .
```

## 4) Apply Starter Manifests

Apply manifests:

```bash
kubectl apply -f k8s/minikube/demo3-starter.yaml
```

Check workloads:

```bash
kubectl get deploy,svc -n demo3
```

## 5) Access Gateway

Port-forward gateway service:

```bash
kubectl port-forward -n demo3 svc/api-gateway 18080:8080
```

Then use gateway exactly like local compose flow:

- `POST http://localhost:18080/api/v1/auth/login`
- `GET http://localhost:18080/api/v1/dashboard?patientId=p-001`

## 6) Scale in Kubernetes

Scale `web-bff` in cluster:

```bash
kubectl scale deploy/web-bff -n demo3 --replicas=3
kubectl get pods -n demo3 -l app=web-bff
```

## 7) Cleanup

```bash
kubectl delete namespace demo3
minikube stop
```
