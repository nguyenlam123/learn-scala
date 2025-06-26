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

  def map[A, B](t: Tree[A], f: (a: A) => B): Tree[B] = t match
    case Leaf(value) => Leaf(f(value))
    case Branch(l, r) => Branch(map(l, f),  map(r, f))

  def fold[A, B, C](t: Tree[A], f: (a: A) => B, g: (a: Tree[A], b: Tree[A]) => C) = t match
    case Leaf(value) => f(value)
    case Branch(l, r) => g(l, r)

  def foldSize[A](t: Tree[A]): Int = fold(t, (_) => 1, (l: Tree[A], r: Tree[A]) => 1 + foldSize(l: Tree[A]) + foldSize(r: Tree[A]))

  def foldMaximum(t: Tree[Int]): Int = fold(t, (a: Int) => a, (l: Tree[Int], r: Tree[Int]) => foldMaximum(l).max(foldMaximum(r)))
  
  def foldDepth[A](t: Tree[A]): Int = fold(t, (_) => 1, (l: Tree[A], r: Tree[A]) => 1 + foldDepth(l).max(foldDepth(r)))

  def foldMap[A, B](t: Tree[A], f: (a: A) => B): Tree[B] = fold(t, (a: A) => Leaf(f(a)), (l: Tree[A], r: Tree[A]) => Branch(foldMap(l, f),  foldMap(r, f)))
}
