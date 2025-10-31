BUDGET TRACKER JAVA APPLICATION
DESCRIPTION
A simple console-based budget tracker written in Java for managing personal finances.

QUICK START
Compile: javac BudgetTracker.java
Run: java BudgetTracker

BASIC COMMANDS
Add transaction: java BudgetTracker add type category amount note
View transactions: java BudgetTracker list YYYY-MM
Show summary: java BudgetTracker summary YYYY-MM
Monthly balance: java BudgetTracker balance
Help: java BudgetTracker

HOW TO USE
ADDING TRANSACTIONS
Add expense:
java BudgetTracker add expense Food 12.50 Lunch
Add income:
java BudgetTracker add income Salary 2000 Paycheck

VIEWING DATA
All transactions:
java BudgetTracker list
September 2025 only:
java BudgetTracker list 2025-09

Financial summary:
java BudgetTracker summary

TRANSACTION TYPES
income: Money received like salary, gifts
expense: Money spent like food, bills

DATA STORAGE
Automatically saves to transactions.csv
Keeps data between sessions
Simple file format

FEATURES

Track income and expenses
Filter by month
Financial summaries
Category organization
Notes for transactions

EXAMPLE WORKFLOW
java BudgetTracker add expense Groceries 85.30
java BudgetTracker add income Salary 2500
java BudgetTracker list
java BudgetTracker summary
