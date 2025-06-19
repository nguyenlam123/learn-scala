package example.core

object IsSorted {
  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean =
    @annotation.tailrec
    def loop(n: Int): Boolean =
      if (n == as.length - 1) then true
      else if !gt(as(n), as(n + 1)) then false
      else loop(n + 1)

    loop(0)
}
