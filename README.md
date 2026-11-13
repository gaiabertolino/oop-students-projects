# Object-Oriented Programming – Practice Projects

This repository is for the students I supervise on the **Object-Oriented Programming** course at the University of Cambridge.

It collects some of the Java projects I wrote during my Bachelor's in Computer Engineering, as coursework for my own OOP exam. Each one is a small, self-contained exercise that puts the core OOP ideas into practice.

> **Spot the bug!** The code is **deliberately left with a few minor bugs and design weaknesses**. Each project ends with some **questions for discussion** that guide you to find them. Answering these questions is part of the exercise: reading someone else's code critically is as important as writing your own.

> **Note:** the projects were originally written in Italian and have been **translated into English with Claude** (code identifiers, comments, messages and documentation). The logic has not been changed, but small typos or slightly odd wording may still be present. If you spot one, feel free to open an issue or a pull request.

---

## Projects (from easiest to hardest)

| # | Project | Difficulty | Concepts practised |
|:-:|---|:-:|---|
| 1 | [**Expression Evaluator**](expressionEvaluatorProject/)<br><sub>Evaluate arithmetic expressions with a two-stack algorithm and a small Swing GUI. Mainly an algorithms warm-up.</sub> | ★☆☆☆☆ | ![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=flat-square) |
| 2 | [**BigInt**](bigIntProject/)<br><sub>Arbitrary-precision integers built as an interface → abstract class → concrete class hierarchy</sub> | ★★☆☆☆ | ![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=flat-square) ![Encapsulation](https://img.shields.io/badge/Encapsulation-2ea44f?style=flat-square) ![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=flat-square) ![Constructors & Life Cycle](https://img.shields.io/badge/Constructors_%26_Life_Cycle-bf8700?style=flat-square) ![Inheritance](https://img.shields.io/badge/Inheritance-e36209?style=flat-square) ![Polymorphism](https://img.shields.io/badge/Polymorphism-cf222e?style=flat-square) ![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=flat-square) |
| 3 | [**Polynomial Calculator**](polynomialCalculatorProject/)<br><sub>Parse polynomials with regular expressions, operate on them in a GUI, and save/load them from files</sub> | ★★★☆☆ | ![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=flat-square) ![Inheritance](https://img.shields.io/badge/Inheritance-e36209?style=flat-square) ![Polymorphism](https://img.shields.io/badge/Polymorphism-cf222e?style=flat-square) ![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=flat-square) |
| 4 | [**Self-Balancing Binary Search Tree**](balancedBinarySearchTreeProject/)<br><sub>A generic BST that rebalances itself, with a stack-based iterator</sub> | ★★★★☆ | ![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=flat-square) ![Encapsulation](https://img.shields.io/badge/Encapsulation-2ea44f?style=flat-square) ![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=flat-square) ![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=flat-square) |
| 5 | [**Sudoku with Backtracking**](sudokuProject/)<br><sub>Solve Sudoku through a generic backtracking template, with a full GUI and save/load</sub> | ★★★★★ | ![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=flat-square) ![Encapsulation](https://img.shields.io/badge/Encapsulation-2ea44f?style=flat-square) ![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=flat-square) ![Inheritance](https://img.shields.io/badge/Inheritance-e36209?style=flat-square) ![Polymorphism](https://img.shields.io/badge/Polymorphism-cf222e?style=flat-square) ![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=flat-square) |

The badges show only the concepts each project trains **in depth**. Each project's README explains *where* they show up, and also lists the concepts that are only touched on.

---

## How to use these projects

Every project folder contains a `README.md` with three parts:

1. **Concepts practised:** which OOP ideas the exercise trains, and where.
2. **Part 1 – What you are asked to do:** the specification. **Try to solve it yourself first**, without looking at the code.
3. **Part 2 – How the reference solution works:** a short, schematic explanation of my solution, plus a few **questions for discussion**. These lead you to the bugs and design weaknesses deliberately left in the code. Try to answer them before your supervision.

## Running the code

You need **Java 9 or later**. From inside a project folder:

```bash
javac -encoding UTF-8 -d out *.java
java -cp out <package>.<MainClass>     # the exact command is in each project's README
```
