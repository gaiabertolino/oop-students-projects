# Polynomial Calculator (with GUI)

![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=for-the-badge)
![Inheritance](https://img.shields.io/badge/Inheritance-e36209?style=for-the-badge)
![Polymorphism](https://img.shields.io/badge/Polymorphism-cf222e?style=for-the-badge)
![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=for-the-badge)

*The OOP model classes (`Monomial`, `Polynomial`, …) are given: you practise these concepts mostly by reading, using and extending them.*

> Try Part 1 on your own first. Read Part 2 and the reference code only afterwards.

---

## Part 1 – What you are asked to do

**Given (starting code):** a polynomial library in one variable `x` with integer coefficients.

| Class | Role |
|---|---|
| `Monomial` | `coeff`·x^`degree`, immutable, `Comparable` (by degree) |
| `Polynomial` | interface, `Iterable<Monomial>`: default `add`, `mul`, `derivative`, `valueAt` |
| `AbstractPolynomial` | abstract class: `toString`, `equals`, `hashCode` |
| `PolynomialSet` | concrete polynomial backed by a `TreeSet<Monomial>` |

**Your task:**
1. **Parsing (class `Regex`):**
   - check with a regular expression whether a string such as `3x^2-5x+4` is a valid polynomial
   - split the string into monomials and build a `PolynomialSet`
   - handle implicit coefficients (`x` = 1x, `-x^3` = −1x³) and implicit degrees (`5` = 5x⁰, `5x` = 5x¹)
2. **GUI (class `PolynomialGUI`):**
   - a text field to type a polynomial; each polynomial is added as a **checkbox** to a list (maximum 64)
   - an *Operations* menu whose items are **enabled according to how many polynomials are selected**:

     | Selected | Enabled operations |
     |---|---|
     | 1 | evaluate at a value of x, derivative, remove |
     | 2 | addition, multiplication, derivative, remove |
     | more than 2 | derivative, remove |

   - each result is added to the list, so you can keep operating on it
   - a *File* menu: **save** the selected polynomials to a `.txt` file (one per line, optionally creating a new file) and **load** polynomials from a `.txt` file
   - every error (invalid input, missing file, limit reached, …) is shown in an error window, never as a crash
   - closing the main window asks for confirmation

---

## Part 2 – How the reference solution works

### Structure
```
Polynomial (interface) ◄── AbstractPolynomial ◄── PolynomialSet ──uses──► Monomial
Regex        : isPolynomial(s)    → regex check
               parse(s)           → splits at each + / −, calls parseMonomial(s, i)
PolynomialGUI (main) ──► MainWindow (JFrame, ActionListener)
               ├─ LinkedList<PolynomialSet> polynomials   (index i ↔ i-th checkbox)
               ├─ ErrorWindow / OutputWindow / ValueInputWindow / FileInputWindow
               └─ TxtFileFilter
```

### Key ideas
- **Why `PolynomialSet`:** a `TreeSet` keeps the monomials sorted by degree. Because `Monomial.equals` and `compareTo` look only at the degree, like terms are found and merged inside `add`.
- **Regex `POLYNOMIAL`:** an optional leading `-`, then monomials of the form `[sign]digits[x | x^digits]`. It rejects things like `5xx^3` or `5x^^2`.
- **`parseMonomial(s, i)`:** reads one character at a time, using a boolean flag `x`:
  - before `x` is seen → the characters are coefficient digits
  - after `x` → the characters (skipping `^`) are the degree
  - it stops at the next `+` / `-`, then fills in the implicit coefficient or degree
- **Keeping the list and the GUI in sync:** checkbox *i* in the panel corresponds to `polynomials.get(i)`.
- **`refresh()`:** counts the selected checkboxes (`counter()`) and calls `menuNone` / `menuOne` / `menuTwo` / `menuMore` to enable the right menu items.
- **Files:** written with `BufferedWriter` and read with `BufferedReader`. Each line read is validated with `Regex` before being added.

### How to run
```bash
javac -encoding UTF-8 -d out *.java
java -cp out Bertolino.polynomials.PolynomialGUI
```

### Questions for discussion
1. `Monomial.equals` says `3x²` equals `5x²`. Why is this convenient here, and why is it dangerous in general?
2. `PolynomialSet.add` scans the whole set to find a monomial of the same degree. Can you use the `TreeSet` more efficiently?
3. `addSelected()` and `multiplySelected()` are almost identical. How would you refactor them?
4. How would you extend the parser to accept spaces, a leading `+`, or rational coefficients?

---

## Concepts practised in detail

### Main focus

- **References and object identity:**
  - keeping `equals`, `hashCode` and `compareTo` consistent (and what happens when `equals` only looks at the degree)
  - `==` on event sources
- **Inheritance:** `PolynomialSet extends AbstractPolynomial`; the windows extend `JFrame`.
- **Polymorphism:** the default methods in `Polynomial` (`add`, `mul`, `derivative`, `valueAt`) work on any implementation.
- **Abstract classes and interfaces:** interface `Polynomial` → abstract class `AbstractPolynomial` → concrete class `PolynomialSet`; `Comparable`, `Iterable`, `ActionListener`.

### Touched on

- **Objects and classes:** monomials and polynomials as objects; GUI windows as objects.
- **Encapsulation:** `Monomial` is immutable (`private final` fields, getters only), and the set of monomials is `private`.
- **Constructors and life cycle:** normal and copy constructors (`Monomial(Monomial m)`); the factory method `create()`.
