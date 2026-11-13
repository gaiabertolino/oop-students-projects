# Sudoku with Backtracking (with GUI)

![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=for-the-badge)
![Encapsulation](https://img.shields.io/badge/Encapsulation-2ea44f?style=for-the-badge)
![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=for-the-badge)
![Inheritance](https://img.shields.io/badge/Inheritance-e36209?style=for-the-badge)
![Polymorphism](https://img.shields.io/badge/Polymorphism-cf222e?style=for-the-badge)
![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=for-the-badge)

> Try Part 1 on your own first. Read Part 2 and the reference code only afterwards.

---

## Part 1 – What you are asked to do

**Given (starting code):** the generic abstract class `Backtracking<P,S>`.
- `P` = a choice point, `S` = a choice.
- Its `attempt` method is a **template method**: for each admissible choice it checks `isAssignable`, calls `assign`, either records a solution or recurses on the next choice point, and then calls `unassign`.

**Your task:**
1. **`ChoicePoint`:** an immutable cell `(i, j)` with getters, `toString` and `equals`.
2. **`Sudoku extends Backtracking<ChoicePoint, Integer>`:**
   - a 9×9 board (`0` = empty) and the list of all 81 choice points
   - implement the hooks: `isAssignable` (check the row, column and 3×3 subgrid), `assign`, `unassign`, `choices` (the values 1–9), `recordSolution`, `isSolutionComplete`, `isLastSolution` and `solve`
   - **Cells already filled by the user must be respected**: the solver must not overwrite them, and you must not modify `Backtracking` to achieve this.
   - store the solutions found in a list, and stop after a fixed number of solutions
3. **GUI (`SudokuGUI`):**
   - a welcome window with the rules and a "start" button
   - a game window with a 9×9 grid of text fields. Each entry is validated: it must be a number from 1 to 9 that respects the rules, and once accepted the cell is locked.
   - buttons: **show solutions**, **new game**, **save** and **load**
     - save/load use a `.txt` file of 9 lines with 9 space-separated numbers; `0` = empty
     - save can optionally create a new file first
   - a solutions window with **NEXT / PREVIOUS** to browse the solutions, and **NEW GAME**
   - error windows for invalid input or file problems; closing any window asks for confirmation

---

## Part 2 – How the reference solution works

### Structure
```
Backtracking<P,S>  (given: template method attempt)
        ▲
Sudoku extends Backtracking<ChoicePoint,Integer>   ── uses ──► ChoicePoint(i,j)

SudokuGUI (main) ─► WelcomeWindow ─► GameWindow ─► SolutionsWindow
                    (rules)          (grid, save/load,   (browse solutions)
                                      ErrorWindow, FileInputWindow, TxtFileFilter)
```

### Key ideas
- **Search order:** the choice points are the cells, in row-major order (0,0) → (8,8). `solve()` starts `attempt` at (0,0). A solution is complete when a value has been placed in the last cell (8,8).
- **`isAssignable(ps, s)`:**
  1. Throws if `s` is not in 1–9.
  2. **Trick for pre-filled cells:** if the cell already contains `s`, the method itself continues the search to the next cell and then returns `false`, so the cell is never overwritten. Every other value is rejected for that cell.
  3. Otherwise it checks the row, the column and the 3×3 subgrid (`checkSubgrid`).
- **Collecting solutions:** `recordSolution` copies the board into the `solutions` list and increments `solutionCount`. `isLastSolution` stops the search after 11 solutions.
- **`getBoard()`** returns a **deep copy** of the board (encapsulation).
- **GUI:**
  - an `ActionListener` dispatches on `e.getSource()`
  - file I/O uses `BufferedWriter` / `BufferedReader` + `StringTokenizer`
  - a `JFileChooser` with a custom `FileFilter` shows only `.txt` files
  - `updateButtons()` enables or disables NEXT / PREVIOUS

### How to run
```bash
javac -encoding UTF-8 -d out *.java
java -cp out Bertolino.sudoku.SudokuGUI
```

### Questions for discussion
1. `isAssignable` is meant to be a *check*, yet it can start a recursive search. Is that good design? What would you change?
2. The solver stops at 11 solutions, but `updateButtons()` disables NEXT at index 9. What happens if the puzzle has only 1 solution, or none?
3. `Sudoku.solutionCount` and `GameWindow.game` are `static`. What problems does shared static state cause?
4. `checkSubgrid` uses nine `if/else` branches. Can you compute the top-left corner of the 3×3 subgrid in one line?

---

## Concepts practised in detail

### Main focus

- **Objects and classes:** a game model (`Sudoku`), a value object (`ChoicePoint`) and window classes.
- **Encapsulation:** a `private` board, with `getBoard()` returning a **deep copy**.
- **References and object identity:**
  - `ChoicePoint.equals` (used by `List.indexOf`)
  - copying arrays (deep vs shallow)
  - shared `static` state
- **Inheritance:** `Sudoku extends Backtracking<ChoicePoint, Integer>`; the windows extend `JFrame`.
- **Polymorphism:** the *template method* `attempt` in `Backtracking` calls hooks overridden in `Sudoku`.
- **Abstract classes and interfaces:** the generic abstract class `Backtracking<P,S>`; `ActionListener`; the abstract class `FileFilter`.

### Touched on

- **Constructors and life cycle:** windows are created, hidden and re-shown; the game state is reset for a new game.
