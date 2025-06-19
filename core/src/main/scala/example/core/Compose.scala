package example.core

object Compose {
  def compose[A, B, C](f: B => C, g: A => B): A => C = (a: A) => f(g(a))
}
