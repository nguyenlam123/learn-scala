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

   test("Fold right sum") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(1, 2, 3, 4)

    assert(List.foldRight(list, 0, _ + _) == 15)
  }

  test("Fold right product") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(1, 2, 3, 4)

    assert(List.foldRight(list, 1.0, _ * _) == 120)
  }

   test("Fold right, compute the length of a nil list") {
    val list = Nil

    assert(List.length(list) == 0)
  }

  test("Fold right, compute the length of a nil list") {
    val list = List(1, 2, 3, 4, 5)

    assert(List.length(list) == 5)
  }

  test("Fold left sum") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(1, 2, 3, 4)

    assert(List.foldLeft(list, 0, _ + _) == 15)
  }

  test("Fold left product") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(1, 2, 3, 4)

    assert(List.foldLeft(list, 1.0, _ * _) == 120)
  }

  test("Fold left length") {
    val list = List(1, 2, 3, 4, 5)

    assert(List.foldLeft(list, 0, (x, _) => x + 1) == 5)
  }

  test("reverse list") {
    val list = List(1, 2, 3, 4, 5)
    val reversedList = List(5, 4, 3, 2, 1)
    val res = List.reverse(list)

    assert(res == reversedList)
  }

  test("append list") {
    val a1 = List(1, 2, 3, 4, 5)
    val a2 = List(6, 7)
    val res = List(1, 2, 3, 4, 5, 6, 7)
    val res1 = List.append(a1, a2)

    assert(res1 == List(1, 2, 3, 4, 5, 6, 7))
  }
}
