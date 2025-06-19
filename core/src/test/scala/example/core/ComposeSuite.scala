package example.core

import example.core.Compose

class ComposeSuite extends munit.FunSuite {
  test("Compose") {
    val f = (b: Int) => b * 2
    val g = (a: Int) => a + 1

    assert(Compose.compose(f, g)(6) == 14)
  }
}
