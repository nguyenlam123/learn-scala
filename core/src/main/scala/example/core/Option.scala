package example.core

enum Option[+A]:
  case Some(get: A)
  case None

  def mean(xs: Seq[Double]): Option[Double] =
    if xs.isEmpty then None
    else Some(xs.sum / xs.length)
