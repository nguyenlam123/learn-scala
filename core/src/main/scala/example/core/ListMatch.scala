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

  def tail[A](list: List[A]): List[A] = (list: List[A]) match
    case Nil => sys.error("message")
    case Cons(_, t) => t

  def setHead[A](h: A, list: List[A]): List[A] = list match
    case Nil => Cons(h, Nil)
    case Cons(_, t) => Cons(h, t)

  def drop[A](as: List[A], n: Int): List[A] = as match
    case Nil => Nil
    case Cons(_, t) => {
      if (as == Nil) then Nil
      else if (n == 0) then as
      else drop(t, n - 1)
    }

  def dropWhile[A](as: List[A], f: A => Boolean): List[A] = as match
    case Nil => Nil
    case Cons(h, t) => {
      if (f(h)) then dropWhile(t, f)
      else Cons(h, t)
    }

  def init[A](as: List[A]): List[A] = as match
    case Nil => Nil
    case Cons(h, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))

  def foldRight[A, B](as: List[A], acc: B, f: (A, B) => B): B =
    as match
      case Nil => acc
      case Cons(x, xs) => f(x, foldRight(xs, acc, f))

  def length[A](as: List[A]): Int = as match
    case Nil => 0
    case Cons(x, xs) => foldRight(as, 0, (_, y) => y + 1)

  @annotation.tailrec
  def foldLeft[A, B](as: List[A], acc: B, f: (B, A) => B): B = as match
      case Nil => acc
      case Cons(x, xs) => foldLeft(xs, f(acc, x), f)

  def reverse[A](as: List[A]): List[A] = as match
    case Nil => Nil
    case Cons(x, xs) => foldLeft(as, Nil:List[A], (xs, x) => Cons(x, xs))

  def append[A](a1: List[A], a2: List[A]): List[A] =
    (a1, a2) match
      case (Nil, Nil) => Nil
      case (a1, Nil) => a1
      case (Nil, a2) => a2
      case (a1, a2) => foldLeft(reverse(a1), a2, (xs, x) => Cons(x, xs))

  def appendOne[A](as: List[Int]): List[Int] =
    as match
      case Nil => Nil
      case Cons(x, xs) => foldLeft(reverse(as), Nil: List[Int], (xs, x) => Cons(x + 1, xs))

  def toString[Double](as: List[Double]): List[String] =
    as match
      case Nil => Nil
      case Cons(x, xs) => foldLeft(reverse(as), Nil: List[String], (xs, x) => Cons(x.toString(), xs))

  def map[A, B](as: List[A], f: A => B): List[B] =
    as match
      case Nil => Nil
      case Cons(x, xs) => foldLeft(reverse(as), Nil: List[B], (xs, x) => Cons(f(x), xs))

  def filter[A](as: List[A], f: A => Boolean): List[A] =
    as match
      case Nil => Nil
      case Cons(x, xs) => foldLeft(reverse(as), Nil: List[A], (xs, x) => {
        if (f(x)) then Cons(x, xs)
        else xs
      })

  def flatMap[A, B](as: List[A], f: A => List[B]): List[B] =
    as match
      case Nil => Nil
      case Cons(x, xs) => foldLeft(reverse(as), Nil: List[B], (xs, x) => append(f(x), xs))
  
  def filterWithFlatMap[A](as: List[A], f: A => Boolean): List[A] =
    as match
      case Nil => Nil
      case Cons(x, xs) => flatMap(as, (x) => {
        if (f(x)) then List(x)
        else Nil
      })

  def addCorr(a1: List[Int], a2: List[Int], res: List[Int]): List[Int] =
    (a1, a2) match
      case (Cons(x, xs), Cons(y, ys)) =>  addCorr(xs, ys, Cons(x + y, res))
      case _ => reverse(res)

  def corrGen[A](a1: List[A], a2: List[A], res: List[A], f: (a: A, b: A) => A): List[A] =
    (a1, a2) match
      case (Cons(x, xs), Cons(y, ys)) =>  corrGen(xs, ys, Cons(f(x, y), res), f)
      case _ => reverse(res)

  def hasSubsequence[A](sup: List[A], sub: List[A], hasStarted: Boolean): Boolean = 
    (sup, sub) match
      case (_, Nil) => true
      case (Nil, Cons(_, _)) => false // If we have iterated through the sup list and still don't have a match, return false
      case  (Cons(x, xs), Cons(y, ys)) => {
        if (x != y && filter(Cons(x, xs), (z) => z == y) == Nil) then false // If the current element is not equal and we can't find it further in the sequence, return false
        if (x != y && hasStarted) then false // If they are not equal and a sequence have been started, return false
        else if (x == y) then hasSubsequence(xs, ys, true) // Move on in the sub list if they are equal. Kick in the flag indicating that a sequence has been started
        else hasSubsequence(xs, Cons(y, ys), hasStarted) // Stay if they are not equal
      }
    
import List.*

val result = List(1,2,3,4,5) match 
  case Cons(x, Cons(2, Cons(4, _))) => x // Fails, we need a 3 instead of 4
  case Nil => 42 // Fails, we need a Cons
  case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y // Pass since the cons are in order
  case Cons(h, t) => h + sum(t) // Pass, since sum is defined, yields 15
  case _ => 101 // Pass, matches any expressions