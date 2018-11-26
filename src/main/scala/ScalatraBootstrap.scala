import eu.vandekeybus.cooluris._
import org.scalatra._
import javax.servlet.ServletContext
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val sparqlEndpoint = sys.env("SPARQL_ENDPOINT")
    val repo = new SPARQLRepository(sparqlEndpoint)
    repo.initialize()
    context.mount(new WebApi(repo), "/*")
  }
}
