# jwt-apigateway-security

## Regist an user

```
curl -X POST 'http://localhost:8080/auth/register' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=7CE91EE75A65277C0DCB6C5736C5DF5D' \
--data-raw '{
    "name":"Basant",
    "password":"Pwd1",
    "email":"basant@gmail.com"
}'

```

## Generate token

```
curl -X POST 'http://localhost:8080/auth/token' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=7CE91EE75A65277C0DCB6C5736C5DF5D' \
--data-raw '{
    "username":"Basant",
    "password":"Pwd1"
}'
```
## Access Swiggy-app

```
curl -X GET 'http://localhost:8080/swiggy/37jbd832' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYXNhbnQiLCJpYXQiOjE3NzMyODc0MTMsImV4cCI6MTc3MzI4OTIxM30.EVdf_vIk033-iRxk0XbGFlStii8mRUETcq-4M07v1sw[' \
--header 'Cookie: JSESSIONID=7CE91EE75A65277C0DCB6C5736C5DF5D'
```

## Access Restaurant-service

```
curl -X GET 'http://localhost:8080/restaurant/orders/status/37jbd832' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYXNhbnQiLCJpYXQiOjE3NzMyODc0MTMsImV4cCI6MTc3MzI4OTIxM30.EVdf_vIk033-iRxk0XbGFlStii8mRUETcq-4M07v1sw[' \
--header 'Cookie: JSESSIONID=7CE91EE75A65277C0DCB6C5736C5DF5D'
```
