# cool-uris
A simple web api that dereferences real world objects in a sparql endpoint following the [cool uri's strategy](http://www.w3.org/TR/cooluris/).
The api takes a query parameter "uri" and will dereference to the data view or the html view based on accept headers, based on data available in the endpoint.

This is a port of [cool-dereferenceable-uris](https://github.com/nvdk/cool-dereferenceable-uris) to scala. It has proven its quality in a production setup.


## Usage with Docker 
```
docker run -p 80:8080 --name cool-uri -e SPARQL_ENDPOINT="https://stad.gent/sparql" nvdk/cool-uris:0.1.0
```

```
$curl -i http://localhost/?uri=https://stad.gent/id/agents/083b7b32-df2e-4da4-bcc5-b7fe75ea8426
```

## Example redirection

1. Step 1 set up a reverse proxy that redirects `https://qa.stad.gent/id/` to cool-uris. 

The following is an example using nginx and uses the received host and scheme to construct the full uri. 172.24.0.2 is the ip cool-uris is running on.
```
server {
   listen      80;
   location /id {
     proxy_pass http://172.24.0.2:8080/?uri=$scheme://$host$request_uri;
   }
}
```

2. Add the necessary information in your SPARQL endpoint

Example triples:

```
<https://qa.stad.gent/id/visualart/streetart/05c7b793-b387-4b0f-b3da-63c4d3900e76> rdfs:isDefinedBy <https://qa.stad.gent/data/visualart/streetart/05c7b793-b387-4b0f-b3da-63c4d3900e76>
<https://qa.stad.gent/id/visualart/streetart/05c7b793-b387-4b0f-b3da-63c4d3900e76> foaf:page <https://cultuur.qa.stad.gent/node/341>
```

3. Test it 
```
$curl -H "Accept: text/turtle" -i https://qa.stad.gent/id/visualart/streetart/05c7b793-b387-4b0f-b3da-63c4d3900e76 
```
should redirect to https://qa.stad.gent/data/visualart/streetart/05c7b793-b387-4b0f-b3da-63c4d3900e76

```
$curl -H "Accept: text/html" -i https://qa.stad.gent/id/visualart/streetart/05c7b793-b387-4b0f-b3da-63c4d3900e76 
```

should redirect to https://cultuur.qa.stad.gent/node/341


## Build & Run 

```sh
$ cd cool_uris
$ ./sbt
> jetty:start
> browse
```


## License
See [LICENSE](LICENSE)
