package exp1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by deded on 2017/4/12.
 *
 * 决定采用位操作实现生成相邻状态，计算距离目标状态的距离
 */
public class Limbo implements Comparable {
    static final long[] bit010;//数组中第 i 个元素的第 4*i 到第 4*(i+1) 位为1， 其他位为0
    static final long[] bit101;//数组中第 i 个元素的第 4*i 到第 4*(i+1) 位为0， 其他位为1
    static final long bit011;//高 8 位为0，其他位为1
    static final long bit100;//高 8 位为1，其他位为0
    static long dest;
    static {
        bit010 = new long[9];
        bit101 = new long[9];
        bit010[0] = 15;
        bit101[0] = ~bit010[0];
        for (int i = 1; i < 9; i++) {
            bit010[i] = bit010[i-1] << 4;
            bit101[i] = ~bit010[i];
        }
        long tmpBit011 = 0;
        bit011 = (~tmpBit011) >>> 8;
        bit100 = ~bit011;
//        System.out.println(Arrays.toString(bit010));
//        System.out.println(Arrays.toString(bit101));
//        System.out.printf("%x\n%x", bit011, bit100);
    }

    public static void setDest(int... num) {
        dest = calList(num);
    }

    public static boolean isGoal(Limbo limboToCheck) {
        if (limboToCheck.list == dest) {
            return true;
        }
        return false;
    }

    /**
     * 压缩码的表示：
     * 前八位表示0的位置，最低4位表示第1个数字，依次递增表示各个位置的数字
     */
    long list;//用于表示当前状态中各数字排列的压缩码
    int disToGoal;//初始化后用于表示当前状态与最终状态的距离，
    int dis;// 调用set函数后表示从初始状态经过此状态到最终状态所需步数
    Limbo former;
//    Limbo latter;

    private Limbo(long list) {
        this.list = list;
        disToGoal = calDis();
        dis = disToGoal;
        former = null;
//        latter = null;
    }

    public Limbo(int... num) {
        this(calList(num));
    }

    void setFormer(Limbo formerOne) {
        former = formerOne;
        dis = formerOne.dis + 1 + disToGoal;
//        System.out.println(this);
    }

    private static void checkValid(int[] num) {
        //检查参数合法性
        ArrayList<Integer> checkValid = new ArrayList<>(9);
        for (int curNum : num) {
            checkValid.add(curNum);
        }
        checkValid.sort(null);

        if ((new HashSet<Integer>(checkValid)).size() != 9 ||
                checkValid.get(0) != 0 || checkValid.get(8) != 8) {
            System.out.println("参数内容不合格");
            System.exit(0);
        }
        //end of 检查参数合法性
    }

    /**
     * 根据给出的一串数字计算列表的值
     * @param num
     * @return
     */
    private static long calList(int[] num) {
        checkValid(num);//检查参数合法性
        //构造 list
        long result = 0;
        long locOf0 = -1;
        for (int i = 0; i < 9; i++) {
            result <<= 4;
            result += num[i];
            if (num[i] == 0) {
                locOf0 = i;
            }
        }

        locOf0 = (8 - locOf0);
        result |= locOf0 << 60;
        System.out.printf("%x\n", result);
        return result;
    }

    /**
     * 检测 0 是否可以向上下左右四个方向移动，如果可以，就生成相邻状态的对象
     * @return
     */
    public ArrayList<Limbo> getAdjacent() {
        int zeroLoc = getZeroLoc();
        ArrayList<Limbo> adjacent = new ArrayList<>(4);

        if (zeroLoc + 3 <= 8) {
            adjacent.add(getAdjacentOf(zeroLoc, 3));
        }
        if (zeroLoc - 3 >= 0) {
            adjacent.add(getAdjacentOf(zeroLoc, -3));
        }
        if ((zeroLoc + 1) % 3 > zeroLoc % 3) {
            adjacent.add(getAdjacentOf(zeroLoc, 1));
        }
        if ((zeroLoc + 2) % 3 < zeroLoc % 3) {
            adjacent.add(getAdjacentOf(zeroLoc, -1));
        }

        return adjacent;
    }

    public int getReverse() {
        long tmpList = list & bit011;
        int num[] = new int[8];
        for (int i = 7; i >= 0; ) {
            int curInt = (int) (tmpList & bit010[0]);
            tmpList >>>= 4;
//            System.out.println(curInt);
            if (curInt != 0) {
                num[i] = curInt;
                i--;
            }
        }

        int count = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = i-1; j >= 0; j--) {
                if (num[j] > num[i]) {
                    count++;
                }
            }
        }

        System.out.println(Arrays.toString(num)+" "+count);
        return count;
    }

    private int getZeroLoc() {
        int zeroLoc = (int) (list >>> 60);
        return zeroLoc;
    }

    private Limbo getAdjacentOf(int zeroLoc, int offset) {
        int numLoc = zeroLoc + offset;
//        System.out.println(numLoc);
        long adjacent = list & bit010[numLoc];//得到要交换的位置的值

        //把要交换的值移动到原来 0 的位置
        if (offset > 0) {
            adjacent >>>= (offset * 4);
        }
        if (offset < 0) {
            adjacent <<= (-offset * 4);
        }

        adjacent |= list;//设置不需要交换的位置的值
        adjacent &= bit011;//设置高 8 位为 0
        adjacent &= bit101[numLoc];//设置要交换的位置为 0
        adjacent |= ((long) numLoc << 60);//设置高八位为交换后 0 的位置
//        System.out.printf("%x\n", adjacent);
        Limbo toReturn = new Limbo(adjacent);
//        System.out.println(toReturn);
        return toReturn;
    }

    /**
     * 首先用一个变量存储 list 与 dest 异或的结果
     * 再把这个变量的高八位设置为 0 (与 bit011 相与获得)
     * 再依次查询各个数字对应位置是否全部相同
     * @return
     */
    private int calDis() {
        long tmpResult = list ^ dest;
        tmpResult &= bit011;
        int count = 0;
        for (int i = 0; i < 9; i++) {
            long isEqualFlag = (tmpResult & bit010[0]);
            if (isEqualFlag > 0) {
                count++;
            }
            tmpResult >>= 4;
        }
        return count;
    }

    public int getDisToGoal() {
        return disToGoal;
    }

    @Override
    public int compareTo(Object o) {
        Limbo ol = (Limbo) o;
        if (ol.dis < dis) {
            return 1;
        }
        if (ol.dis > dis) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dis: "+dis+"\n");
        long tmpList = list;
        int value;
        for (int i = 0; i < 9; i++) {
            value = (int) (tmpList & bit010[0]);
            stringBuilder.append(value);
            if (i % 3 == 2) {
                stringBuilder.append('\n');
            } else {
                stringBuilder.append(' ');
            }
            tmpList >>>= 4;
        }

        return stringBuilder.toString();
    }

    public boolean equals(Object o) {
        Limbo oLimbo = (Limbo) o;
        if (oLimbo.list == list) {
            return true;
        }
        return false;
    }

}
