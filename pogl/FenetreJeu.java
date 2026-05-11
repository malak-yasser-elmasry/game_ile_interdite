import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FenetreJeu extends JFrame {
    private GrillePanel panelGrille;
    private JPanel panneauControle;
    private Grille g;
    private Image textureActive;
    private Image iconePersoRouge;
    private Image iconePersoBleu;
    private Image iconeHeliport;
    private Image iconeAir;
    private Image iconeEau;
    private Image iconeFeu;
    private Image iconeTerre;
    
    // Variables du jeu
    private int tourJoueur = 0;
    private int actionsRestantes = 3;
    private JLabel labelJoueurActif;
    private JLabel labelActions;
    private JTextArea zoneInfo;
    private boolean modeAssechementActif = false;

    public FenetreJeu(Grille g) {
        this.g = g;
    
        setTitle("L'île Interdite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700); // Augmenté pour accueillir le panneau de contrôle
        setLocationRelativeTo(null);
    
        // Chargement des images
        textureActive = new ImageIcon(getClass().getResource("texture_active.png")).getImage();
        iconePersoRouge = new ImageIcon(getClass().getResource("persoRouge.png")).getImage();
        iconePersoBleu = new ImageIcon(getClass().getResource("persoBleu.png")).getImage();
        
        // Chargement des nouvelles images
        // Utilisez les images que vous avez fournies
        iconeHeliport = new ImageIcon(getClass().getResource("heliport.png")).getImage();
        iconeAir = new ImageIcon(getClass().getResource("artefact_air.png")).getImage();
        iconeEau = new ImageIcon(getClass().getResource("artefact_eau.png")).getImage();
        iconeFeu = new ImageIcon(getClass().getResource("artefact_feu.png")).getImage();
        iconeTerre = new ImageIcon(getClass().getResource("artefact_terre.png")).getImage();
        
        // Vérifier le chargement des images
        if (iconeHeliport == null) {
            System.out.println("Erreur: heliport.png non trouvée");
        }
        if (iconeAir == null) {
            System.out.println("Erreur: artefact_air.png non trouvée");
        }
        if (iconeEau == null) {
            System.out.println("Erreur: artefact_eau.png non trouvée");
        }
        if (iconeFeu == null) {
            System.out.println("Erreur: artefact_feu.png non trouvée");
        }
        if (iconeTerre == null) {
            System.out.println("Erreur: artefact_terre.png non trouvée");
        }
        
        // Création du layout principal
        setLayout(new BorderLayout());
        
        // Création du panneau de grille avec toutes les images
        panelGrille = new GrillePanel(g, textureActive, iconePersoRouge, iconePersoBleu, 
                                    iconeHeliport, iconeAir, iconeEau, iconeFeu, iconeTerre);
        add(panelGrille, BorderLayout.CENTER);
        
        // Création du panneau de contrôle
        creerPanneauControle();
        add(panneauControle, BorderLayout.EAST);
        
        // Redimensionnement automatique
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelGrille.repaint();
            }
        });
        
        // Gestion des touches clavier
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gererTouchesClavier(e);
            }
        });
        
        setFocusable(true);
        requestFocus();
        
        // Affichage initial des informations
        mettreAJourInfosJeu();
        afficherInfo("Bienvenue dans l'Île Interdite! Utilisez les flèches du clavier pour vous déplacer.");

       //ajouté car sans ça les touchent ne répondent pas 
    addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
    }
    
    @Override
    public void focusLost(FocusEvent e) { 
    }
});

        

KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            
            // Mode assèchement prioritaire sur les déplacements
            if (modeAssechementActif) {
                String direction = "";
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q: direction = "ici"; break;
                    case KeyEvent.VK_Z: direction = "haut"; break;
                    case KeyEvent.VK_S: direction = "bas"; break;
                    case KeyEvent.VK_D: direction = "gauche"; break;
                    case KeyEvent.VK_F: direction = "droite"; break;
                    case KeyEvent.VK_ESCAPE: 
                        modeAssechementActif = false;
                        afficherInfo("Action annulée");
                        return true;
                }
                
                if (!direction.isEmpty()) {
                    Joueur joueurCourant = g.getJoueurs().get(tourJoueur);
                    boolean actionEffectuee = joueurCourant.assecher(direction, g);
                    modeAssechementActif = false; // Désactiver le mode assèchement
                    
                    if (actionEffectuee) {
                        afficherInfo("Zone asséchée avec succès dans la direction: " + direction);
                        actionsRestantes--;
                        mettreAJourInfosJeu();
                        repaint();
                        panelGrille.repaint();
                    } else {
                        afficherInfo("Impossible d'assécher cette zone!");
                    }
                    return true;
                }
            } else {
                // Si pas en mode assèchement, traiter les touches normales
                gererTouchesClavier(e);
            }
            return true; // Indique que l'événement a été traité
        }
        return false; // Laisse l'événement se propager
    }
});
        setVisible(true);
    }
    
    private void creerPanneauControle() {
        panneauControle = new JPanel();
        panneauControle.setLayout(new BoxLayout(panneauControle, BoxLayout.Y_AXIS));
        panneauControle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panneauControle.setPreferredSize(new Dimension(200, getHeight()));
        
        // Informations sur le tour de jeu
        JPanel panelInfos = new JPanel(new GridLayout(2, 1));
        labelJoueurActif = new JLabel("Tour: " + g.getJoueurs().get(tourJoueur).getNom());
        labelActions = new JLabel("Actions restantes: " + actionsRestantes);
        panelInfos.add(labelJoueurActif);
        panelInfos.add(labelActions);
        
        // Zone d'informations
        zoneInfo = new JTextArea(5, 20);
        zoneInfo.setEditable(false);
        zoneInfo.setLineWrap(true);
        zoneInfo.setWrapStyleWord(true);
        JScrollPane scrollInfo = new JScrollPane(zoneInfo);
        
        // Boutons d'actions
        JPanel panelBoutons = new JPanel(new GridLayout(6, 1, 5, 5));
        
        JButton btnDeplacer = new JButton("Déplacer");
        btnDeplacer.addActionListener(e -> {
            afficherInfo("Utilisez les flèches du clavier pour vous déplacer");
        });
        
        JButton btnAssecher = new JButton("Assécher");
        btnAssecher.addActionListener(e -> {
            selectionnerZoneAssechement();
        });
        
        JButton btnRecupArtefact = new JButton("Récupérer Artefact");
        btnRecupArtefact.addActionListener(e -> {
            recupererArtefact();
        });
        
        JButton btnDonnerCle = new JButton("Donner une clé");
        btnDonnerCle.addActionListener(e -> {
            donnerCle();
        });
        
        JButton btnSEnvoler = new JButton("S'envoler");
        btnSEnvoler.addActionListener(e -> {
            tenterEnvoler();
        });
        
        JButton btnFinTour = new JButton("Fin du tour");
        btnFinTour.addActionListener(e -> {
            finirTour();
        });
        
        panelBoutons.add(btnDeplacer);
        panelBoutons.add(btnAssecher);
        panelBoutons.add(btnRecupArtefact);
        panelBoutons.add(btnDonnerCle);
        panelBoutons.add(btnSEnvoler);
        panelBoutons.add(btnFinTour);
        
        // Affichage des clés du joueur actuel
        JPanel panelCles = new JPanel();
        panelCles.setBorder(BorderFactory.createTitledBorder("Clés du joueur"));
        panelCles.setLayout(new BoxLayout(panelCles, BoxLayout.Y_AXIS));
        
        // Ajouter un label pour les clés (sera mis à jour dans mettreAJourInfosJeu())
        JLabel labelCles = new JLabel("Aucune clé");
        panelCles.add(labelCles);
        
        // Ajout des composants au panneau de contrôle
        panneauControle.add(panelInfos);
        panneauControle.add(Box.createRigidArea(new Dimension(0, 10)));
        panneauControle.add(scrollInfo);
        panneauControle.add(Box.createRigidArea(new Dimension(0, 10)));
        panneauControle.add(panelBoutons);
        panneauControle.add(Box.createRigidArea(new Dimension(0, 10)));
        panneauControle.add(panelCles);
        
        // Espace vide en bas pour remplir
        panneauControle.add(Box.createVerticalGlue());
    }
    
    private void gererTouchesClavier(KeyEvent e) {
        if (actionsRestantes <= 0) {
            afficherInfo("Plus d'actions disponibles pour ce tour!");
            return;
        }
        
        Joueur joueurCourant = g.getJoueurs().get(tourJoueur);
        boolean actionEffectuee = false;
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                actionEffectuee = joueurCourant.deplacer("haut", g);
                if (actionEffectuee) {
                    afficherInfo(joueurCourant.getNom() + " s'est déplacé vers le haut");
                }
                break;
            case KeyEvent.VK_DOWN:
                actionEffectuee = joueurCourant.deplacer("bas", g);
                if (actionEffectuee) {
                    afficherInfo(joueurCourant.getNom() + " s'est déplacé vers le bas");
                }
                break;
            case KeyEvent.VK_LEFT:
                actionEffectuee = joueurCourant.deplacer("gauche", g);
                if (actionEffectuee) {
                    afficherInfo(joueurCourant.getNom() + " s'est déplacé vers la gauche");
                }
                break;
            case KeyEvent.VK_RIGHT:
                actionEffectuee = joueurCourant.deplacer("droite", g);
                if (actionEffectuee) {
                    afficherInfo(joueurCourant.getNom() + " s'est déplacé vers la droite");
                }
                break;
        }
        
        if (actionEffectuee) {
            actionsRestantes--;
            mettreAJourInfosJeu();
            repaint();
        }
    }
    
    private void selectionnerZoneAssechement() {
        if (actionsRestantes <= 0) {
            afficherInfo("Plus d'actions disponibles pour ce tour!");
            return;
        }
        
        modeAssechementActif = true;
        afficherInfo("Sélectionnez une zone à assécher (Q=ici, Z=haut, S=bas, D=gauche, F=droite)");
        System.out.println("Mode assèchement activé, en attente de touche (Q, Z, S, D, F)");
        requestFocus();
    }
    
    private void recupererArtefact() {
        if (actionsRestantes <= 0) {
            afficherInfo("Plus d'actions disponibles pour ce tour!");
            return;
        }
        
        Joueur joueurCourant = g.getJoueurs().get(tourJoueur);
        boolean actionEffectuee = joueurCourant.recupererArtefact(joueurCourant.getPosition());
        
        if (actionEffectuee) {
            afficherInfo("Artefact récupéré avec succès!");
            actionsRestantes--;
            mettreAJourInfosJeu();
            repaint();
        }
    }
    
    private void donnerCle() {
        if (actionsRestantes <= 0) {
            afficherInfo("Plus d'actions disponibles pour ce tour!");
            return;
        }
        
        Joueur joueurCourant = g.getJoueurs().get(tourJoueur);
        
        // Vérifier s'il y a d'autres joueurs sur la même case
        java.util.List<Joueur> autresJoueurs = new ArrayList<>();
        for (Joueur j : g.getJoueurs()) {
            if (j != joueurCourant && j.getPosition() == joueurCourant.getPosition()) {
                autresJoueurs.add(j);
            }
        }
        
        if (autresJoueurs.isEmpty()) {
            afficherInfo("Aucun joueur sur la même case pour échanger!");
            return;
        }
        
        if (joueurCourant.getCles().isEmpty()) {
            afficherInfo("Vous n'avez aucune clé à donner!");
            return;
        }
        
        // Afficher une boîte de dialogue pour choisir le joueur
        String[] nomsJoueurs = new String[autresJoueurs.size()];
        for (int i = 0; i < autresJoueurs.size(); i++) {
            nomsJoueurs[i] = autresJoueurs.get(i).getNom();
        }
        
        String joueurChoisi = (String) JOptionPane.showInputDialog(
            this, "Choisissez un joueur:", "Donner une clé", 
            JOptionPane.QUESTION_MESSAGE, null, nomsJoueurs, nomsJoueurs[0]);
            
        if (joueurChoisi == null) return; // Annulation
        
        // Trouver le joueur sélectionné
        Joueur cible = null;
        for (Joueur j : autresJoueurs) {
            if (j.getNom().equals(joueurChoisi)) {
                cible = j;
                break;
            }
        }
        
        // Afficher une boîte de dialogue pour choisir la clé
        String[] nomsCles = new String[joueurCourant.getCles().size()];
        java.util.List<Cle> clesCourantes = joueurCourant.getCles();
        for (int i = 0; i < clesCourantes.size(); i++) {
            nomsCles[i] = clesCourantes.get(i).toString();
        }
        
        String cleChoisie = (String) JOptionPane.showInputDialog(
            this, "Choisissez une clé à donner:", "Donner une clé", 
            JOptionPane.QUESTION_MESSAGE, null, nomsCles, nomsCles[0]);
            
        if (cleChoisie == null) return; // Annulation
        
        // Trouver la clé sélectionnée
        Cle cle = null;
        for (Cle c : clesCourantes) {
            if (c.toString().equals(cleChoisie)) {
                cle = c;
                break;
            }
        }
        
        // Effectuer le transfert
        joueurCourant.retirerCle(cle);
        cible.ajouterCle(cle);
        afficherInfo("Clé " + cle + " donnée à " + cible.getNom() + "!");
        
        actionsRestantes--;
        mettreAJourInfosJeu();
    }
    
    private void tenterEnvoler() {
        if (!Grille.tousArtefactsRecuperes()) {
            afficherInfo("Il manque encore des artefacts!");
            return;
        }
        
        // Vérifier si tous les joueurs sont sur l'héliport
        boolean tousSurHeliport = true;
        for (Joueur j : g.getJoueurs()) {
            if (!j.getPosition().estHeliport()) {
                tousSurHeliport = false;
                break;
            }
        }
        
        if (tousSurHeliport) {
            afficherInfo("Tous les joueurs sont sur l'héliport et tous les artefacts ont été récupérés!");
            JOptionPane.showMessageDialog(this, 
                "Félicitations, vous vous êtes échappés avec succès de l'île interdite!", 
                "Victoire", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else {
            afficherInfo("Tous les joueurs ne sont pas encore sur l'héliport!");
        }
    }
    
    private void afficherInfo(String message) {
        zoneInfo.append(message + "\n");
        zoneInfo.setCaretPosition(zoneInfo.getDocument().getLength());
    }
    
    private void mettreAJourInfosJeu() {
        Joueur joueurCourant = g.getJoueurs().get(tourJoueur);
        labelJoueurActif.setText("Tour: " + joueurCourant.getNom());
        labelActions.setText("Actions restantes: " + actionsRestantes);
        
        // Mettre à jour l'affichage des clés
        StringBuilder cleInfo = new StringBuilder("Clés: ");
        for (Cle c : joueurCourant.getCles()) {
            cleInfo.append(c).append(" ");
        }
        // Affichage dans le panneau ou dans la zone d'info selon votre préférence
        // Ici on l'affiche dans la zone d'info
        afficherInfo(cleInfo.toString());
    }
    
    private void finirTour() {
        Joueur joueurCourant = g.getJoueurs().get(tourJoueur);
        afficherInfo("Fin du tour de " + joueurCourant.getNom());
        
        // Inonder 3 zones
        g.inonder3Zones();
        panelGrille.repaint();
        afficherInfo("3 zones ont été inondées!");
        
        // Vérifier si un artefact est perdu
        if (g.artefactSubmergePerdu()) {
            afficherInfo("Un artefact a été perdu sous l'eau! La mission est un échec!");
            JOptionPane.showMessageDialog(this, 
                "Un artefact a été perdu sous l'eau!\nLa mission est un échec!", 
                "Défaite", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        
        // Vérifier si un joueur est piégé
        for (Joueur j : g.getJoueurs()) {
            if (j.estPiege(g)) {
                afficherInfo(j.getNom() + " est piégé sur une zone submergée sans issue! La mission est un échec!");
                JOptionPane.showMessageDialog(this, 
                    j.getNom() + " est piégé sur une zone submergée sans issue!\nLa mission est un échec!", 
                    "Défaite", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                return;
            }
        }
        
        // Piocher une clé aléatoire
        java.util.Random rand = new java.util.Random();
        Cle[] types = Cle.values();
        Cle piochee = types[rand.nextInt(types.length)];
        joueurCourant.ajouterCle(piochee);
        afficherInfo(joueurCourant.getNom() + " a pioché une clé: " + piochee);
        
        // Passer au joueur suivant
        tourJoueur = (tourJoueur + 1) % g.getJoueurs().size();
        actionsRestantes = 3;
        
        mettreAJourInfosJeu();
        repaint();
        panelGrille.repaint(); 
        SwingUtilities.updateComponentTreeUI(this);
        // message de deboggage pour voir si le tour est bien passé ou pas 
        System.out.println("Tour passé de " + joueurCourant.getNom() + " à " + g.getJoueurs().get(tourJoueur).getNom());
        System.out.println("Actions restantes : " + actionsRestantes);

        // À la fin de finirTour()
        SwingUtilities.invokeLater(() -> {
            requestFocus();
            System.out.println("Focus forcé après changement de tour");
});
    }
    
    // Méthode pour mettre à jour l'affichage
    public void rafraichir() {
        panelGrille.repaint();
    }
}