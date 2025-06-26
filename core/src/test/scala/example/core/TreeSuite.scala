package example.core

import example.core.Tree.*

class TreeSuite extends munit.FunSuite {
  test("Get size of tree") {
    val left = Leaf(1)
    val right = Leaf(2)

    val tree = Branch(left, right)

    assert(Tree.size(tree) == 3)
  }
}
