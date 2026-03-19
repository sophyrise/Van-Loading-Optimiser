# VanOpt

Spring Boot backend for van loading optimization.

## a) Build and run

Requirements:
- Java 21
- Docker + Docker Compose

Build:
```bash
./mvnw clean package
```

Run from JAR:
```bash
java -jar target/vanopt-0.0.1-SNAPSHOT.jar
```

Or run directly:
```bash
./mvnw spring-boot:run
```

Windows:
```bash
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

Swagger:
- `http://localhost:8080/swagger-ui.html`

## b) Database setup

Start PostgreSQL:
```bash
docker compose up -d
```

Database config:
- host: `localhost`
- port: `5433`
- db: `vanopt`
- user: `vanopt`
- password: `vanopt`

Flyway runs migration automatically on app startup (`V1__init.sql`).

## c) Example cURL requests and responses

Base path: `/van-optimizations`

### 1. POST `/van-optimizations`

Request:
```bash
curl -X POST http://localhost:8080/van-optimizations \
  -H "Content-Type: application/json" \
  -d '{
    "maxVolume": 15,
    "availableShipments": [
      { "name": "Parcel A", "volume": 5, "revenue": 120 },
      { "name": "Parcel B", "volume": 10, "revenue": 200 },
      { "name": "Parcel C", "volume": 3, "revenue": 80 },
      { "name": "Parcel D", "volume": 8, "revenue": 160 }
    ]
  }'
```

Response:
```json
{
  "requestId": "a1b2c3d4-e5f6-...",
  "selectedShipments": [
    { "name": "Parcel A", "volume": 5, "revenue": 120 },
    { "name": "Parcel B", "volume": 10, "revenue": 200 }
  ],
  "totalVolume": 15,
  "totalRevenue": 320,
  "createdAt": "2025-06-01T10:00:00+04:00"
}
```

### 2. GET `/van-optimizations/{requestId}`

Request:
```bash
curl http://localhost:8080/van-optimizations/<requestId>
```

Response:
```json
{
  "requestId": "<requestId>",
  "selectedShipments": [
    { "name": "Parcel A", "volume": 5, "revenue": 120 },
    { "name": "Parcel B", "volume": 10, "revenue": 200 }
  ],
  "totalVolume": 15,
  "totalRevenue": 320,
  "createdAt": "2025-06-01T10:00:00+04:00"
}
```

### 3. GET `/van-optimizations`

Request:
```bash
curl http://localhost:8080/van-optimizations
```

Response:
```json
[
  {
    "requestId": "a1b2c3d4-e5f6-...",
    "selectedShipments": [
      { "name": "Parcel A", "volume": 5, "revenue": 120 },
      { "name": "Parcel B", "volume": 10, "revenue": 200 }
    ],
    "totalVolume": 15,
    "totalRevenue": 320,
    "createdAt": "2025-06-01T10:00:00+04:00"
  }
]
```

## d) Database schema and index choices

DB file: `src/main/resources/db/migration/V1__init.sql`

Tables:
- `optimisation_record` — one row per optimization request (`id`, `max_volume`, `created_at`)
- `shipment` — one row per shipment (`request_id`, `name`, `volume`, `revenue`, `selected`)

Index:
- `idx_shipment_request_id` on `shipment(request_id)` speeds up loading shipments for a request
