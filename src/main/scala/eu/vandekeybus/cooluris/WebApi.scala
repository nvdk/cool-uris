package eu.vandekeybus.cooluris

import org.scalatra._
import org.eclipse.rdf4j.repository._
import org.eclipse.rdf4j.query._

import org.slf4j.{Logger, LoggerFactory}

class WebApi(repository: Repository) extends ScalatraServlet {
  val rdfTypes = Set("text/rdf+n3", "text/rdf+ttl", "text/rdf+turtle", "text/turtle", "text/n3", "application/turtle", "application/x-turtle", "application/rdf+xml")
  val htmlTypes = Set("text/html", "text/*", "*/*")
  val logger =  LoggerFactory.getLogger(getClass)

  get("/") {
    val uri = params("uri")

    response.setHeader("Vary", "Accept")
    response.setHeader("Cache-Control", "max-age=30,must-revalidate")
    response.setHeader("Content-Type","application/json")

    if (uri.isEmpty) {
      halt(400, """{ "error": "query parameter uri is required"}""")
    }
    val result = getRepresentations(uri) getOrElse halt(404, s"""{ "error": "no linked data representation of $uri was found"}""")

    if (acceptsRDF && stringVal(result, "dataURL").nonEmpty)
      halt(303,"", Map("Location" -> stringVal(result, "dataURL")))
    else if (acceptsHTML && stringVal(result, "pageURL").nonEmpty)
      halt(303,"", Map("Location" -> stringVal(result, "pageURL")))
    else if (acceptsHTML && stringVal(result, "dataURL").nonEmpty)
      halt(303,"", Map("Location" -> stringVal(result, "dataURL")))
    else
      halt(406, s"""{ "error": "$uri is not available in the requested format"}""")
  }

  private def acceptHeader = { request.header("accept").getOrElse("").split(",").toSet }
  private def stringVal(b: BindingSet, prop: String) = {
    if (b.hasBinding(prop))
      b.getBinding(prop).getValue.stringValue
    else
      ""
  }
  private def getRepresentations(uri: String): Option[BindingSet] = {
    val con = repository.getConnection
    val queryString = s"""
                  SELECT ?pageURL ?dataURL
                  WHERE {
                    OPTIONAL { <$uri> <http://www.w3.org/2000/01/rdf-schema#isDefinedBy> ?dataURL. }
                    OPTIONAL { <$uri> <http://xmlns.com/foaf/0.1/page> ?pageURL. }
                  } LIMIT 1
                """
    val result = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString).evaluate
    if (result.hasNext) {
      val r = result.next
      if (stringVal(r,"dataURL").trim.nonEmpty || stringVal(r,"pageURL").nonEmpty )
        new Some(r)
      else
        None
    }
    else
      None
  }

  private def acceptsRDF:Boolean = {
    rdfTypes.intersect(acceptHeader).size > 0
  }

  private def acceptsHTML:Boolean = {
    htmlTypes.intersect(acceptHeader).size > 0
  }
}
