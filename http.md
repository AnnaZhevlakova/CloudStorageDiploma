curl --location 'http://localhost:8081/cloud/file' \
--header 'Content-Type: application/json' \
--header 'auth-token: asd' \
--form 'filename="Test"' \
--form 'file=@"/C:/Users/admin/Desktop/фото .jpg"'

curl --location 'http://localhost:8081/cloud/file?filename=Test' \
--header 'auth-token: asd'

curl --location --request PUT 'http://localhost:8081/cloud/file?filename=Test' \
--header 'auth-token: asd' \
--header 'Content-Type: application/json' \
--data '{
"name" : "newTestNAME"
}'

curl --location --request GET 'http://localhost:8081/cloud/list?limit=1' \
--header 'auth-token: asd' \
--header 'Content-Type: application/json' \
--data '{
"name" : "newTestNAME"
}'

curl --location --request DELETE 'http://localhost:8081/cloud/file' \
--header 'auth-token: asd' \
--form 'filename="Test"'

curl --location 'http://localhost:8081/cloud/login' \
--header 'auth-token: asd' \
--header 'Content-Type: application/json' \
--data '{
"login": "test",
"password": "test"
}'

curl --location --request POST 'http://localhost:8081/cloud/logout' \
--header 'auth-token: asd'