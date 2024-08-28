package com.team2.microservicemanagementtool.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestLeadEngineer {
    static LeadEngineer bob = new LeadEngineer("bob","b@bmail.ie");

    @BeforeEach
    void init()
    {
        bob = new LeadEngineer("bob","b@bmail.ie");
    }

    @Test
    void testEquals() {
        LeadEngineer le = new LeadEngineer("bob","b@bmail.ie");

        assertEquals(le,le);
        assertEquals(bob,le);
        le.setName("bob1");
        assertNotEquals(bob,le);
        le = new LeadEngineer("bob","b@bmail.issssssssssssssssse");
        assertNotEquals(bob,le);
    }

    @Test
    void testEqualsNullFails() {
        assertFalse(bob.equals(null));
    }

    @Test
    void testEqualsNotInstance() {
        assertFalse(bob.equals("string"));
        assertFalse(bob.equals(new StringBuilder()));
    }

    @Test
    void testHashcode() {
        assertEquals(new LeadEngineer("bob1","b@bmail.ie").hashCode(),new LeadEngineer("bob1","b@bmail.ie").hashCode());
        assertNotEquals(new LeadEngineer("bob2","b@bmail.ie").hashCode(),new LeadEngineer("bob1","b@bmail.ie").hashCode());
    }
}
