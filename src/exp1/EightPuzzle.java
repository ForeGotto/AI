package exp1;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by deded on 2017/4/12.
 */
public class EightPuzzle {

    static Limbo startLimbo;
    static Limbo destLimbo;


    public static void setInitialState(int... num) {
        startLimbo = new Limbo(num);
        checkIfHasAnswer();
    }

    public static void setGoal(int... num) {
        destLimbo = new Limbo(num);
        Limbo.setDest(num);
        checkIfHasAnswer();
    }

    private static void checkIfHasAnswer() {
        if (destLimbo != null && startLimbo != null) {
            if (destLimbo.getReverse() % 2 != startLimbo.getReverse() % 2) {
                System.out.println("NA");
                System.exit(0);
            }
        }
    }

    static void goA() {
        ArrayList<Limbo> open = new ArrayList<>(100);
//        TreeSet<Limbo> open = new TreeSet<>();
        ArrayList<Limbo> close = new ArrayList<>(1000);
        open.add(startLimbo);
        while (!open.isEmpty()) {
            Limbo curLimbo = open.get(0);

            if (Limbo.isGoal(curLimbo)) {
                collateResult(curLimbo);
                return;
            }
            open.remove(0);
            close.add(curLimbo);

            ArrayList<Limbo> adjacentLimbos = curLimbo.getAdjacent();
            for (Limbo adjacent : adjacentLimbos) {
                //如果扩展节点在 open 表中
                if (open.contains(adjacent)) {
                    if (curLimbo.dis + adjacent.getDisToGoal() < adjacent.dis) {
                        adjacent.setFormer(curLimbo);
                        open.sort(null);
                    }
                    continue;
                }
                //如果扩展节点在 close 表中
                if (close.contains(adjacent)) {
                    if (curLimbo.dis + adjacent.getDisToGoal() < adjacent.dis) {
                        adjacent.setFormer(curLimbo);
                        close.remove(adjacent);
                    }
                    continue;
                }
                //如果扩展节点未被访问过
                open.add(adjacent);
                adjacent.setFormer(curLimbo);
            }
            open.sort(null);
        }//while end
    }

    static void goA1() {
        SortedSet<Limbo> open = new TreeSet<>();
        ArrayList<Limbo> close = new ArrayList<>(10000);
        open.add(startLimbo);
        while (!open.isEmpty()) {
            Limbo curLimbo = open.first();
            open.remove(curLimbo);
            if (Limbo.isGoal(curLimbo)) {
                collateResult(curLimbo);
                return;
            }

            close.add(curLimbo);
            ArrayList<Limbo> adjacentLimbos = curLimbo.getAdjacent();
            for (Limbo adjacent : adjacentLimbos) {
                //如果扩展节点在 open 表中
                if (open.contains(adjacent)) {
                    if (curLimbo.dis + adjacent.getDisToGoal() < adjacent.dis) {
//                        open.remove(adjacent);
                        adjacent.setFormer(curLimbo);
//                        open.add(adjacent);
                    }
                    continue;
                }
                //如果扩展节点在 close 表中
                if (close.contains(adjacent)) {
                    if (curLimbo.dis + adjacent.getDisToGoal() < adjacent.dis) {
                        adjacent.setFormer(curLimbo);
                        close.remove(adjacent);
                    }
                    continue;
                }
                //如果扩展节点未被访问过
                open.add(adjacent);
                adjacent.setFormer(curLimbo);
            }
//            open.sort(null);
        }//while end
    }

    static void collateResult(Limbo lastLimbo) {

        if (lastLimbo.former == null) {
            System.out.println("result:");
            System.out.println(lastLimbo);
            return;
        }
        collateResult(lastLimbo.former);
        System.out.println(lastLimbo);
    }








}
