package engine.figures;

import engine.Figure;

import java.util.Random;

public class FigureFour extends Figure {
    public FigureFour() {
        super();
        boolean[][][] vars = new boolean[4][3][3];
        vars[0] = new boolean[][]{  {false, true, false},
                                    {false, true, false},
                                    {true, true, true}};

        vars[1] = new boolean[][]{  {true, true, true},
                                    {false, true, false},
                                    {false, true, false}};

        vars[2] = new boolean[][]{  {true, false, false},
                                    {true, true, true},
                                    {true, false, false}};

        vars[3] = new boolean[][]{  {false, false, true},
                                    {true, true, true},
                                    {false, false, true}};

        presentation = vars[new Random().nextInt(0, 4)];
    }
}
