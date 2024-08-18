package BrainF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
class Interpreter implements Stmt.Visitor<Void> {
    private final List<Integer> arr = new ArrayList<>(Collections.nCopies(30000, 0));
    private int cur = 0;
    private int num = 0;
    final private Scanner sc = new Scanner(System.in);
    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeException error) {
            System.out.print("error");
            sc.close();
            return;
        }
        sc.close();
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    @Override
    public Void visitLoopStmt(Stmt.Loop stmt) {
        executeBlock(stmt.statements);
        return null;
    }

    private Void executeBlock(List<Stmt> statements) {
        while(get() != 0) {
            for(Stmt statement : statements) {
                execute(statement);
            }
        }
        return null;
    }

    @Override
    public Void visitActionStmt(Stmt.Action stmt) {
        switch(stmt.action.type) {
            case TokenType.INCREMENT:
                set(stmt, get() + 1);
                break;
            case TokenType.DECREMENT:
                set(stmt, get() - 1);
                break;
            case TokenType.IN:
                boolean correctInput = false;
                int val = 0;
                while (!correctInput) {
                    try {
                        String input = sc.next();
                        if (input.length() == 1) {
                            int a = Character.getNumericValue(input.charAt(0));
                            if (a < 0 || a > 127) {
                                throw new Exception();
                            }
                            correctInput = true;
                            val = a;
                        }
                    } catch(Exception e) {
                        continue;
                    }
                }
                set(stmt, val);

                break;
            case TokenType.OUT:
                System.out.print((char) (arr.get(cur).intValue()));
                break;
            case TokenType.RIGHT:
                cur++;
                break;
            case TokenType.LEFT:
                cur--;
                break;

        }
        return null;
    }
    private void set(Stmt.Action stmt, int val) {
        try {
            arr.set(cur, val);
        } catch(ArrayIndexOutOfBoundsException e) {
            BF.error(stmt.action, "Array Index out of bounds");
            throw new RuntimeException();
        }
    }

    private int get(Stmt.Action stmt) {
        int a = 0;
        try {
            a = arr.get(cur);

        } catch(ArrayIndexOutOfBoundsException e) {
            BF.error(stmt.action, "Array Index out of bounds");
            throw new RuntimeException();
        }
        return a;
    }
    private int get() {
        int a = 0;
        try {
            a = arr.get(cur);

        } catch(ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
        return a;
    }

}
