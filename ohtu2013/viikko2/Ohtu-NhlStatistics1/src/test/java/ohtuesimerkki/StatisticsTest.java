/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtuesimerkki;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class StatisticsTest {

    Statistics statistiikat;

    @Before
    public void setUp() {

        Reader reader = new TestReader();
        statistiikat = new Statistics(reader);
    }

    private static class TestReader implements Reader {

        @Override
        public List<Player> getPlayers() {
            ArrayList<Player> players = new ArrayList<Player>();

            players.add(new Player("Semenko", "EDM", 4, 12));
            players.add(new Player("Lemieux", "PIT", 45, 54));
            players.add(new Player("Kurri", "EDM", 37, 53));
            players.add(new Player("Yzerman", "DET", 42, 56));
            players.add(new Player("Gretzky", "EDM", 35, 89));

            return players;
        }
    }

    @Test
    public void etsiminenPalauttaaOikeanPelaajanNimen() {
        assertEquals("Semenko", statistiikat.search("Semenko").getName());
    }

    @Test
    public void etsiminenPalauttaaOikeanPelaajanJoukkueen() {
        assertEquals("PIT", statistiikat.search("Lemieux").getTeam());
    }

    @Test
    public void josPelaajaaEiLoydyPalautetaanNull() {
        assertEquals(null, statistiikat.search("Karikoski"));
    }

    @Test
    public void pelaajatListautuuOkeisiinJoukkueisiin() {

        for (Player pelaaja : statistiikat.team("EDM")) {
            assertEquals("EDM", pelaaja.getTeam());
        }
    }

    @Test
    public void oikeaMaaraPelaajiaListautuuJoukkeisiin() {
        assertEquals(3, statistiikat.team("EDM").size());
        assertEquals(1, statistiikat.team("DET").size());
    }

    @Test
    public void topScorersNegatiivinenSyotePalauttaaTyhjanListan() {
        assertEquals(0, statistiikat.topScorers(-5).size());
    }

    @Test
    public void topScorersPalauttaaOikeanMaaranPelaajia() {
        assertEquals(2, statistiikat.topScorers(2).size());
        assertEquals(4, statistiikat.topScorers(4).size());
    }
}
