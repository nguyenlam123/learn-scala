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

  test("Fold right tail rec sum") {
    val list = List(1, 2, 3, 4, 5)
    val res = List.foldRightTailRec(list, 0, _ + _)

    assert(res == 15)
  }

  test("Fold right tail rec product") {
    val list = List(1, 2, 3, 4, 5)
    val resList = List(1, 2, 3, 4)

    assert(List.foldRightTailRec(list, 1.0, _ * _) == 120)
  }

  test("append list") {
    val a1 = List(1, 2, 3, 4, 5)
    val a2 = List(6, 7)
    val res = List(1, 2, 3, 4, 5, 6, 7)
    val res1 = List.append(a1, a2)

    assert(res1 == List(1, 2, 3, 4, 5, 6, 7))
  }

  test("add one to each element in list") {
    val a1 = List(1, 2, 3, 4, 5)
    val res = List(2, 3, 4, 5, 6)

    assert(List.appendOne(a1) == res)
  }

  test("list of doubles to list of strings") {
    val a1: List[Double] = List(1.0, 2.0, 3.0, 4.0, 5.0)
    val res: List[String] = List("1.0", "2.0", "3.0", "4.0", "5.0")

    assert(List.toString(a1) == res)
  }

  test("map product") {
    val a1: List[Double] = List(1.0, 2.0, 3.0, 4.0, 5.0)
    val res: List[Double] = List(2.0, 4.0, 6.0, 8.0, 10.0)

    assert(List.map(a1, (x) => x * 2) == res)
  }

  test("map sum") {
    val a1 = List(1, 2, 3, 4, 5)
    val res = List(2, 3, 4, 5, 6)

    assert(List.map(a1, (x) => x + 1) == res)
  }

  test("filter") {
    val a1 = List(1, 2, 3, 4, 5)
    val res = List(4, 5)

    assert(List.filter(a1, (x) => x > 3) == res)
  }

  test("flat map") {
    val a1 = List(1, 2, 3)
    val res = List(1, 1,2, 2, 3, 3)

    assert(List.flatMap(a1, i => List(i,i)) == res)
  }

   test("filter flat map") {
    val a1 = List(1, 2, 3, 4, 5)
    val res = List(4, 5)

    assert(List.filterWithFlatMap(a1, (x) => x > 3) == res)
  }

  test("add corr") {
    val a1 = List(1, 2, 3)
    val a2 = List(4, 5, 6)
    val res = List(5, 7, 9)
    val act = List.addCorr(a1, a2, Nil: List[Int])

    assert(act == res)
  }

  test("take the product of corresponding elements") {
    val a1 = List(1, 2, 3)
    val a2 = List(4, 5, 6)
    val res = List(4, 10, 18)
    val prod = (a: Int, b: Int) => a * b

    val act = List.corrGen(a1, a2, Nil, prod)

    assert(act == res)
  }

  test("hasSubsequence 1") {
    val a1 = List(1, 2, 3, 4)
    val a2 = List(1, 2)

    val act = List.hasSubsequence(a1, a2, false)

    assert(act == true)
  }

  test("hasSubsequence 2") {
    val a1 = List(1, 2, 3, 4)
    val a2 = List(2, 3)

    val act = List.hasSubsequence(a1, a2, false)

    assert(act == true)
  }

   test("hasSubsequence 3") {
    val a1 = List(1, 2, 3, 4)
    val a2 = List(4)

    val act = List.hasSubsequence(a1, a2, false)

    assert(act == true)
  }

  test("is not a subsequence") {
    val a1 = List(1, 2, 3, 4)
    val a2 = List(2, 4)

    val act = List.hasSubsequence(a1, a2, false)

    assert(act == false)
  }

  test("is not a subsequence 2") {
    val a1 = List(1, 2, 3, 4)
    val a2 = List(5, 6, 7)

    val act = List.hasSubsequence(a1, a2, false)

    assert(act == false)
  }
}
