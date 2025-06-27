package example.core

import example.core.Option.*

class OptionSuite extends munit.FunSuite {
  test("mean of non-empty sequence") {
    val sequence = Seq(1.0, 2.0, 3.0)
    assertEquals(Option.None.mean(sequence), Some(2.0))
  }
}