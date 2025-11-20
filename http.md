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