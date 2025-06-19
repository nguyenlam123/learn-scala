package example.core

import example.core.Curry
import example.core.Uncurry

class UncurrySuite extends munit.FunSuite {
  test("uncurrying") {
    val sumCurry = Curry.curry((a: Int, b: Int) => a + b)

    assert(Uncurry.uncurry(sumCurry)(13, 8) == sumCurry(13)(8))
  }
}