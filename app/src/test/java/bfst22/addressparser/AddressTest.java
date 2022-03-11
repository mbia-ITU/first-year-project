/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package bfst22.addressparser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    @Test void itu() {
        var addr = Address.parse("Pærevangen 22, 2765 Smørum");
        assertEquals("Pærevangen", addr.street);
        assertEquals("22", addr.house);
        assertEquals("2765", addr.postcode);
        assertEquals("Smørum", addr.city);
    }
    @Test void ituComma() {
        var addr = Address.parse("Pærevangen    22 ,  , 2765     Smørum");
        assertEquals("Pærevangen", addr.street);
        assertEquals("22", addr.house);
        assertEquals("2765", addr.postcode);
        assertEquals("Smørum", addr.city);
    }
    @Test void ituSimple() {
        var addr = Address.parse("Pærevangen 22");
        assertEquals("Pærevangen", addr.street);
        assertEquals("22", addr.house);
    }
}
