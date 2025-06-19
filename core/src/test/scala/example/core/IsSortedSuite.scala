package example.core

import example.core.IsSorted

class IsSortedSuite extends munit.FunSuite {
  test("sorted ascending") {
    assert(IsSorted.isSorted(Array(1, 2, 3), _ > _) == true)
  }

  test("not sorted") {
    assert(IsSorted.isSorted(Array(1, 2, 1), _ < _) == false)
  }

  test("sorted descending") {
    assert(IsSorted.isSorted(Array(3, 2, 1), _ < _) == true)
  }

   test("sorted ascending, but trying with greater than comparison function") {
    assert(IsSorted.isSorted(Array(1, 2, 3), _ < _) == false)
  }
}