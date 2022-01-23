After running the app you can check all the available endpoints:  
[Swagger](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/)

In order to be able to send requests you must first obtain the JWT token from /users/login endpoint.  
After successful login, token is returned as a response header "token".
Please put the above token in you all requests in header "Authorization".

You can create your own user with /users/register endpoint or use the existing one:
admin/admin
