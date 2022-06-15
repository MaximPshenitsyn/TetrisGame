package engine.figures;

import engine.Figure;

import java.util.Random;

public class FigureSix extends Figure {
    public FigureSix () {
        super();
        boolean[][][] vars = new boolean[4][3][3];
        vars[0] = new boolean[][]{  {true, true, false},
                                    {true, false, false},
                                    {false, false, false}};

        vars[1] = new boolean[][]{  {false, true, true},
                                    {false, false, true},
                                    {false, false, false}};

        vars[2] = new boolean[][]{  {false, false, false},
                                    {false, false, true},
                                    {false, true, true}};

        vars[3] = new boolean[][]{  {false, false, false},
                                    {true, false, false},
                                    {true, true, false}};

        presentation = vars[new Random().nextInt(0, 4)];
    }
}
