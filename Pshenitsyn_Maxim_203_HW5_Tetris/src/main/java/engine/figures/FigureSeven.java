package engine.figures;

import engine.Figure;

import java.util.Random;

public class FigureSeven extends Figure {
    public FigureSeven () {
        super();
        boolean[][][] vars = new boolean[4][3][3];
        vars[0] = new boolean[][]{  {true, false, false},
                                    {true, true, false},
                                    {true, false, false}};

        vars[1] = new boolean[][]{  {true, true, true},
                                    {false, true, false},
                                    {false, false, false}};

        vars[2] = new boolean[][]{  {false, false, true},
                                    {false, true, true},
                                    {false, false, true}};

        vars[3] = new boolean[][]{  {false, false, false},
                                    {false, true, false},
                                    {true, true, true}};

        presentation = vars[new Random().nextInt(0, 4)];
    }
}
