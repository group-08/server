package ch.uzh.ifi.seal.soprafs20.entity;

import static org.junit.jupiter.api.Assertions.*;
import ch.uzh.ifi.seal.soprafs20.field.CasualField;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import org.junit.jupiter.api.Test;

public class FigureTest {
    @Test
    public void TestCorrectFigureCreation() {
        Field testField = new CasualField();
        Figure testFigure = new Figure(testField);
        assertEquals(testField, testFigure.getField());
    }
}
