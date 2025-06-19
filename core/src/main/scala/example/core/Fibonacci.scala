package example.core

object Fibonacci {
  def fib(n: Int): Int = {
    @annotation.tailrec
    def go(n: Int, res: Int, acc: Int): Int =
      if (n <= 1) then res
      else go(n - 1, acc, acc + res)

    go(n, 0, 1)
  }
}
