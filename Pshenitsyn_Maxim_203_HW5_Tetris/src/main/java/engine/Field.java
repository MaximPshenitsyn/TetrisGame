package engine;

import java.util.Arrays;

public class Field {
    protected boolean[][] field;

    protected Field() {
        field = new boolean[9][9];
        for (boolean[] booleans : field) {
            Arrays.fill(booleans, false);
        }
    }

    protected boolean isInField(int x, int y) {
        return (0 <= x && x < field.length) && (0 <= y && y < field.length);
    }

    protected boolean isFree(int x, int y) {
        if (!isInField(x, y)) {
            return false;
        }
        return !field[x][y];
    }

    protected boolean isFreeOrOutOfField(int x, int y) {
        return isFree(x, y) || !isInField(x, y);
    }

    protected boolean canSettle(Figure f, int x, int y) {
        for (int i = 0; i < f.presentation.length; ++i) {
            for (int j = 0; j < f.presentation[i].length; ++j) {
                if (f.presentation[i][j]) {
                    if (!isFree(i + x, j + y)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void settleFigure(Figure f, int x, int y) {
        if (!canSettle(f, x, y)) {
            return;
        }
        for (int i = 0; i < f.presentation.length; ++i) {
            for (int j = 0; j < f.presentation[i].length; ++j) {
                if (f.presentation[i][j]) {
                    field[i + x][j + y] = true;
                }
            }
        }
    }
}
