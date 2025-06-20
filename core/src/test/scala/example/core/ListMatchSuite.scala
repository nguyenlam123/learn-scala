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

  test("Set head for empty list") {
    val resList = List(1)

    assert(List.setHead(1, Nil) == resList)
  }

  test("Set head for list") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(99, 2, 3, 4, 5)

    assert(List.setHead(99, list) == resList)
  }

  test("Drop an empty list") {
    assert(List.drop(Nil, 2) == Nil)
  }

  test("Drop two elements from list") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(3, 4, 5)

    assert(List.drop(list, 2) == resList)
  }

  test("Drop all elements from list") {
    val list = List(1, 2, 3, 4, 5)
    
    assert(List.drop(list, 5) == Nil)
  }

  test("Drop more elements than list length") {
    val list = List(1, 2, 3, 4, 5)
    
    assert(List.drop(list, 6) == Nil)
  }

  test("Drop while an empty list") {
    val predicate = (x: Int) => x < 3

    assert(List.dropWhile(Nil, predicate) == Nil)
  }

  test("Drop while list") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(3, 4, 5)
    val predicate = (x: Int) => x < 3

    assert(List.dropWhile(list, predicate) == resList)
  }

   test("Init nil") {
    assert(List.init(Nil) == Nil)
  }

  test("Init list one element") {
    assert(List.init(List(1)) == Nil)
  }

   test("Init list") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(1, 2, 3, 4)

    assert(List.init(list) == resList)
  }
}
