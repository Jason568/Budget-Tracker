import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Simple Budget Tracker in Java
 * 
 * Usage:
 *   java BudgetTracker add expense Food 12.50 "Lunch"
 *   java BudgetTracker add income Salary 2000 "Paycheck"
 *   java BudgetTracker list
 *   java BudgetTracker list 2025-09
 *   java BudgetTracker summary
 *   java BudgetTracker summary 2025-09
 *   java BudgetTracker balance
 */
public class BudgetTracker {
    private static final String CSV_FILE = "transactions.csv";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    record Transaction(LocalDate date, String type, String category, BigDecimal amount, String note) {
        String toCsv() {
            return String.format("%s,%s,%s,%s,%s",
                    date.format(DATE_FMT), type, category, amount.toPlainString(), note.replace(",", " "));
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }
        String cmd = args[0].toLowerCase();
        ensureCsvExists();

        try {
            switch (cmd) {
                case "add" -> handleAdd(args);
                case "list" -> handleList(args);
                case "summary" -> handleSummary(args);
                case "balance" -> handleBalance();
                default -> printHelp();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void ensureCsvExists() {
        if (!Files.exists(Path.of(CSV_FILE))) {
            try (PrintWriter out = new PrintWriter(new FileWriter(CSV_FILE, true))) {
                out.println("date,type,category,amount,note");
            } catch (IOException e) {
                throw new RuntimeException("Could not create file: " + e);
            }
        }
    }

    private static List<Transaction> loadTransactions() {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 5);
                if (p.length < 5) continue;
                LocalDate d = LocalDate.parse(p[0], DATE_FMT);
                String type = p[1];
                String cat = p[2];
                BigDecimal amt = new BigDecimal(p[3]).setScale(2, RoundingMode.HALF_UP);
                String note = p[4];
                list.add(new Transaction(d, type, cat, amt, note));
            }
        } catch (IOException e) {
            System.err.println("Read error: " + e);
        }
        return list;
    }

    private static void handleAdd(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("Usage: add <type> <category> <amount> [note]");
            return;
        }
        String type = args[1].toLowerCase();
        if (!type.equals("income") && !type.equals("expense")) {
            System.out.println("Type must be income or expense");
            return;
        }
        String category = args[2];
        BigDecimal amount = new BigDecimal(args[3]).setScale(2, RoundingMode.HALF_UP);
        String note = args.length > 4 ? args[4] : "";
        LocalDate today = LocalDate.now();

        Transaction t = new Transaction(today, type, category, amount, note);
        try (PrintWriter out = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            out.println(t.toCsv());
        }
        System.out.printf("Saved %s of %s in category %s%n", type, amount, category);
    }

    private static void handleList(String[] args) {
        List<Transaction> tx = loadTransactions();
        String monthFilter = args.length > 1 ? args[1] : null;
        if (monthFilter != null) {
            tx.removeIf(t -> !t.date.format(DateTimeFormatter.ofPattern("yyyy-MM")).equals(monthFilter));
        }
        System.out.printf("%-10s %-7s %-12s %-10s %s%n", "Date", "Type", "Category", "Amount", "Note");
        System.out.println("---------------------------------------------------------------");
        for (Transaction t : tx) {
            String amt = t.type.equals("expense") ? "-" + t.amount : t.amount.toPlainString();
            System.out.printf("%-10s %-7s %-12s %-10s %s%n",
                    t.date.format(DATE_FMT), t.type, t.category, amt, t.note);
        }
        System.out.println(tx.size() + " transactions shown");
    }

    private static void handleSummary(String[] args) {
        List<Transaction> tx = loadTransactions();
        String monthFilter = args.length > 1 ? args[1] : null;
        if (monthFilter != null) {
            tx.removeIf(t -> !t.date.format(DateTimeFormatter.ofPattern("yyyy-MM")).equals(monthFilter));
        }
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        for (Transaction t : tx) {
            if (t.type.equals("income")) income = income.add(t.amount);
            else expense = expense.add(t.amount);
        }
        BigDecimal net = income.subtract(expense);
        System.out.println("Summary for " + (monthFilter == null ? "all months" : monthFilter));
        System.out.println("  Income : " + income);
        System.out.println("  Expense: " + expense);
        System.out.println("  Net    : " + net);
    }

    private static void handleBalance() {
        List<Transaction> tx = loadTransactions();
        Map<String, BigDecimal[]> months = new TreeMap<>();
        for (Transaction t : tx) {
            String ym = t.date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            months.putIfAbsent(ym, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            if (t.type.equals("income"))
                months.get(ym)[0] = months.get(ym)[0].add(t.amount);
            else
                months.get(ym)[1] = months.get(ym)[1].add(t.amount);
        }
        for (String m : months.keySet()) {
            BigDecimal inc = months.get(m)[0];
            BigDecimal exp = months.get(m)[1];
            BigDecimal net = inc.subtract(exp);
            System.out.printf("%s: income=%s  expense=%s  net=%s%n", m, inc, exp, net);
        }
    }

    private static void printHelp() {
        System.out.println("""
        Commands:
          add <type> <category> <amount> [note]
          list [YYYY-MM]
          summary [YYYY-MM]
          balance
        Example:
          java BudgetTracker add expense Food 12.50 "Lunch"
          java BudgetTracker list 2025-09
        """);
    }
}

