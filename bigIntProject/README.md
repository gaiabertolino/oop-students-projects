# BigInt – Arbitrary-Precision Integers

![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=for-the-badge)
![Encapsulation](https://img.shields.io/badge/Encapsulation-2ea44f?style=for-the-badge)
![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=for-the-badge)
![Constructors & Life Cycle](https://img.shields.io/badge/Constructors_%26_Life_Cycle-bf8700?style=for-the-badge)
![Inheritance](https://img.shields.io/badge/Inheritance-e36209?style=for-the-badge)
![Polymorphism](https://img.shields.io/badge/Polymorphism-cf222e?style=for-the-badge)
![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=for-the-badge)

> Try Part 1 on your own first. Read Part 2 and the reference code only afterwards.

---

## Part 1 – What you are asked to do

Java's `int` and `long` overflow. Build your own type for **non-negative integers of any size**, designed as an interface → abstract class → concrete class hierarchy.

**1. Interface `BigInt extends Comparable<BigInt>, Iterable<Integer>`**
(iterating over a `BigInt` yields its digits, most significant first)
- Abstract methods (each implementation provides them):
  - `BigInt factory(int i)`: build a `BigInt` from an `int`
  - `BigInt add(BigInt a)`
  - `BigInt sub(BigInt s)`: throws `IllegalArgumentException` if the result would be negative
- Default methods, written **only** in terms of the methods above and the iterator:
  - `String value()`, `int length()` (number of digits)
  - `incr()`, `decr()` (`decr()` on 0 throws)
  - `mul(BigInt m)`, `div(BigInt d)`, `rem(BigInt d)`, `pow(int exp)`

**2. Abstract class `AbstractBigInt implements BigInt`**
- Override `equals`, `toString` and `hashCode` once, for every implementation.

**3. Concrete class `BigIntLL extends AbstractBigInt`**
- Store the digits in a `LinkedList<Integer>`.
- Constructors: `()`, `(String s)` (throw `NumberFormatException` on non-digits, strip leading zeros) and `(int i)` (throw on negatives).
- Implement `add`, `sub` and `compareTo` digit by digit, with carry/borrow.
- Optional: override `mul` with a more efficient "pen-and-paper" algorithm.

**4. Class `Main`**: an interactive text menu (using `Scanner`)
- Read a number, then let the user apply operations (increment, decrement, compare, power, length, divide, remainder, add, subtract, multiply).
- Handle invalid input and exceptions without crashing. Typing `STOP` exits.

---

## Part 2 – How the reference solution works

### Class structure
```
        BigInt  (interface: default + abstract methods)
           ▲
   AbstractBigInt  (equals / toString / hashCode)
           ▲
       BigIntLL    (LinkedList<Integer> digits)

       Main        (console menu: readBigInt, operationsMenu)
```

### Key ideas
- **Template-method style defaults.** `incr`, `decr`, `mul`, `div`, `rem` and `pow` in the interface only use `add`, `sub`, `factory`, `compareTo` and `equals`. Any new implementation gets them for free.
  - `mul` = repeated addition (5·6 = 5+5+5+5+5+5)
  - `div` / `rem` = keep adding the divisor while the total stays ≤ `this`
  - `pow` = repeated `mul`, starting from 1
- **`factory(int)`** lets interface code create new numbers without knowing the concrete class.
- **`value()` / `toString()`** use a `StringBuilder` rather than `String +` in a loop, because strings are immutable.
- **`equals` / `compareTo`**: compare lengths first, then digit by digit with two iterators.
- **`BigIntLL.add` / `sub`**: two `ListIterator`s start at the end of `digits` (least significant digit) and walk backwards with a `carry`. A final carry is added at the front (999 + 1 = 1000).
- **`BigIntLL.mul(BigInt)`** overrides the slow default with the schoolbook method: multiply by each digit, shift by a power of 10, and sum the partial products.
- **`Main`** uses a labelled `for(;;)` loop with `switch`. Each risky operation is attempted inside `try/catch`, so bad input never crashes the program.

### How to run
```bash
javac -encoding UTF-8 -d out *.java
java -cp out Bertolino.math.Main        # interactive menu (M = multiply also works)
java -cp out Bertolino.math.BigIntLL    # compares 2^128 with java.math.BigInteger
```

### Questions for discussion
1. `div` stores the quotient in an `int`. What goes wrong for very large quotients? Also, `3 / 5` throws instead of returning 0. Is that right?
2. `AbstractBigInt.equals` compares two `Integer` objects with `!=`. Why does it still work here, and when would it fail?
3. `BigIntLL.add` casts its argument to `BigIntLL`. What does that mean for the interface abstraction?
4. `BigIntLL.mul(BigInt)` keeps `exp` in an `int`. Try multiplying by a number with more than 10 digits.

---

## Concepts practised in detail

### Main focus

- **Objects and classes:** a number is modelled as an object holding a list of digits.
- **Encapsulation:** the digit list is `private`, and helpers such as `removeLeadingZeros` are hidden.
- **References and object identity:** `equals` vs `==`, `equals`/`hashCode` consistency, and operations that return *new* objects.
- **Constructors and life cycle:** three constructors (`()`, `(String)`, `(int)`) and a `factory` method.
- **Inheritance:** `BigIntLL extends AbstractBigInt`.
- **Polymorphism:** default methods call `add`/`sub` through dynamic dispatch, and `BigIntLL` overrides `mul`.
- **Abstract classes and interfaces:** interface → abstract class → concrete class, with default methods.
