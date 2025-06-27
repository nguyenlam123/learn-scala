package example.core

import example.core.Option.*

class OptionSuite extends munit.FunSuite {
  test("mean of non-empty sequence") {
    val sequence = Seq(1.0, 2.0, 3.0)
    assertEquals(Option.None.mean(sequence), Some(2.0))
  }

  test("map") {
    assertEquals(Option.Some(2.0).map((x) => x * 2), Some(4.0))
  }

  test("getOrElse: Return result of some") {
    assertEquals(Option.Some(2.0).getOrElse(0.0), 2.0)
  }

  test("getOrElse: Return default") {
    assertEquals(Option.None.getOrElse(0.0), 0.0)
  }
}
