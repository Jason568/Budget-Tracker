# Budget Tracker

A console-based budget tracker written in Java for managing personal finances. Transactions are saved locally to a CSV file and persist between sessions.

## Requirements

- Java 16+

## Setup

```bash
javac BudgetTracker.java
```

## Usage

```bash
# Add a transaction
java BudgetTracker add <type> <category> <amount> [note]

# List transactions
java BudgetTracker list
java BudgetTracker list 2025-09

# Show summary
java BudgetTracker summary
java BudgetTracker summary 2025-09

# Show monthly balance breakdown
java BudgetTracker balance
```

## Examples

```bash
java BudgetTracker add expense Food 12.50 Lunch
java BudgetTracker add expense Groceries 85.30
java BudgetTracker add income Salary 2500 Paycheck

java BudgetTracker list
java BudgetTracker list 2025-09
java BudgetTracker summary
java BudgetTracker balance
```

## Commands

| Command | Description |
|---|---|
| `add income` | Record money received (salary, gifts, etc.) |
| `add expense` | Record money spent (food, bills, etc.) |
| `list [YYYY-MM]` | View all transactions, optionally filtered by month |
| `summary [YYYY-MM]` | Show total income, expenses, and net for a period |
| `balance` | Show a month-by-month income and expense breakdown |

## Data Storage

Transactions are saved to `transactions.csv` in the working directory and persist between sessions. Note that commas in transaction notes are automatically replaced with spaces to preserve the CSV format.

## Project Structure

```
budget-tracker/
├── BudgetTracker.java
└── README.md
```
