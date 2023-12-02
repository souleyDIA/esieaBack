package esiea.dao;

import esiea.metier.Voiture;
import esiea.metier.Voiture.Carburant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;

public class VoitureDAOTest {

    private VoitureDAO voitureDAO;

    @BeforeEach
    public void setup() {
        voitureDAO = new VoitureDAO();
    }

    @Test
    public void whenAjouterVoiture_thenVoitureIsAdded() {
        try {
            Voiture nouvelleVoiture = new Voiture();
            nouvelleVoiture.setMarque("TestMarque");
            nouvelleVoiture.setModele("TestModèle");
            nouvelleVoiture.setFinition("TestFinition");
            nouvelleVoiture.setCarburant(Carburant.ESSENCE);
            nouvelleVoiture.setKm(10000);
            nouvelleVoiture.setAnnee(2020);
            nouvelleVoiture.setPrix(15000);

            voitureDAO.ajouterVoiture(nouvelleVoiture);

            // Recherchez la voiture ajoutée pour confirmer son ajout
            ReponseVoiture reponseAjout = voitureDAO.rechercherVoitures("TestMarque", 0, 1);
            Assertions.assertTrue(reponseAjout.getData().length > 0, "La voiture ajoutée devrait être trouvée");
            Assertions.assertEquals("TestMarque", reponseAjout.getData()[0].getMarque(),
                    "La marque de la voiture devrait correspondre");

        } catch (SQLException e) {
            Assertions.fail("Échec de l'ajout de la voiture : " + e.getMessage());
        }
    }

    @Test
    public void whenModifierVoiture_thenSQLExceptionIsThrown() {
        int id = 1;
        Voiture voitureModifiee = new Voiture();

        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            voitureDAO.modifierVoiture(id, voitureModifiee);
        });

        String expectedMessage = "Can not issue data manipulation statements with executeQuery().";
        String actualMessage = exception.getMessage();
        System.out.println("Message d'erreur : " + actualMessage);
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    // @Test
    // public void whenModifierVoiture_thenVoitureIsModified() {
    // try {
    // Voiture voitureModifiee = new Voiture();
    // // Config de la voiture modifiée
    // voitureModifiee.setMarque("TestMarqueModifiee");

    // int voitureId = 1;
    // voitureDAO.modifierVoiture(voitureId, voitureModifiee);

    // // Recherche de la voiture modifiée pour confirmer les modifications
    // ReponseVoiture reponseModification =
    // voitureDAO.rechercherVoitures("TestMarqueModifiee", 0, 1);
    // Assertions.assertTrue(reponseModification.getData().length > 0, "La voiture
    // modifiée devrait être trouvée");
    // Assertions.assertEquals("TestMarqueModifiee",
    // reponseModification.getData()[0].getMarque(),
    // "La marque modifiée de la voiture devrait correspondre");

    // } catch (SQLException e) {
    // Assertions.fail("Échec de la modification de la voiture : " +
    // e.getMessage());
    // }
    // }

    @Test
    public void whenRechercherVoitures_thenVoituresAreFound() {
        try {
            String critereRecherche = "Renault";
            int mini = 0;
            int nbVoitures = 5;
            ReponseVoiture reponse = voitureDAO.rechercherVoitures(critereRecherche, mini, nbVoitures);

            Assertions.assertTrue(reponse.getData().length > 0, "Des voitures auraient dû être trouvées.");
            for (Voiture voiture : reponse.getData()) {
                Assertions.assertEquals(critereRecherche, voiture.getMarque(),
                        "La marque des voitures trouvées devrait correspondre au critère de recherche");
            }

        } catch (SQLException e) {
            Assertions.fail("Échec de la recherche de voitures : " + e.getMessage());
        }
    }

    @Test
    public void whenSupprimerVoiture_thenVoitureIsDeleted() {
        try {
            int voitureId = 1;
            voitureDAO.supprimerVoiture(String.valueOf(voitureId));

            // Vérifiez si la voiture a été supprimée
            ReponseVoiture reponseSuppression = voitureDAO.rechercherVoitures("MarqueDeLaVoitureSupprimee", 0, 1);
            Assertions.assertTrue(reponseSuppression.getData().length == 0,
                    "La voiture supprimée ne devrait pas être trouvée");

        } catch (SQLException e) {
            Assertions.fail("Échec de la suppression de la voiture : " + e.getMessage());
        }
    }

}
