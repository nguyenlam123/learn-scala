package example.core

enum Tree[+A]:
  case Leaf(value: A)
  case Branch(left: Tree[A], right: Tree[A])

object Tree {
  def size[A](t: Tree[A]): Int = t match
    case Leaf(_) => 1
    case Branch(l, r) => 1 + size(l) + size(r)

  def maximum(t: Tree[Int]): Int = t match
    case Leaf(value) => value
    case Branch(l, r) => maximum(l).max(maximum(r))

  def depth[A](t: Tree[A]): Int = t match
    case Leaf(_) => 1
    case Branch(l, r) => 1 + depth(l).max(depth(r))
}
