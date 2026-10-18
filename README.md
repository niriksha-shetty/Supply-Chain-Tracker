# Multi-Tier Supply Chain Disruption & Resilience Tracker (SC-RESILIENCE)

A cloud-native, distributed microservice architecture designed to provide end-to-end visibility across multi-tier supplier networks, isolate business domains, eliminate single points of failure, and track cascading disruption impacts in real time.

---

## 1. Architecture & Tech Stack

- **Java**: 17
- **Spring Boot**: 3.3.4
- **Spring Cloud**: 2023.0.3
- **Service Registry & Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway (Reactive)
- **Inter-Service Communication**: Spring Cloud OpenFeign (with Apache HttpClient 5)
- **Fault Tolerance & Resilience**: Resilience4j Circuit Breakers & Fallback Factories
- **Distributed Tracing**: Micrometer Tracing (Brave bridge) + Zipkin Reporter
- **Persistence**: Spring Data JPA + PostgreSQL (Database-per-Service)
- **API Documentation**: SpringDoc OpenAPI / Swagger UI

---

## 2. Microservice Topology

| Microservice | Port | Database | Primary Responsibilities |
| :--- | :--- | :--- | :--- |
| **`eureka-server`** | `8761` | *N/A* | Centralized service registry for discovery, dynamic routing, and load balancing. |
| **`api-gateway`** | `8080` | *N/A* | Edge reverse proxy, unified ingress routing (`lb://`), request logging, and trace propagation. |
| **`supplier-service`** | `8081` | `supplier_db` | Manages master vendor passports, tier rankings (Tier 1–5), ESG compliance scores, and production facilities. |
| **`order-service`** | `8082` | `order_db` | Manages purchase order placement, delivery schedules, and line-item allocations. Validates vendors via OpenFeign. |
| **`disruption-service`** | `8083` | `disruption_db` | Ingests regional disruption signals (port congestion, strikes, weather alerts) and cascades alerts to impacted facilities. |

---

## 3. Resilience, Fault Tolerance & Distributed Tracing (Phase 4)

### 3.1 Resilience4j Circuit Breakers & Fallbacks
- **OpenFeign Circuit Breaker Integration**: Enabled via `spring.cloud.openfeign.circuitbreaker.enabled=true`.
- **Fallback Factories**:
  - `SupplierClientFallbackFactory` (`order-service`): Returns a fallback `SupplierDTO` with status `TEMPORARILY_UNAVAILABLE` when `SUPPLIER-SERVICE` is offline or failing.
  - `OrderClientFallbackFactory` (`disruption-service`): Catches failures gracefully when cascading facility alerts to `ORDER-SERVICE`.
- **Circuit Breaker Configuration**:
  ```properties
  resilience4j.circuitbreaker.configs.default.sliding-window-size=10
  resilience4j.circuitbreaker.configs.default.minimum-number-of-calls=5
  resilience4j.circuitbreaker.configs.default.failure-rate-threshold=50
  resilience4j.circuitbreaker.configs.default.wait-duration-in-open-state=10s
  resilience4j.circuitbreaker.configs.default.permitted-number-of-calls-in-half-open-state=3
  ```

### 3.2 Distributed Tracing with Micrometer & Zipkin
- Traces requests across `api-gateway` -> `order-service` / `disruption-service` -> `supplier-service`.
- **Trace Sampling Rate**: 100% (`management.tracing.sampling.probability=1.0`)
- **Zipkin Endpoint**: `http://localhost:9411/api/v2/spans`
- **Correlated Log Pattern**: `%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]`

---

## 4. API Endpoints Reference

All endpoints can be accessed directly or routed through the API Gateway on port `8080`.

### 4.1 Supplier Service (`/api/suppliers` via Gateway: `http://localhost:8080/api/suppliers`)

* **Create Supplier & Facilities**: `POST /api/suppliers`
  ```json
  {
    "legalName": "Apex Semiconductor",
    "tierLevel": 2,
    "esgComplianceScore": 95.50,
    "status": "ACTIVE",
    "facilities": [
      {
        "geoLatitude": 19.0760,
        "geoLongitude": 72.8777,
        "regionCode": "IN-MH",
        "capacityUnitsDay": 5000
      }
    ]
  }
  ```
* **Get All Suppliers**: `GET /api/suppliers`
* **Get Supplier by ID**: `GET /api/suppliers/{id}`
* **Delete Supplier**: `DELETE /api/suppliers/{id}`

### 4.2 Order Service (`/api/orders` via Gateway: `http://localhost:8080/api/orders`)

* **Create Purchase Order (with Inter-Service Validation & Circuit Breaker)**: `POST /api/orders`
  ```json
  {
    "supplierId": "<VALID_SUPPLIER_UUID>",
    "destFacilityId": "<VALID_FACILITY_UUID>",
    "expectedDelivery": "2026-09-15T10:00:00",
    "lineItems": [
      {
        "quantity": 500,
        "unitPrice": 12.75
      }
    ]
  }
  ```
  - **Success (`201 Created`)**: Validates supplier against `supplier-service` and records order as `CONFIRMED`.
  - **Circuit Breaker Fallback**: If `supplier-service` is down, `SupplierClientFallbackFactory` prevents a 500 crash and allows graceful degradation.
* **Get All Orders**: `GET /api/orders`
* **Get Order by ID**: `GET /api/orders/{id}`
* **Update Order Status by Facility**: `PATCH /api/orders/flag-facility/{facilityId}/{status}`

### 4.3 Disruption Ingestion Service (`/api/disruptions` via Gateway: `http://localhost:8080/api/disruptions`)

* **Ingest Disruption & Trigger Downstream Propagation**: `POST /api/disruptions`
  ```json
  {
    "eventType": "PORT_CONTAINER_CONGESTION",
    "severityIndex": 8,
    "sourceRegionCode": "IN-MH",
    "impacts": [
      {
        "facilityId": "<TARGET_FACILITY_UUID>",
        "estDelayDays": 14,
        "status": "OPERATIONS_HALTED"
      }
    ]
  }
  ```
  - **Behavior**: Records the disruption in `disruption_db` and flags matching orders in `order-service` via OpenFeign. If `order-service` is unreachable, `OrderClientFallbackFactory` logs and recovers gracefully.
* **Get All Disruptions**: `GET /api/disruptions`
* **Get Disruption by ID**: `GET /api/disruptions/{id}`

---

## 5. Local Setup & Execution Guide

### Prerequisites
* Java JDK 17+
* PostgreSQL 15+
* Docker (optional, for Zipkin)

### Database Initialization
Create the three isolated databases in PostgreSQL:
```sql
CREATE DATABASE supplier_db;
CREATE DATABASE order_db;
CREATE DATABASE disruption_db;
```

### Start Zipkin (Distributed Tracing UI)
```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

### Startup Sequence
Run each service in a dedicated terminal window using the Maven wrapper:

1. **Eureka Server**:
   ```powershell
   cd eureka-server; .\mvnw spring-boot:run
   ```
   *Dashboard available at:* `http://localhost:8761`

2. **API Gateway**:
   ```powershell
   cd api-gateway; .\mvnw spring-boot:run
   ```
   *Gateway URL:* `http://localhost:8080`

3. **Core Microservices**:
   ```powershell
   cd supplier-service; .\mvnw spring-boot:run
   cd order-service; .\mvnw spring-boot:run
   cd disruption-service; .\mvnw spring-boot:run
   ```

---

## 6. Verification & Testing

### 6.1 Testing Circuit Breaker & Fallback
1. Stop `supplier-service` (`Ctrl+C`).
2. Submit a purchase order request to `http://localhost:8080/api/orders`.
3. Check `order-service` console logs to observe the fallback trigger:
   ```
   WARN [...] com.orderservice.client.SupplierClientFallbackFactory : Fallback triggered for SupplierClient.getSupplierById(...)
   ```

### 6.2 Testing Distributed Tracing
1. Issue a request through the API Gateway:
   ```bash
   curl http://localhost:8080/api/suppliers
   ```
2. Check terminal logs for `api-gateway` and `supplier-service` to observe identical `traceId` correlation across services:
   ```
   INFO [api-gateway,4bf92f3577b...,4bf92f3577b...]
   INFO [supplier-service,4bf92f3577b...,71e5c4a30e...]
   ```
3. Open the Zipkin UI at `http://localhost:9411` and click **Run Query** to view complete latency trace waterfalls.