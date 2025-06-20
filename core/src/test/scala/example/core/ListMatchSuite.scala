package example.core

import example.core.List.*

class ListMatchSuite extends munit.FunSuite {
  test("Get tail of nil") {
   intercept[RuntimeException] {
      List.tail(Nil)
    }
  }

  test("Get tail of list") {
    val list = List(1, 2, 3, 4, 5)
    val tail = List(2, 3, 4, 5)

    assert(List.tail(list) == tail)
  }
}