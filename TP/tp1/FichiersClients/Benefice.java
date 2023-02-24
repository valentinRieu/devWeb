package FichiersClients;

public class Benefice implements Runnable{

    private CoffreFort coffre;
    private String type;
    private int periodicite;
    private int montant;

    public Benefice(CoffreFort c, String t, int p, int m) {
        coffre = c;
        type = t;
        periodicite = p;
        montant = m;
    }

    public String getType() {
        return type;
    }

    public void afficher() {
        System.out.println("Arrivée du bénéfice " + type + " de " + montant + " sur le coffre " + coffre.getNom() + "(thread " + Thread.currentThread().getId() + ")");
    }
    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {


            afficher();
            coffre.livrer(montant);

            if (periodicite <= 0) {
                System.out.println("Interruption du thread " + Thread.currentThread().getId());
                Thread.currentThread().interrupt();
            }

            try {
                Thread.sleep(periodicite);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Interruption demandée du thread " + Thread.currentThread().getId());
    }

    public void setMontant(int i) {
        montant = i;
    }

    public int getMontant() {
        return montant;
    }
}
