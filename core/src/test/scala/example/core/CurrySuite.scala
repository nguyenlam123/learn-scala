package example.core

import example.core.Curry

class CurrySuite extends munit.FunSuite {
  test("currying") {
    assert(Curry.curry((a: Int, b: Int) => a + b)(13)(8) == 21)
  }
}