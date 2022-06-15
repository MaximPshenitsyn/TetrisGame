package engine;

import engine.figures.*;

import java.util.Random;

public class Engine {
    private static class Generator {
        public static Figure generateFigure() {
            Figure[] vars = new Figure[] {new FigureOne(), new FigureTwo(), new FigureThree(), new FigureFour(),
                                            new FigureFive(), new FigureSix(), new FigureSeven(), new FigureSeven()} ;
            return vars[new Random().nextInt(0, vars.length)];
        }
    }

    private final Field field;

    public Engine() {
        field = new Field();
    }

    public static Figure generateFigure() {
        return Generator.generateFigure();
    }

    public boolean trySettleFigure(Figure f, int x, int y) {
        if (!field.canSettle(f, x, y)) {
            return false;
        }
        field.settleFigure(f, x, y);
        return true;
    }

    public boolean[][] getField() {
        return field.field.clone();
    }
}
