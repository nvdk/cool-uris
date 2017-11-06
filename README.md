# cool-uris #

A quick and dirty port of [cool-dereferenceable-uris](https://github.com/nvdk/cool-dereferenceable-uris) to scala. Don't judge first scala project using scalatra :).


## Build & Run ##

```sh
$ cd cool_uris
$ ./sbt
> jetty:start
> browse
```

## todo ##
[] currently won't build correctly as a standalone jar
[] uses a hardcoded endpoint, should retrieve it from ENV or java property