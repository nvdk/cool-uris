package eu.vandekeybus.cooluris

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class WebApiTests extends ScalatraSuite with FunSuiteLike {

  addServlet(classOf[WebApi], "/*")

  test("GET / on WebApi should return status 400"){
    get("/"){
      status should equal (400)
    }
  }

}
