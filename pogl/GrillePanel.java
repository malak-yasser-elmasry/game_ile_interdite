import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GrillePanel extends JPanel {
    private static final int TAILLE = 7;
    private Grille grille;
    private Image textureActive;
    private Image iconePersoRouge;
    private Image iconePersoBleu;
    private Image iconeHeliport;
    private Image iconeAir;
    private Image iconeEau;
    private Image iconeFeu;
    private Image iconeTerre;
    
    public GrillePanel(Grille grille, Image textureActive, Image iconePersoRouge, Image iconePersoBleu, 
                 Image iconeHeliport, Image iconeAir, Image iconeEau, Image iconeFeu, Image iconeTerre) {

        this.grille = grille;
        this.textureActive = textureActive;
        this.iconePersoRouge = iconePersoRouge;
        this.iconePersoBleu = iconePersoBleu;
        this.iconeHeliport = iconeHeliport;
        this.iconeAir = iconeAir;
        this.iconeEau = iconeEau;
        this.iconeFeu = iconeFeu;
        this.iconeTerre = iconeTerre;

        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Activation de l'antialiasing pour un meilleur rendu
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Calcul de la taille de chaque cellule
        int largeur = getWidth();
        int hauteur = getHeight();
        int cellWidth = largeur / TAILLE;
        int cellHeight = hauteur / TAILLE;
        
        // Dessin de chaque cellule
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                int x = j * cellWidth;
                int y = i * cellHeight;
                
                // Vérifier si la zone existe
                Zone zone = grille.getZone(i, j);
                if (zone == null) {
                    // Dessiner une zone inactive (gris foncé)
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRect(x, y, cellWidth, cellHeight);
                } else {
                    // Dessiner la texture de fond selon l'état de la zone
                    switch (zone.getEtat()) {
                        case NORMALE:
                            g2d.drawImage(textureActive, x, y, cellWidth, cellHeight, this);
                            break;
                        case INONDEE:
                            g2d.drawImage(textureActive, x, y, cellWidth, cellHeight, this);
                            // Ajouter une teinte bleue semi-transparente pour l'inondation
                            g2d.setColor(new Color(0, 0, 255, 70));
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                        case SUBMERGEE:
                            // Zone submergée (bleu foncé)
                            g2d.setColor(new Color(0, 0, 150));
                            g2d.fillRect(x, y, cellWidth, cellHeight);
                            break;
                    }
                    
                    // Dessiner une bordure
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, cellWidth, cellHeight);
                    
                    // Afficher l'héliport si c'est le cas
                   // Afficher l'héliport si c'est le cas
if (zone.estHeliport()) {
    // Taille de l'icône d'héliport
    int heliSize = Math.min(cellWidth, cellHeight) / 3;
    int heliX = x + (cellWidth - heliSize) / 2;
    int heliY = y + (cellHeight - heliSize) / 2;
    
    // Dessiner l'image de l'héliport
    if (iconeHeliport != null) {
        g2d.drawImage(iconeHeliport, heliX, heliY, heliSize, heliSize, this);
        
    } 
}
                    
                    // Afficher l'artefact s'il y en a un
                    // Afficher l'artefact s'il y en a un
if (zone.getArtefact() != Zone.Artefact.NONE) {
    // Taille de l'icône d'artefact
    int artefactSize = Math.min(cellWidth, cellHeight) / 4;
    int artefactX = x + 10;
    int artefactY = y + cellHeight - artefactSize - 10;
    
    Image iconeArtefact = null;
    
    
    // Sélectionner la bonne icône selon le type d'artefact
    switch (zone.getArtefact()) {
        case AIR:
            iconeArtefact = iconeAir;
            
            break;
        case EAU:
            iconeArtefact = iconeEau;
            
            break;
        case FEU:
            iconeArtefact = iconeFeu;
            
            break;
        case TERRE:
            iconeArtefact = iconeTerre;
            
            break;
        default:
            break;
    }
    
    // Dessiner l'image de l'artefact
    if (iconeArtefact != null) {
        g2d.drawImage(iconeArtefact, artefactX, artefactY, artefactSize, artefactSize, this);
        
    }
}
}
                    
     // Dessiner les joueurs sur cette zone
    for (Joueur joueur : grille.getJoueurs()) {
         Zone position = joueur.getPosition();
            if (position != null && position.getX() == i && position.getY() == j) {
                Image icone = joueur.getNom().equals("Maria") ? iconePersoRouge : iconePersoBleu;
                            
                // Calculer la taille du personnage (environ 1/3 de la cellule)
                int playerSize = Math.min(cellWidth, cellHeight) / 3;
                            
                // Positionner le personnage au centre-haut de la cellule
                int playerX = x + (cellWidth - playerSize) / 2;
                int playerY = y + playerSize / 2;
                            
                // Dessiner l'image du personnage
                g2d.drawImage(icone, playerX, playerY, playerSize, playerSize, this);
                }
            }
        }
    }
}
}
