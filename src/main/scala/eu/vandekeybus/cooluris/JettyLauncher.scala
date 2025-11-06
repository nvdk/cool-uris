import org.eclipse.jetty.server.Server
import org.eclipse.jetty.ee10.servlet.ServletContextHandler
import org.scalatra.servlet.ScalatraListener

object JettyLauncher { // this is my entry object as specified in sbt project definition
  def main(args: Array[String]) {
    val port = if(System.getenv("PORT") != null) System.getenv("PORT").toInt else 8080

    val server = new Server(port)
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")
    context.addEventListener(new ScalatraListener)

    server.setHandler(context)

    server.start
    server.join
  }
}
