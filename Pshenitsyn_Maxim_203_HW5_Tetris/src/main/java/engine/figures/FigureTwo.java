package engine.figures;

import engine.Figure;

import java.util.Random;

public class FigureTwo extends Figure {
    public FigureTwo() {
        super();
        boolean[][][] vars = new boolean[4][3][3];
        vars[0] = new boolean[][]{  {true, false, false},
                                    {true, true, false},
                                    {false, true, false}};

        vars[1] = new boolean[][]{  {false, false, false},
                                    {false, true, true},
                                    {true, true, false}};

        vars[2] = new boolean[][]{  {false, true, false},
                                    {true, true, false},
                                    {true, false, false}};

        vars[3] = new boolean[][]{  {false, false, false},
                                    {true, true, false},
                                    {false, true, true}};

        presentation = vars[new Random().nextInt(0, 4)];
    }
}
