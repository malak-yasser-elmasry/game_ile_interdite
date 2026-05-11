public class Zone {

    public enum Etat { NORMALE, INONDEE, SUBMERGEE }
    public enum Artefact { NONE, AIR, EAU, FEU, TERRE }

    private Etat etat;
    private Artefact artefact;
    private boolean artefactRecupere = false;
    private boolean estHeliport = false;
    private int x, y;

    public Zone(int x, int y) {
        this.x = x;
        this.y = y;
        this.etat = Etat.NORMALE;
        this.artefact = Artefact.NONE;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Etat getEtat() { return etat; }
    public void setEtat(Etat e) { etat = e; }

    public Artefact getArtefact() { return artefact; }
    public void setArtefact(Artefact a) { artefact = a; }

    public boolean estHeliport() {
        return estHeliport;
    }
    
    public void setHeliport(boolean heliport) { this.estHeliport = heliport; }
    

    public boolean estArtefactRecupere() { return artefactRecupere; }

    public void setArtefactRecupere(boolean recupere) {
        this.artefactRecupere = recupere;
    }


    public void assecher() {
        if (etat == Etat.INONDEE) {
            etat = Etat.NORMALE;
        }
    }

    public void inonde() {
        if (etat == Etat.NORMALE) {
            etat = Etat.INONDEE;
        } else if (etat == Etat.INONDEE) {
            etat = Etat.SUBMERGEE;
        }
    }

    public boolean estAccessible() {
        return etat != Etat.SUBMERGEE;
    }

    @Override
public String toString() {
    String etatStr = switch (etat) {
        case NORMALE -> "N";
        case INONDEE -> "I";
        case SUBMERGEE -> "S";
    };

    if (artefact != Artefact.NONE && estHeliport) {
        String artStr = switch (artefact) {
            case AIR -> "A";
            case EAU -> "E";
            case FEU -> "F";
            case TERRE -> "T";
            default -> "?";
        };
        return "H" + artStr + "(" + etatStr + ")";
    } else if (artefact != Artefact.NONE) {
        String artStr = switch (artefact) {
            case AIR -> "A";
            case EAU -> "E";
            case FEU -> "F";
            case TERRE -> "T";
            default -> "?";
        };
        return artStr + "(" + etatStr + ")";
    } else if (estHeliport) {
        return "H(" + etatStr + ")";
    }

    return etatStr;
}

}
