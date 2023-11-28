package esiea.metier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoitureTest {

    @Test
    public void whenVoitureIsValid_thenCheckReturnsTrue() {

        Voiture voiture = new Voiture();
        voiture.setMarque("Renault");
        voiture.setModele("Clio");
        voiture.setFinition("Zen");
        voiture.setCarburant(Voiture.Carburant.ESSENCE);
        voiture.setKm(50000);
        voiture.setAnnee(2018);
        voiture.setPrix(10000);

        boolean isValid = voiture.check();

        assertTrue(isValid, "La voiture doit être considérée comme valide.");
    }

    @Test
    public void whenVoitureIsInvalid_thenCheckReturnsFalse() {

        Voiture voiture = new Voiture();
        voiture.setMarque("");
        voiture.setModele("");
        voiture.setKm(-1);

        boolean isValid = voiture.check();

        assertFalse(isValid, "La voiture doit être considérée comme invalide.");
    }

    @Test
    public void testGetTypeDonneeWithStringAttribute() {
        String attribute = "marque";
        String expectedType = "string";
        String actualType = Voiture.getTypeDonnee(attribute);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetTypeDonneeWithIntegerAttribute() {
        String attribute = "id";
        String expectedType = "entier";
        String actualType = Voiture.getTypeDonnee(attribute);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetTypeDonneeWithNullAttribute() {
        String attribute = null;
        String expectedType = "";
        String actualType = Voiture.getTypeDonnee(attribute);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetTypeDonneeWithInvalidAttribute() {
        String attribute = "color";
        String expectedType = "";
        String actualType = Voiture.getTypeDonnee(attribute);
        assertEquals(expectedType, actualType);
    }
}
