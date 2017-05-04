package exp2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by deded on 2017/4/24.
 */
public class Maze {

    Random random = new Random(System.currentTimeMillis());

    int h;//高
    int w;//宽

    static int startXValue;
    static int startYValue;
    static int destXValue;
    static int destYValue;

    static Point startPoint;
    static Point destPoint;

    Point[][] points;
    ArrayList<Point>[][] maze;


//    ArrayList<ArrayList<Point>> maze;


    public Maze(int h, int w) {
        this.h = h;
        this.w = w;
        maze = new ArrayList[w][h];
        points  = new Point[w][h];

//        fillMaze();
    }

    public void init(int startX, int startY, int destX, int destY) {
        setStart(startX, startY);
        setDest(destX, destY);
        fillMaze();
        startPoint = points[startXValue][startYValue];
        destPoint = points[destXValue][destYValue];
    }

    private void setStart(int xValue, int yValue) {
        checkValid(xValue, yValue);
        startXValue = xValue;
        startYValue = yValue;
    }

    private void setDest(int xValue, int yValue) {
        checkValid(xValue, yValue);
        destXValue = xValue;
        destYValue = yValue;
    }

    void checkValid(int xValue, int yValue) {
        if (!isOnMap(xValue, yValue)) {
            System.out.println("wrong parameters!");
            System.exit(1);
        }
    }

    void fillMaze() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                points[i][j] = new Point(i, j);
                maze[i][j] = new ArrayList<>();
            }
        }

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                if (isAddable(i+1, j)) {
                    maze[i][j].add(points[i+1][j]);
                    maze[i+1][j].add(points[i][j]);
                }
                if (isAddable(i, j+1)) {
                    maze[i][j].add(points[i][j+1]);
                    maze[i][j+1].add(points[i][j]);
                }
            }
        }

    }

    boolean isAddable(int xValue, int yValue) {
        return isOnMap(xValue, yValue) && random.nextInt(100) > 35;
    }

    boolean isOnMap(int xValue, int yValue) {
        return xValue >= 0 && xValue < w && yValue >= 0 && yValue < h;
    }

    public void solve() {

        ArrayList<Point> openList = new ArrayList<>();
        ArrayList<Point> closeList = new ArrayList<>();

        openList.add(startPoint);
        while (!openList.isEmpty()) {
//            System.out.println(openList);
            Point first = openList.get(0);
            if (first.h == 0) {
                System.out.println("success");
                printPath(first);
                System.out.println("\b");
                return;
            }
            openList.remove(0);
            closeList.add(first);
            for (Point adPoint : maze[first.x][first.y]) {
                if (openList.contains(adPoint)) {
                    if (adPoint.f > adPoint.h + first.g + 1) {
                        adPoint.setFormer(first);
                    }
                } else if (closeList.contains(adPoint)) {
                    if (adPoint.f > adPoint.h + first.g + 1) {
                        adPoint.setFormer(first);
                        closeList.remove(adPoint);
                        openList.add(adPoint);
                    }
                } else {
                    adPoint.setFormer(first);
                    openList.add(adPoint);
                }
            }
            openList.sort(null);
        }
        System.out.println("fail");
        return;
    }

    void printPath(Point last) {
        if (last.former != null) {
            printPath(last.former);
        }
        System.out.print(last+"-");
    }

    public String toString() {
        if (points[0][0] == null) {
            return "please call init first";
        }

        StringBuilder stringBuilder = new StringBuilder("maze ");
        stringBuilder.append("start at:"+startPoint+", dest is"+destPoint+"\n");
        char[] connector = new char[w];
        for (int j = 0; j < h; j++) {
            Arrays.fill(connector, ' ');
            stringBuilder.append(String.format("%3s", " "));
            for (int i = 0; i < w; i++) {
                stringBuilder.append(points[i][j]);
                char toAppend = isOnMap(i+1, j) && maze[i][j].contains(points[i+1][j])
                        ? '-' : ' ';
                stringBuilder.append(toAppend);
                connector[i] = isOnMap(i, j+1) && maze[i][j].contains(points[i][j+1])
                        ? '|' : ' ';
            }
            stringBuilder.append('\n');
            for (int i = 0; i < w; i++) {
                stringBuilder.append(String.format("%5s%c"," ",connector[i]));
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    private String toString2() {
        StringBuilder stringBuilder = new StringBuilder("maze:\n");
        for (int i = 0; i < w; i++) {
            stringBuilder.append("line"+i+":\n");
            for (int j = 0; j < h; j++) {
                stringBuilder.append("j:"+points[i][j]+" connect to:");
                for (Point point : maze[i][j]) {
                    stringBuilder.append(point+" ");
                }


                stringBuilder.append(";  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    class Point implements Comparable {
        int x;
        int y;

        int g = 0;
        int h;
        int f;

        Point former;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
            h = Math.abs(x-destXValue) + Math.abs(y-destYValue);
        }

        void setFormer(Point former) {
            g = former.g + 1;
            f = h + g;
            this.former = former;
        }

        public boolean equals(Object o) {
            Point oPoint = (Point) o;
            return x == oPoint.x && y == oPoint.y;
        }

        public String toString() {
            return "("+x+","+y+")";
        }

        @Override
        public int compareTo(Object o) {
            Point oPoint = (Point) o;
            if (oPoint.f < f) {
                return 1;
            }
            if (oPoint.f > f) {
                return -1;
            }
            return 0;
        }
    }
}
