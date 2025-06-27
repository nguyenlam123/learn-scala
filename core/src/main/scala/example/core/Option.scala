package example.core

enum Option[+A]:
  case Some(get: A)
  case None

  def mean(xs: Seq[Double]): Option[Double] =
    if xs.isEmpty then None
    else Some(xs.sum / xs.length)

  def map[B](f: A => B): Option[B] = this match
    case Some(get: A) => Some(f(get))
    case None => None

  def getOrElse[B >: A](default: => B): B = this match
    case Some(get: B) => get
    case None => default
