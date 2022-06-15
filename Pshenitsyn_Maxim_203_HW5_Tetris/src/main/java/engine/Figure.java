package engine;

public abstract class Figure {
    protected boolean[][] presentation;

    protected Figure() {
        presentation = new boolean[3][3];
    }

    public boolean getXY(int x, int y) {
        return presentation[x][y];
    }

    public void displayFigure() {
        for (var b : presentation) {
            for (boolean c : b) {
                if (c)
                    System.out.print(1 + " ");
                else
                    System.out.print(0 + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (var b : presentation) {
            for (boolean c : b) {
                if (c)
                    res.append("1");
                else
                    res.append("0");
            }
        }
        return res.toString();
    }

    public static Figure convertFromString(String query) {
        Figure f = new Figure() { };
        f.presentation = new boolean[3][3];
        for (int i = 0; i < query.length(); ++i) {
            f.presentation[i / 3][i % 3] = query.charAt(i) == '1';
        }
        return f;
    }
}
