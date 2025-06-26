package example.core

import example.core.Tree.*

class TreeSuite extends munit.FunSuite {
  test("Get size of tree") {
    val left = Leaf(1)
    val right = Leaf(2)

    val tree = Branch(left, right)

    assert(Tree.size(tree) == 3)
  }

  test("Get maximum element of tree") {
    val left = Leaf(1)
    val right = Leaf(2)

    val tree = Branch(left, right)

    assert(Tree.maximum(tree) == 2)
  }

  test("Get maximum element of tree 2") {
    val left = Leaf(1)
    val right = Branch(Leaf(10), Leaf(99))

    val tree = Branch(left, right)

    assert(Tree.maximum(tree) == 99)
  }

  test("Get maximum element of tree 3") {
    val left = Branch(Branch(Branch(Leaf(4), Leaf(101)), Branch(Leaf(5), Leaf(6))), Leaf(7))
    val right = Branch(Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))), Leaf(99))

    val tree = Branch(left, right)

    assert(Tree.maximum(tree) == 101)
  }

  test("Get depth of tree") {
    val left = Leaf(1)
    val right = Branch(Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4))), Leaf(99))

    val tree = Branch(left, right)

    assert(Tree.depth(tree) == 5)
  }
}
