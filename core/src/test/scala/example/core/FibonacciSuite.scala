package example.core

import example.core.Fibonacci

class FibonacciSuite extends   munit.FunSuite {
  test("Fibonacci: Negative integer for params base case") {
    assert(Fibonacci.fib(-1) == 0)
  }

  test("Fibonacci: Zero") {
    assert(Fibonacci.fib(0) == 0)
  }

  test("Fibonacci: One") {
    assert(Fibonacci.fib(1) == 0)
  }

   test("Fibonacci: sixth number") {
    assert(Fibonacci.fib(6) == 5)
  }

  test("Fibonacci: seventh number") {
    assert(Fibonacci.fib(7) == 8)
  }

   test("Fibonacci: eigth number") {
    assert(Fibonacci.fib(8) == 13)
  }
}