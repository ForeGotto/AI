package exp2;

/**
 * Created by deded on 2017/4/24.
 */
public class test {
    public static void main(String[] args) {
        int h = 10, w = 10;
        Maze maze = new Maze(h,w);
//        System.out.println(maze);
        maze.init(1,1,w-1,h-1);
        System.out.println(maze);

        maze.solve();
    }
}
