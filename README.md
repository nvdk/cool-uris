# cool-uris #

A quick and dirty port of [cool-dereferenceable-uris](https://github.com/nvdk/cool-dereferenceable-uris) to scala. Don't judge first scala project using scalatra :).


## Usage with Docker ##
```
docker run -p 80:8080 --name cool-uri -e SPARQL_ENDPOINT="https://stad.gent/sparql" nvdk/cool-uris:0.1.0
```

```
$curl -i http://localhost/?uri=https://stad.gent/id/agents/083b7b32-df2e-4da4-bcc5-b7fe75ea8426
```

## Build & Run ##

```sh
$ cd cool_uris
$ ./sbt
> jetty:start
> browse
```

