# Dropwizard Missing Validation Example

Dropwizard example application showing how validation can be (accidentally) disabled by a rogue dependency.
First, run the application as-is and do a `POST` to the `/person` resource with JSON that will pass validation
in the `Person` class. For example, use [httpie](http://www.slideshare.net/scottleber/htt-pie-minitalk) to submit a
valid `POST` request:

```JSON
$ http -v http://localhost:8080/person firstName=Bob lastName=Sackamano
POST /person HTTP/1.1
Accept: application/json
Accept-Encoding: gzip, deflate, compress
Content-Length: 45
Content-Type: application/json; charset=utf-8
Host: localhost:8080
User-Agent: HTTPie/0.8.0

{
    "firstName": "Bob", 
    "lastName": "Sackamano"
}

HTTP/1.1 201 Created
Content-Length: 49
Content-Type: application/json
Date: Mon, 03 Aug 2015 02:36:39 GMT
Location: http://localhost:8080/person/2

{
    "firstName": "Bob", 
    "id": 2, 
    "lastName": "Sackamano"
}
```

Now, submit an _invalid_ request, we get a `422 Unprocessable Entity` response:

```JSON
$ http -v http://localhost:8080/person firstName=Bob
POST /person HTTP/1.1
Accept: application/json
Accept-Encoding: gzip, deflate, compress
Content-Length: 20
Content-Type: application/json; charset=utf-8
Host: localhost:8080
User-Agent: HTTPie/0.8.0

{
    "firstName": "Bob"
}

HTTP/1.1 422 
Content-Length: 51
Content-Type: application/json
Date: Mon, 03 Aug 2015 02:39:24 GMT

{
    "errors": [
        "lastName may not be empty (was null)"
    ]
}
```

The above is how things normally work. To demonstrate Dropwizard validation not working, edit `pom.xml` and uncomment
the `jersey-media-json-jackson` dependency. Now re-run the above command to post an invalid person entity, and instead
of a `422` you will get a `201 Created`, because validation has been "disabled" due to the automatic registration of
the `JacksonFeature`. See code comments in the POM file and `PersonResource` for more details. Here's the output,
showing that validation did not actually occur:

```JSON
$ http -v http://localhost:8080/person firstName=Bob
Accept: application/json
Accept-Encoding: gzip, deflate, compress
Content-Length: 20
Content-Type: application/json; charset=utf-8
Host: localhost:8080
User-Agent: HTTPie/0.8.0

{
    "firstName": "Bob"
}

HTTP/1.1 201 Created
Content-Length: 42
Content-Type: application/json
Date: Mon, 03 Aug 2015 02:44:21 GMT
Location: http://localhost:8080/person/1

{
    "firstName": "Bob", 
    "id": 1, 
    "lastName": null
}
```
 
Oops.
