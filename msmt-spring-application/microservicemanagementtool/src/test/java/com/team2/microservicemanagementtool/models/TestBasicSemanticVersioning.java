package com.team2.microservicemanagementtool.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestBasicSemanticVersioning {

    static BasicSemanticVersioning v1 = new BasicSemanticVersioning(1,0,0);
    static BasicSemanticVersioning v2 = new BasicSemanticVersioning(2,0,1);
    static BasicSemanticVersioning v3 = new BasicSemanticVersioning(1,0,0);


    @Test
    void testGetMajor() {
        assertEquals(1, v1.getMajor());
        assertEquals(2, v2.getMajor());
        assertEquals(1, v3.getMajor());
    }

    @Test
    void testSetMajor() {
        v1.setMajor(3);
        assertEquals(3, v1.getMajor());
    }

    @Test
    void testGetMinor() {
        assertEquals(0, v1.getMinor());
        assertEquals(0, v2.getMinor());
        assertEquals(0, v3.getMinor());
    }

    @Test
    void testSetMinor() {
        v1.setMinor(4);
        assertEquals(4, v1.getMinor());
    }

    @Test
    void testGetPatch() {
        assertEquals(0, v1.getPatch());
        assertEquals(1, v2.getPatch());
        assertEquals(0, v3.getPatch());
    }

    @Test
    void testSetPatch() {
        v1.setPatch(3);
        assertEquals(3, v1.getPatch());
    }

    @Test
    void testToString() {
        assertEquals("1.0.0", v1.toString());
        assertEquals("2.0.1", v2.toString());
        assertEquals("1.0.0", v3.toString());
    }

    @Test
    void testEqualsBasicSemanticVersioning() {
        assertEquals(v1, v3);
        assertNotEquals(v1, new BasicSemanticVersioning(11,0,0));
        assertNotEquals(v1, new BasicSemanticVersioning(1,1,0));
        assertNotEquals(v1, new BasicSemanticVersioning(1,0,1));
        assertNotEquals(v1,v2);
    }

    @Test
    void testEqualsNullFails() {
        assertFalse(v1.equals(null));
        assertFalse(v2.equals(null));
    }

    @Test
    void testEqualsNotInstance() {
        assertFalse(v1.equals("string"));
        assertFalse(v2.equals("string"));
        assertFalse(v1.equals(new StringBuilder()));
    }

    @Test()
    void testNegativeIntegersFails() {
        assertThrows(IllegalArgumentException.class,() -> {new BasicSemanticVersioning(0,-1,0);});
        assertThrows(IllegalArgumentException.class,() -> {new BasicSemanticVersioning(-1,0,0);});
        assertThrows(IllegalArgumentException.class,() -> {new BasicSemanticVersioning(0,0,-1);});
    }

    @Test
    void testHashcode(){
        assertEquals(new BasicSemanticVersioning(1, 19, 7).hashCode(), new BasicSemanticVersioning(1,19, 7).hashCode());
        assertNotEquals(new BasicSemanticVersioning(1, 12, 9).hashCode(), new BasicSemanticVersioning(2,4, 6).hashCode());
    }
}
