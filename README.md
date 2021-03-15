# AkkaHTTP JWT Security [![Codeship Status for techmonad/akka-http-jwt-security](https://app.codeship.com/projects/9ee6b8ff-4dcc-4f9f-94d8-154228d380be/status?branch=master)](https://app.codeship.com/projects/434411)

A basic example to implement a role based authentication and authorization with Akka HTTP using JWT Token.

#### Run application:
 ```
$ sbt run

[info] Running com.techmonad.QuickStartServer 
Server online at http://127.0.0.1:8080/

```

```

1) Get JWT token:

 $ curl -XPOST -H 'Content-Type:application/json'  'localhost:8080/auth' -d '{"name":"Andy"}'
   {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTExNDY0MjksImlhdCI6MTYxMTA2MDAyOSwKICAiYWdlIjogMzEsCiAgImNvdW50cnkiOiAiRnJhbmNlIiwKICAibmFtZSI6ICJBbmR5IiwKICAicm9sZXMiOiBbImFkbWluIiwgInVzZXIiXQp9._7nzblnmMKABOK8KnkB9yJwjzH3PZhNZWUsCIrhdd78"}

2) Get User list:

 $ curl  -H 'Content-Type:application/json'  -H 'Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTExNDYwMjIsImlhdCI6MTYxMTA1OTYyMiwKICAiYWdlIjogMzEsCiAgImNvdW50cnkiOiAiRnJhbmNlIiwKICAibmFtZSI6ICJBbmR5IiwKICAicm9sZXMiOiBbImFkbWluIiwgInVzZXIiXQp9.YVytWBCqFupvH2UEbPafFmGCmCq6l_6UNLx9VealSig' 'localhost:8080/users'
    {"users":[{"age":31,"country":"France","name":"Andy","roles":["admin","user"]}]}


3) Create new User:
 $ curl -XPOST -H 'Content-Type:application/json'  -H 'Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTExNDYwMjIsImlhdCI6MTYxMTA1OTYyMiwKICAiYWdlIjogMzEsCiAgImNvdW50cnkiOiAiRnJhbmNlIiwKICAibmFtZSI6ICJBbmR5IiwKICAicm9sZXMiOiBbImFkbWluIiwgInVzZXIiXQp9.YVytWBCqFupvH2UEbPafFmGCmCq6l_6UNLx9VealSig' 'localhost:8080/users' \
 -d '{ "name": "bob", "age": 21,  "country": "CSA", "roles": ["user"]}'
      User bob created.
```



# License
This code is open source software licensed under the MIT license.
