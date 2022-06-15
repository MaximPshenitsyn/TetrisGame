package test_package;

import com.example.pshenitsyn_maxim_203_hw5_tetris.HelloController;
import com.example.pshenitsyn_maxim_203_hw5_tetris.RequestParser;
import com.example.pshenitsyn_maxim_203_hw5_tetris.Server;
import engine.Engine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    @Test
    public void testField() {
        Engine e = new Engine();
        assertEquals(e.getField().length, 9);
    }

    @Test
    public void testSettle() {
        Engine e = new Engine();
        assertTrue(e.trySettleFigure(Engine.generateFigure(), 0, 0));
    }

    @Test
    public void testSettle2() {
        Engine e = new Engine();
        assertFalse(e.trySettleFigure(Engine.generateFigure(), -3, -3));
    }

    @Test
    public void testServer() {
        assertNull(Server.p1);
    }

    @Test
    public void validRequest() {
        String rString = RequestParser.convertToRequestMessage(RequestParser.RequestTypes.FINISH_GAME, "name");
        try {
            RequestParser r = RequestParser.parseRequest(rString);
            assertEquals("name", r.getName());
            assertEquals(r.getRequestType(), RequestParser.RequestTypes.FINISH_GAME);
        } catch (Exception ex) {
            assertFalse(ex.getMessage().isEmpty());
        }
    }

    @Test
    public void invalidRequest1() {
        String rString = RequestParser.convertToRequestMessage(RequestParser.RequestTypes.FINISH_GAME, "name");
        try {
            RequestParser r = RequestParser.parseRequest(rString);
            assertNotEquals("not name", r.getName());
            assertNotEquals(r.getRequestType(), RequestParser.RequestTypes.STAY);
        } catch (Exception ex) {
            assertFalse(ex.getMessage().isEmpty());
        }
    }

    @Test
    public void invalidRequest2() {
        String rString = RequestParser.convertToRequestMessage(RequestParser.RequestTypes.FINISH_GAME, " ;name");
        try {
            RequestParser r = RequestParser.parseRequest(rString);
        } catch (Exception ex) {
            assertTrue(ex.getMessage().isEmpty());
        }
    }

    @Test
    public void correctName() {
        HelloController helloController = new HelloController();
        assertFalse(helloController.incorrectName("abc"));
        assertTrue(helloController.incorrectName(""));
        assertTrue(helloController.incorrectName("ab;c"));
        assertTrue(helloController.incorrectName("abc;"));
    }
}
