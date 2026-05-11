# game_ile_interdite
# L'île Interdite

Un jeu en Java implémentant une adaptation de **Forbidden Island** - un jeu coopératif où les joueurs doivent travailler ensemble pour récupérer les artefacts mystérieux et s'échapper d'une île qui s'enfonce.

##  Description

L'île Interdite est un jeu stratégique tour par tour où :
- Les joueurs explorent une grille de 7×7 zones
- Chaque zone peut être sèche, inondée ou immergée
- L'objectif est de récupérer les 4 artefacts (Air, Eau, Feu, Terre) et rejoindre l'héliport
- Les joueurs ont 3 actions par tour (se déplacer, assécher des zones, récupérer des artefacts, etc.)

##  Fonctionnalités

- **Grille de jeu dynamique** : Gestion des zones avec états d'inondation
- **Système de joueurs multiples** : Jeu en tour par tour avec alternance des joueurs
- **Mécanique d'asséchage** : Assécher les zones inondées pour les rendre traversables
- **Collecte d'artefacts** : Récupérer les 4 artefacts dispersés aléatoirement
- **Clés et déverrouillage** : Système de clés pour accéder à certaines zones
- **Héliport** : Zone d'échappement pour remporter la victoire
- **Interface graphique** : Affichage visuel du jeu avec Swing

##  Architecture

Le projet est composé de 7 classes Java :

| Classe | Rôle |
|--------|------|
| **Jeu.java** | Classe principale - gère la boucle de jeu |
| **Grille.java** | Gère l'état de la grille et des zones |
| **Zone.java** | Représente une zone avec ses états et artefacts |
| **Joueur.java** | Représente un joueur et ses actions |
| **FenetreJeu.java** | Interface graphique du jeu (Swing) |
| **GrillePanel.java** | Panneau d'affichage de la grille |
| **Cle.java** | Système de clés pour zones verrouillées |

##  Prérequis

- Java 8 ou supérieur
- Compilateur Java (javac)

##  Installation et Lancement

### Compilation
```bash
javac *.java
```

### Exécution
```bash
java Jeu
```

##  Comment Jouer

### Tour de jeu
Chaque joueur a **3 actions** par tour :

| Action | Description |
|--------|-------------|
| **1 - Déplacer** | Bouger vers une zone adjacente |
| **2 - Assécher** | Réduire le niveau d'inondation d'une zone |
| **3 - Rien** | Passer l'action |
| **4 - Récupérer artefact** | Collecter un artefact (si présent) |
| **5 - S'envoler** | Voler vers l'héliport (avec clé) |
| **6 - Donner une clé** | Partager une clé avec un autre joueur |

### Objectif
1. Récupérer les 4 artefacts : **Air**, **Eau**, **Feu**, **Terre**
2. Rejoindre l'héliport et s'échapper avant que l'île ne soit complètement immergée

### Conditions de victoire
- Tous les artefacts récupérés et les joueurs à l'héliport

### Conditions de défaite
- L'île s'enfonce complètement
- Un joueur se retrouve dans une zone immergée sans issue

## 📝 Exemple de Partie

```
=== Tour de Maria ===
Clés : Air(1), Eau(2)
Position actuelle : 2,2
Actions restantes : 3
Action ? (1 = Déplacer, 2 = Assécher, 3 = Rien, 4 = Récupérer artefact, 5 = S'envoler, 6 = Donner une clé) : 1
Direction (haut/bas/gauche/droite) : droite
```

##  Détails Techniques

- **Langage** : Java
- **Interface** : Swing (AWT)
- **Structure de données** : Grille 2D, ArrayList, Collections
- **Pattern** : Modèle MVC (Modèle-Vue-Contrôle)

##  Structure du Projet

```
project-pogl/
└── pogl/
    ├── Jeu.java           # Point d'entrée
    ├── Grille.java        # Logique de grille
    ├── Zone.java          # Définition des zones
    ├── Joueur.java        # Logique des joueurs
    ├── FenetreJeu.java    # Interface graphique
    ├── GrillePanel.java   # Affichage grille
    └── Cle.java           # Système de clés
```


##  Licence

Projet universitaire - Adaptation de Forbidden Island

---

**Dernière mise à jour** : 2026
