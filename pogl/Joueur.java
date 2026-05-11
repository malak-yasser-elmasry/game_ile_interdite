import java.util.*;

public class Joueur {
    private String nom;
    private Zone position;
    private List<Cle> cles;


    public Joueur(String nom, Zone depart) {
        this.nom = nom;
        this.position = depart;
        this.cles = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public Zone getPosition() {
        return position;
    }

    // ajoutée pour pouvoir tester mes différentes actions plus rapidement 
    public void setPosition(Zone z) {
        this.position = z;
    }
    
    

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void retirerCle(Cle c) {
        cles.remove(c);
    }

    public List<Cle> getCles() {
        return cles;
    }
    

    public void ajouterCle(Cle c) {
        cles.add(c);
    }

    public void afficherCles() {
        System.out.print("Clés : ");
        for (Cle c : cles) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private int compterCles(Cle cle) {
        int count = 0;
        for (Cle c : cles) {
            if (c == cle) count++;
        }
        return count;
    }

    public boolean deplacer(String direction, Grille g) {
        int x = position.getX();
        int y = position.getY();
        int nx = x, ny = y;

        direction = direction.trim().toLowerCase();
        switch (direction) {
            case "haut" -> nx--;
            case "bas" -> nx++;
            case "gauche" -> ny--;
            case "droite" -> ny++;
            default -> {
                System.out.println("Direction invalide.");
                return false;
            }
        }

        Zone nouvelle = g.getZone(nx, ny);
        if (nouvelle != null && nouvelle.estAccessible()) {
            position = nouvelle;
            return true;
        } else {
            System.out.println("Déplacement impossible.");
            return false;
        }
    }

    public boolean assecher(String direction, Grille g) {
        int x = position.getX();
        int y = position.getY();
        int tx = x, ty = y;

        direction = direction.trim().toLowerCase();
        switch (direction) {
            case "ici" -> {}
            case "haut" -> tx--;
            case "bas" -> tx++;
            case "gauche" -> ty--;
            case "droite" -> ty++;
            default -> {
                System.out.println("Direction invalide.");
                return false;
            }
        }

        Zone cible = g.getZone(tx, ty);
        if (cible != null && cible.getEtat() == Zone.Etat.INONDEE) {
            cible.assecher();
            return true;
        }
        return false;
    }

    public boolean recupererArtefact(Zone z) {
        Zone.Artefact a = z.getArtefact();
        if (a != Zone.Artefact.NONE) {
            long nb = cles.stream().filter(cle -> cle.name().equals(a.name())).count();
            if (nb >= 4) {
                // Supprimer les 4 clés
                int count = 0;
                Iterator<Cle> it = cles.iterator();
                while (it.hasNext() && count < 4) {
                    if (it.next().name().equals(a.name())) {
                        it.remove();
                        count++;
                    }
                }
    
                // Supprimer l’artefact de la grille !
                z.setArtefact(Zone.Artefact.NONE);
    
                // Notifier que l’artefact a été récupéré
                System.out.println("🎉 " + nom + " a récupéré l’artefact " + a.name() + " !");
                Grille.artefactsRecuperes.add(a); // ← si tu utilises un suivi global
                return true;
            } else {
                System.out.println("❌ Pas assez de clés !");
            }
        } else {
            System.out.println("❌ Aucun artefact ici !");
        }
        return false;
    }
    

    public boolean estPiege(Grille g) {
        if (position.getEtat() != Zone.Etat.SUBMERGEE) return false;

        int x = position.getX();
        int y = position.getY();

        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] d : directions) {
            Zone voisine = g.getZone(x + d[0], y + d[1]);
            if (voisine != null && voisine.estAccessible()) {
                return false;
            }
        }
        return true;
    }
}
