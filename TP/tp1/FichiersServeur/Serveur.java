package FichiersServeur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    private static final int PORT = 2121;
    private static final String CHEMIN_SERVEUR = "./src/tp1/FichiersServeur/";
    private ServerSocket servSock;
    private Socket cliSock;
    private InputStream entree;
    private OutputStream sortie;

    private DataInputStream entreeIS;
    private DataOutputStream sortieOS;

    public Serveur() {
        try {
            servSock = new ServerSocket(PORT);
            System.out.println("Serveur : actif");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deconnecterClient() {
        try {
            sortieOS.close();
            sortie.close();
            entreeIS.close();
            entree.close();
            cliSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deconnecterClient(String s) {
        try {
            sortieOS.writeUTF(s);
            sortieOS.flush();
            sortieOS.close();
            sortie.close();
            entreeIS.close();
            entree.close();
            cliSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ecouter() {

        while (true) {
            try {
                BufferedReader br;

                System.out.println("Serveur : En attente d'une nouvelle connnexion");
                cliSock = servSock.accept();

                System.out.println("Serveur : un client vient de se connecter depuis " + cliSock.getInetAddress());

                sortie = cliSock.getOutputStream();

                entree = cliSock.getInputStream();


                sortieOS = new DataOutputStream(sortie);

                entreeIS = new DataInputStream(entree);

                String s1 = "Bienvenue sur le serveur\n";
                String s2 = "Manuel : \n<GET:nomFichier> ou <PUT:nomFichier>\n";

                sortieOS.writeUTF(s1);
                sortieOS.writeUTF(s2);
                sortieOS.writeUTF("null");
                sortieOS.flush();

                /* lecture demande du client */

                String request = entreeIS.readUTF();

                if (!(request.contains("GET:") || request.contains("PUT:"))) {
                    deconnecterClient("Pas une commande reconnue!");
                    continue;
                }

                String file = request.split(":")[1];
                if(request.contains("GET:")) {


                    sortieOS.writeUTF("ok get de fichier " + file);
                    sortieOS.writeUTF("null");
                    sortieOS.flush();

                    FileInputStream fis = new FileInputStream(CHEMIN_SERVEUR + file);

                    sortieOS.write(fis.readAllBytes());
                    fis.close();
                    deconnecterClient();

                } else if(request.contains("PUT:")) {

                    sortieOS.writeUTF("ok put de fichier " + file);
                    sortieOS.writeUTF("null");
                    sortieOS.flush();

                    FileOutputStream f = new FileOutputStream(CHEMIN_SERVEUR + file);
                    f.write(entreeIS.readAllBytes());
                    f.close();
                    deconnecterClient();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        Serveur serveur = new Serveur();

        serveur.ecouter();

    }
}