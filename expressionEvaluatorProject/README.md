# Arithmetic Expression Evaluator (with GUI)

![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=for-the-badge)

*Mainly an algorithms warm-up (stacks and recursion): the other OOP concepts appear only lightly. See the end of this page.*

> Try Part 1 on your own first. Read Part 2 and the reference code only afterwards.

---

## Part 1 – What you are asked to do

Write a Swing application that evaluates arithmetic expressions typed by the user, for example `(2+3)*4-10%3`.

1. **Input:** non-negative integers, the operators `+ - * / % ^`, and parentheses (possibly nested). No spaces.
2. **Validation (class `Regex`):** before evaluating, check that the string is well formed:
   - it does not end with an operator
   - the parentheses are balanced
   - it matches a regular expression for valid expressions
3. **Evaluation (class `Evaluator`):** implement the classic **two-stack algorithm** (an operand stack and an operator stack):
   - respect operator precedence (`^` > `* / %` > `+ -`) and left-to-right order
   - handle parentheses **recursively**
   - throw an exception if the expression cannot be reduced to a single value
4. **GUI (class `EvaluatorGUI`):**
   - a text field plus a "Calculate" button
   - the button is **enabled only while the current input is a valid expression**
   - the result is shown in a separate window
   - closing the main window asks for confirmation

---

## Part 2 – How the reference solution works

### Structure
```
EvaluatorGUI (main) ──creates──► MainWindow (JFrame, ActionListener)
                                   ├─ checker()  → Regex.isWellFormed + Evaluator.evaluate
                                   └─ ResultWindow (result window)
Regex     : isWellFormed(s), isNumber(s)
Evaluator : evaluate(tokenizer), compute(o1,o2,op), hasPriority(op1,op2)
```

### Key ideas
- **Tokenising:** `new StringTokenizer(exp, "+-/%*^()", true)` splits on the operators and parentheses. The `true` flag also **returns the delimiters as tokens**.
- **`evaluate` loop:**
  - a number → push it onto `operands`
  - `(` → call `evaluate` **recursively** on the same tokenizer, then push the result
  - `)` → reduce everything left in this call and return
  - an operator → if it has higher priority than the one on top of `operators`, push it. Otherwise first `compute` the pending operation, push the result, and then push the operator.
  - at the end, empty `operators`. Exactly one operand must remain.
- **`hasPriority(op1, op2)`** decides whether `op1` binds more tightly than `op2`.
- **Live validation:** `checker()` tries to evaluate the text. Any exception (`IllegalArgumentException`, `EmptyStackException`, …) simply disables the button.

### How to run
```bash
javac -encoding UTF-8 -d out *.java
java -cp out Bertolino.expression.EvaluatorGUI
```

### Questions for discussion
1. In Java, what does the `^` operator do on `int`s? Is `2^3` evaluated correctly?
2. `main` runs `for(;;) window.checker();`. What does this cost in CPU time, and why is it unsafe with Swing? How would a `DocumentListener` help?
3. Why are negative numbers (for example `-3+2`) not supported, and what would you change to support them?
4. All the arithmetic is done on `String`s parsed to `int`. What about overflow and integer division?

---

## Concepts practised in detail

### Main focus

- **Objects and classes:** responsibilities split into separate classes (`Regex` validates, `Evaluator` computes, `MainWindow` interacts); `static` utility methods vs GUI objects.

### Touched on

- **Encapsulation:** private helpers (`compute`, `hasPriority`), and `protected` / package-private access.
- **References and object identity:** `e.getSource() == calculateButton` (identity) vs `token.equals("(")` (equality).
- **Constructors and life cycle:** the window is built in its constructor, and a new `ResultWindow` object is created on each click.
- **Inheritance:** `MainWindow extends JFrame`, plus an anonymous subclass of `WindowAdapter`.
- **Polymorphism:** Swing calls the overridden `actionPerformed` and `windowClosing` (callbacks).
- **Interfaces:** `MainWindow implements ActionListener`.
