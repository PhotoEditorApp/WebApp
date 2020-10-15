# WebApp
This is test version with authorization via json web tokens.
To register in system you should send **POST request to localhost:8080/users/signup**:

{
"username":"user",
"password":"user123"
}

To login you should send **POST to localhost:8080/login** with the same body.

Password encryption is enabled.
