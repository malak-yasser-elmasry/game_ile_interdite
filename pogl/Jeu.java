import java.util.*;

public class Jeu {
 public static void main(String[] args) {
        Grille g = new Grille(7);

        Scanner sc = new Scanner(System.in);

        ArrayList<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new Joueur("Maria", g.getZone(2, 2)));
        joueurs.add(new Joueur("Malak", g.getZone(3, 2)));

        g.ajouterJoueur(joueurs.get(0));
        g.ajouterJoueur(joueurs.get(1));

        FenetreJeu fenetre = new FenetreJeu(g);


        int tour = 0;

        while (true) {
            Joueur joueurCourant = joueurs.get(tour % joueurs.size());
            int actionsRestantes = 3;

            System.out.println("\n=== Tour de " + joueurCourant.getNom() + " ===");
            joueurCourant.afficherCles();

            while (actionsRestantes > 0) {
                g.afficher();
                System.out.println("Position actuelle : " + joueurCourant.getPosition().getX() + "," + joueurCourant.getPosition().getY());
                System.out.println("Actions restantes : " + actionsRestantes);
                System.out.print("Action ? (1 = Déplacer, 2 = Assécher, 3 = Rien, 4 = Récupérer artefact, 5 = S'envoler, 6 = Donner une clé) : ");
                String saisie = sc.nextLine().trim();
                int choix;
                try {
                    choix = Integer.parseInt(saisie);
                } catch (NumberFormatException e) {
                    System.out.println(" Tu dois entrer un nombre !");
                    continue;
                }

                if (choix == 1) {
                    System.out.print("Direction (haut/bas/gauche/droite) : ");
                    String dir = sc.nextLine().trim().toLowerCase();
                    if (joueurCourant.deplacer(dir, g)) actionsRestantes--;
                } 
                else if (choix == 2) {
                    System.out.print("Zone à assécher ? (ici/haut/bas/gauche/droite) : ");
                    String dir = sc.nextLine().trim().toLowerCase();
                    if (joueurCourant.assecher(dir, g)) {
                        actionsRestantes--;
                    } else {
                        System.out.println(" Assèchement impossible !");
                    }
                } 
                else if (choix == 3) {
                    System.out.println(" Tour terminé.");
                    break;
                } 
                else if (choix == 4) {
                    if (joueurCourant.recupererArtefact(joueurCourant.getPosition())) {
                        actionsRestantes--;
                    }
                } 
                else if (choix == 5) {
                    if (Grille.tousArtefactsRecuperes()) {
                        boolean tousSurHeliport = true;
                        for (Joueur j : joueurs) {
                            if (!j.getPosition().estHeliport()) {
                                tousSurHeliport = false;
                                break;
                            }
                        }
                
                        if (tousSurHeliport) {
                            System.out.println("\n Tous les joueurs sont sur l'héliport !");
                            System.out.println(" Tous les artefacts ont été récupérés !");
                            System.out.println(" Félicitations, vous vous êtes échappés avec succès !");
                            return;
                        } else {
                            System.out.println(" Tous les joueurs ne sont pas encore sur l'héliport !");
                        }
                    } else {
                        System.out.println(" Il manque encore des artefacts !");
                    }
                }

                else if (choix == 6) {
                    List<Joueur> autres = new ArrayList<>();
                    for (Joueur j : joueurs) {
                        if (j != joueurCourant && j.getPosition() == joueurCourant.getPosition()) {
                            autres.add(j);
                        }
                    }
                
                    if (autres.isEmpty()) {
                        System.out.println(" Aucun joueur sur la même case pour échanger !");
                    } else if (joueurCourant.getCles().isEmpty()) {
                        System.out.println(" Vous n'avez aucune clé à donner !");
                    } else {
                        // Affiche les joueurs disponibles
                        System.out.println("Joueurs disponibles pour l’échange :");
                        for (int i = 0; i < autres.size(); i++) {
                            System.out.println((i + 1) + " - " + autres.get(i).getNom());
                        }
                        System.out.print("Choisis un joueur : ");
                        int cible = sc.nextInt() - 1;
                
                        // Affiche les clés
                        List<Cle> clesCourantes = joueurCourant.getCles();
                        for (int i = 0; i < clesCourantes.size(); i++) {
                            System.out.println((i + 1) + " - " + clesCourantes.get(i));
                        }
                        System.out.print("Choisis une clé à donner : ");
                        int cleChoisie = sc.nextInt() - 1;
                
                        // Transfert
                        Cle cle = clesCourantes.get(cleChoisie);
                        joueurCourant.retirerCle(cle);
                        autres.get(cible).ajouterCle(cle);
                        System.out.println(" Clé " + cle + " donnée à " + autres.get(cible).getNom() + " !");
                        actionsRestantes--;
                    }
                }
                
            
            
                else {
                    System.out.println(" Choix invalide !");
                }
            }

            // Inondation
            g.inonder3Zones();

            // Vérifier si un artefact est perdu
            if (g.artefactSubmergePerdu()) {
                System.out.println(" Un artefact a été perdu sous l'eau... La mission est un échec !");
                return;
            }

            // Vérifier si un joueur est piégé
            for (Joueur j : joueurs) {
                if (j.estPiege(g)) {
                    System.out.println(" " + j.getNom() + " est piégé sur une zone submergée sans issue !");
                    System.out.println("La mission est un échec !");
                    return;
                }
            }

            // Pioche d'une clé aléatoire
            Cle[] types = Cle.values();
            Random rand = new Random();
            Cle piochee = types[rand.nextInt(types.length)];
            joueurCourant.ajouterCle(piochee);
            System.out.println(joueurCourant.getNom() + " a pioché une clé : " + piochee);

            tour++;

            // Pause entre les tours
            System.out.println("\n>>> Fin du tour de " + joueurCourant.getNom() + ". Les eaux montent...");
            System.out.print("Appuie sur Entrée pour continuer...");
            sc.nextLine();
        }
    }
}
