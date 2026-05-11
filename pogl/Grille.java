import java.util.*;

public class Grille {
    private Zone[][] zones;
    private int taille ;
    public static List<Zone.Artefact> artefactsRecuperes = new ArrayList<>();
    public Zone heliport;
    private ArrayList<Joueur> joueurs = new ArrayList<>();


    public Grille(int taille) {
        this.taille = taille;
        zones = new Zone[taille][taille];
        initZones();

        List<Zone> zonesLibres = new ArrayList<>();
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (zones[i][j] != null) {  // Ajoute seulement les zones actives
                    zonesLibres.add(zones[i][j]);
        }
    }
}
Collections.shuffle(zonesLibres);

zonesLibres.get(0).setArtefact(Zone.Artefact.AIR);
zonesLibres.get(1).setArtefact(Zone.Artefact.EAU);
zonesLibres.get(2).setArtefact(Zone.Artefact.FEU);
zonesLibres.get(3).setArtefact(Zone.Artefact.TERRE);




        // Placer l'héliport en bas à droite
        heliport = zones[3][3];
        heliport.setHeliport(true);

    }

    public int getTaille() {
        return taille;
    }

    public Zone getZone(int x, int y) {
        if (x >= 0 && x < taille && y >= 0 && y < taille) {
            return zones[x][y];
        }
        return null;
    }
    

    public void afficher() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                Zone z = zones[i][j];
                if (z == null) {
                    System.out.print("     "); // espace vide
                } else {
                    System.out.print(z + " ");
                }
            }
            System.out.println();
        }
    }
    

    public void inonder3Zones() {
        List<Zone> candidates = new ArrayList<>();
        for (Zone[] ligne : zones) {
            for (Zone z : ligne) {
                // Vérifier que z n'est pas null avant d'accéder à ses méthodes
                if (z != null && z.getEtat() != Zone.Etat.SUBMERGEE) {
                    candidates.add(z);
                }
            }
        }
    
        Collections.shuffle(candidates);
        for (int i = 0; i < Math.min(3, candidates.size()); i++) {
            candidates.get(i).inonde();
        }
    }

    public boolean artefactSubmergePerdu() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                Zone z = zones[i][j];
                // Vérifier que z n'est pas null avant d'accéder à ses méthodes
                if (z != null && z.getEtat() == Zone.Etat.SUBMERGEE &&
                    z.getArtefact() != Zone.Artefact.NONE &&
                    !z.estArtefactRecupere()) {
                    System.out.println("DEBUG Artefact " + z.getArtefact() + " submergé à (" + i + "," + j + ")");
                    return true;
                }
            }
        }
        return false;
    }
    //méthodes pour vérifier si tous les artefacts sont récupérés

    public static boolean tousArtefactsRecuperes() {
        return artefactsRecuperes.containsAll(Arrays.asList(
            Zone.Artefact.AIR,
            Zone.Artefact.EAU,
            Zone.Artefact.FEU,
            Zone.Artefact.TERRE
        ));
    }

    public void initZones() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                // On désactive les coins sauf (1,1), (1,5), (5,1), (5,5)
                boolean coinInactif = ((i <= 1 && j <= 1) || (i <= 1 && j >= taille - 2)
                                    || (i >= taille - 2 && j <= 1) || (i >= taille - 2 && j >= taille - 2))
                                    && !((i == 1 && j == 1) || (i == 1 && j == 5) || (i == 5 && j == 1) || (i == 5 && j == 5));
    
                if (coinInactif) {
                    zones[i][j] = null;
                } else {
                    zones[i][j] = new Zone(i, j);
                }
            }
        }
    }

    public void ajouterJoueur(Joueur j) {
        joueurs.add(j);
    }
    
    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }
    
    
    
}
