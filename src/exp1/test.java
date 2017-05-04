package exp1;

/**
 * Created by deded on 2017/4/12.
 */
public class test {
    public static void main(String[] args) {
        test1();
    }

    static void test1() {
        EightPuzzle.setGoal(5,6,7,4,0,8,3,2,1);
//        EightPuzzle.setInitialState(5,0,7,4,6,1,3,8,2);
//        EightPuzzle.setInitialState(4,0,8,5,6,7,3,2,1);
        EightPuzzle.setInitialState(1,2,3,4,5,6,7,0,8);
//        EightPuzzle.goA();
        EightPuzzle.goA();
//        Limbo aaa = new Limbo(5,6,7,4,0,8,3,2,1);
//        Limbo aaa = new Limbo(0,1,2,3,4,5,6,7,8);
//        Limbo aaa = new Limbo(8,7,6,5,4,3,2,1,0);
//        aaa.getReverse();
    }
}
