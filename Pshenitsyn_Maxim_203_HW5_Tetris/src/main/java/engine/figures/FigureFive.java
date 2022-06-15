package engine.figures;

import engine.Figure;

import java.util.Random;

public class FigureFive extends Figure {
    public FigureFive() {
        super();
        boolean[][][] vars = new boolean[3][3][3];
        vars[0] = new boolean[][]{  {false, false, false},
                                    {true, true, true},
                                    {false, false, false}};

        vars[1] = new boolean[][]{  {false, true, false},
                                    {false, true, false},
                                    {false, true, false}};

        vars[2] = new boolean[][]{  {false, false, false},
                                    {false, true, false},
                                    {false, false, false}};

        presentation = vars[new Random().nextInt(0, 3)];
    }
}
