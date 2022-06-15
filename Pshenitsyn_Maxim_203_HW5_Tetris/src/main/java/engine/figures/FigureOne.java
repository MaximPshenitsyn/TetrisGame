package engine.figures;

import engine.Figure;

import java.util.Random;

public class FigureOne extends Figure {
    public FigureOne() {
        super();
        boolean[][][] vars = new boolean[8][3][3];
        vars[0] = new boolean[][]{  {true, true, false},
                                    {true, false, false},
                                    {true, false, false}};

        vars[1] = new boolean[][]{  {true, false, false},
                                    {true, false, false},
                                    {true, true, false}};

        vars[2] = new boolean[][]{  {false, false, false},
                                    {true, false, false},
                                    {true, true, true}};

        vars[3] = new boolean[][]{  {false, false, false},
                                    {false, false, true},
                                    {true, true, true}};

        vars[4] = new boolean[][]{  {false, false, true},
                                    {false, false, true},
                                    {false, true, true}};

        vars[5] = new boolean[][]{  {false, true, false},
                                    {false, true, false},
                                    {false, true, true}};

        vars[6] = new boolean[][]{  {false, false, false},
                                    {true, true, true},
                                    {false, false, true}};

        vars[7] = new boolean[][]{  {false, false, false},
                                    {true, true, true},
                                    {true, false, false}};

        presentation = vars[new Random().nextInt(0, 8)];
    }
}
