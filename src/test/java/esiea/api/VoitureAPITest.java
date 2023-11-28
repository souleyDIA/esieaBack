package esiea.api;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import esiea.dao.ReponseVoiture;
import esiea.dao.VoitureDAO;
import esiea.metier.Voiture;
import esiea.metier.Voiture.Carburant;

public class VoitureAPITest {

    @InjectMocks
    private VoitureAPI voitureAPI;

    @Mock
    private VoitureDAO voitureDAO;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(voitureDAO).ajouterVoiture(any(Voiture.class));
    }

    @Test
    public void whenVoitureFromJsonCalled_thenReturnCorrectVoitureObject() throws JSONException {

        JSONObject json = new JSONObject()
                .put("id", 1)
                .put("marque", "Peugeot")
                .put("modele", "308")
                .put("finition", "Allure")
                .put("carburant", "D")
                .put("km", 123000)
                .put("annee", 2015)
                .put("prix", 9500);

        Voiture voiture = voitureAPI.voitureFromJson(json);

        System.out.println("Voiture api: " + voiture);

        // Quelques assertions pour vérifier que la voiture est correctement créée
        assertNotNull(voiture, "L'objet Voiture ne devrait pas être null");
        assertEquals(1, voiture.getId());
        assertEquals("Peugeot", voiture.getMarque());
        assertEquals("308", voiture.getModele());
        assertEquals("Allure", voiture.getFinition());
        assertEquals(Voiture.Carburant.DIESEL, voiture.getCarburant());
        assertEquals(123000, voiture.getKm());
        assertEquals(2015, voiture.getAnnee());
        assertEquals(9500, voiture.getPrix());
    }

    @Test
    public void testGetVoituresJson_AllParam() throws SQLException, JSONException {

        String param = "all";
        String mini = "-1";
        String nbVoitures = "-1";

        Voiture voiture1 = new Voiture();
        voiture1.setId(1);
        voiture1.setMarque("Renault");
        voiture1.setModele("VelSatis");
        voiture1.setFinition("Initiale");
        voiture1.setCarburant(Carburant.DIESEL);
        voiture1.setKm(174826);
        voiture1.setAnnee(2008);
        voiture1.setPrix(4600);

        Voiture voiture2 = new Voiture();
        voiture2.setId(2);
        voiture2.setMarque("Renault");
        voiture2.setModele("Scénic");
        voiture2.setFinition("Business");
        voiture2.setCarburant(Carburant.DIESEL);
        voiture2.setKm(124987);
        voiture2.setAnnee(2013);
        voiture2.setPrix(8800);

        ReponseVoiture reponseVoiture = new ReponseVoiture();
        reponseVoiture.setData(new Voiture[] { voiture1, voiture2 });
        reponseVoiture.setVolume(2);
        when(voitureDAO.getVoitures(null, -1, -1)).thenReturn(reponseVoiture);

        String result = voitureAPI.getVoituresJson(param, mini, nbVoitures);

        JSONObject jsonResult = new JSONObject(result);
        JSONArray voituresArray = jsonResult.getJSONArray("voitures");
        assertEquals(2, voituresArray.length());

        JSONObject voiture1Json = new JSONObject(voituresArray.getString(0));
        assertEquals(1, voiture1Json.getInt("id"));
        assertEquals("Renault", voiture1Json.getString("marque"));
        assertEquals("VelSatis", voiture1Json.getString("modele"));
        assertEquals("Initiale", voiture1Json.getString("finition"));
        assertEquals("DIESEL", voiture1Json.getString("carburant"));
        assertEquals(174826, voiture1Json.getInt("km"));
        assertEquals(2008, voiture1Json.getInt("annee"));
        assertEquals(4600, voiture1Json.getInt("prix"));

        JSONObject voiture2Json = new JSONObject(voituresArray.getString(1));
        assertEquals(2, voiture2Json.getInt("id"));
        assertEquals("Renault", voiture2Json.getString("marque"));
        assertEquals("Scénic", voiture2Json.getString("modele"));
        assertEquals("Business", voiture2Json.getString("finition"));
        assertEquals("DIESEL", voiture2Json.getString("carburant"));
        assertEquals(124987, voiture2Json.getInt("km"));
        assertEquals(2013, voiture2Json.getInt("annee"));
        assertEquals(8800, voiture2Json.getInt("prix"));
    }

    @Test
    public void testAjouterVoiture_Success() throws SQLException, JSONException {
        String saisieJson = "{\"id\": 1, \"marque\": \"Renault\", \"modele\": \"VelSatis\", \"finition\": \"Initiale\", \"carburant\": \"D\", \"km\": 174826, \"annee\": 2008, \"prix\": 4600}";

        String result = voitureAPI.ajouterVoiture(saisieJson);

        System.out.println("Réponse JSON: " + saisieJson);

        JSONObject jsonResult = new JSONObject(result);
        assertTrue(jsonResult.getBoolean("succes"));

        // Capture et vérification des arguments passés
        ArgumentCaptor<Voiture> voitureCaptor = ArgumentCaptor.forClass(Voiture.class);
        verify(voitureDAO).ajouterVoiture(voitureCaptor.capture());
        Voiture capturedVoiture = voitureCaptor.getValue();

        assertTrue(capturedVoiture.check());
    }

    @Test
    public void testSupprimerVoiture_Success() throws SQLException, JSONException {

        String id = "1";

        String result = voitureAPI.supprimerVoiture(id);

        JSONObject jsonResult = new JSONObject(result);
        assertEquals(true, jsonResult.getBoolean("succes"));
        verify(voitureDAO).supprimerVoiture(id);
    }
}