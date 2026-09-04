# Multi-Tier Supply Chain Disruption & Resilience Tracker (SC-RESILIENCE)
### Phase 2: Microservice Implementation & Inter-Service Communication

## 1. Project Overview
Modern supply chain ecosystems lack structural visibility into multi-tier dependencies[cite: 1]. The Multi-Tier Supply Chain Disruption & Resilience Tracker is a cloud-native, distributed microservice architecture designed to isolate business domains, eliminate single points of failure, and track cascading disruption impacts in real time[cite: 1].

Phase 2 focuses on core service implementation using **Spring Boot 3**, **Spring Data JPA**, **PostgreSQL** relational persistence, and synchronous inter-service communication using **Spring Cloud OpenFeign**[cite: 1].

---

## 2. Architecture & Design Principles

* **Database-per-Service Pattern**: Each microservice manages its own isolated PostgreSQL database schema to guarantee loose coupling and autonomous scaling[cite: 1]. Cross-database joins and physical foreign keys are strictly prohibited[cite: 1].
* **Decoupled Data Identifiers**: Entities reference records across service boundaries via immutable `UUIDv4` identifiers[cite: 1].
* **Synchronous REST Communication**: Services communicate over HTTP via declarative OpenFeign clients for live validation and state cascade propagation[cite: 1].

---

## 3. Microservice Specifications

| Microservice | Port | Database | Primary Responsibilities |
| :--- | :--- | :--- | :--- |
| **`supplier-service`**[cite: 1] | `8081` | `supplier_db`[cite: 1] | Manages master vendor passports, tier rankings (Tier 1–5), ESG compliance ratings, and localized production facilities[cite: 1]. |
| **`order-service`**[cite: 1] | `8082` | `order_db`[cite: 1] | Manages purchase order placement, delivery schedules, and line-item allocations[cite: 1]. Validates vendor IDs across services via OpenFeign[cite: 1]. |
| **`disruption-service`**[cite: 1] | `8083` | `disruption_db`[cite: 1] | Ingests regional disruption signals (port congestion, strikes, weather alerts) and cascades alerts to impacted facilities[cite: 1]. |

---

## 4. API Endpoints Reference

### 4.1 Supplier Service (`http://localhost:8081`)

* **Create Supplier & Facilities**
  * `POST /api/suppliers`
  * **Payload:**
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
  * **Response:** `201 Created` with generated `supplierId` and `facilityId`.
* **Get All Suppliers:** `GET /api/suppliers`
* **Get Supplier by ID:** `GET /api/suppliers/{id}`
* **Delete Supplier:** `DELETE /api/suppliers/{id}`

---

### 4.2 Order Service (`http://localhost:8082`)

* **Create Purchase Order (with Inter-Service Validation)**
  * `POST /api/orders`
  * **Payload:**
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
  * **Behavior:**
    * **Success (`201 Created`)**: If the `supplierId` exists in `supplier-service`, the order is recorded with initial status `CONFIRMED`.
    * **Rejection (`400 Bad Request`)**: If the `supplierId` does not exist in `supplier-service`, the transaction is rejected via OpenFeign validation.
* **Get All Orders:** `GET /api/orders`
* **Get Order by ID:** `GET /api/orders/{id}`
* **Update Order Status by Facility:** `PATCH /api/orders/flag-facility/{facilityId}/{status}`

---

### 4.3 Disruption Ingestion Service (`http://localhost:8083`)

* **Ingest Disruption & Trigger Downstream Propagation**
  * `POST /api/disruptions`
  * **Payload:**
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
  * **Behavior:** Logs the event in `disruption_db` and calls `order-service` via OpenFeign to transition all active orders for that facility to `DELAYED_AT_RISK`.
* **Get All Disruptions:** `GET /api/disruptions`
* **Get Disruption by ID:** `GET /api/disruptions/{id}`

---

## 5. Local Setup & Execution Guide

### Prerequisites
* Java JDK 17 or higher
* Apache Maven
* PostgreSQL 15+

### Database Initialization
Run the following commands in PostgreSQL (`psql`) before running the applications:
```sql
CREATE DATABASE supplier_db;
CREATE DATABASE order_db;
CREATE DATABASE disruption_db;