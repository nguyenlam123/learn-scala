package example.core

enum List[+A]:
  case Nil
  case Cons(head: A, tail: List[A])

object List:
  def apply[A](as: A*): List[A] =
    if as.isEmpty then Nil
    else Cons(as.head, apply(as.tail*))

  def sum(ints: List[Int]): Int = ints match
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)

  def product(doubles: List[Double]): Double = doubles match
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)

import List.*

val result = List(1,2,3,4,5) match 
  case Cons(x, Cons(2, Cons(4, _))) => x // Fails, we need a 3 instead of 4
  case Nil => 42 // Fails, we need a Cons
  case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y // Pass since the cons are in order
  case Cons(h, t) => h + sum(t) // Pass, since sum is defined, yields 15
  case _ => 101 // Pass, matches any expressions