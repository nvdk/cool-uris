package eu.vandekeybus.cooluris

import org.scalatra._
import org.eclipse.rdf4j.repository._
import org.eclipse.rdf4j.query._
import scala.util.Try
import java.net.URLEncoder

import org.slf4j.{Logger, LoggerFactory}

class WebApi(repository: Repository) extends ScalatraServlet {
  val rdfTypes = Set("text/rdf+n3", "text/rdf+ttl", "text/rdf+turtle", "text/turtle", "text/n3", "application/turtle", "application/x-turtle", "application/rdf+xml", "application/ld+json")
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

    val uris = convertUrl(uri)
    if (uris.isEmpty) {
      halt(400, """{ "error": "query parameter uri is invalid"}""")
    }

    val result = getRepresentations(uris) getOrElse halt(404, s"""{ "error": "no linked data representation of $uri was found"}""")

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

  private def getRepresentations(uris: Set[String]): Option[BindingSet] = {
    val con = repository.getConnection
    val valuesClause = uris.map(url => s"<$url>").mkString(" ", "\n", " ")
    val queryString = s"""
                  SELECT ?pageURL ?dataURL
                  WHERE {
                    VALUES ?uri {
                       $valuesClause
                    }
                    { ?uri <http://www.w3.org/2000/01/rdf-schema#isDefinedBy> ?dataURL. }
                    UNION
                    { ?uri <http://xmlns.com/foaf/0.1/page> ?pageURL. }
                  } LIMIT 1
                """.stripMargin
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

  def convertUrl(urlParam: String): Set[String] = {
    Try(new java.net.URL(urlParam)).toOption match {
      case Some(url) =>
        val path = new java.net.URI(null, null, url.getPath, null).toASCIIString()
        val httpUrl = s"http://${url.getHost}${path}"
        val httpsUrl = s"https://${url.getHost}${path}"

        // Return the URLs as a set
        Set(httpUrl, httpsUrl)
      case None =>
        // Handle invalid URL case
        Set.empty[String]
    }
  }

  private def acceptsRDF:Boolean = {
    rdfTypes.intersect(acceptHeader).size > 0
  }

  private def acceptsHTML:Boolean = {
    htmlTypes.intersect(acceptHeader).size > 0
  }
}
