import eu.vandekeybus.cooluris._
import org.scalatra._
import javax.servlet.ServletContext
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val sparqlEndpoint = "https://stad.gent/sparql"
    val repo = new SPARQLRepository(sparqlEndpoint)
    repo.initialize()
    context.mount(new WebApi(repo), "/*")
  }
}
