package FichiersClients;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String CHEMIN_CLIENT = "./src/tp1/FichiersClients/";

    private Socket cliSock;
    private String hote;
    private int port;
    private InputStream entree;
    private OutputStream sortie;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Client(String h, int p) {
        hote = h;
        port = p;
    }


    public void initierConnexion() {
        try {
            Scanner scanner = new Scanner(System.in);
            cliSock = new Socket(hote, port);
            System.out.println("Client : connecté au serveur");

            sortie = cliSock.getOutputStream();

            entree = cliSock.getInputStream();

            dis = new DataInputStream(entree);
            dos = new DataOutputStream(sortie);

            String line;

            while (!(line = dis.readUTF()).equals("null")) {
                System.out.println(line);
            }


            String request = scanner.nextLine();

            dos.writeUTF(request);
            dos.flush();


            while(!(line = dis.readUTF()).equals("null")) {
                System.out.println(line);
                if(!(line.contains("ok"))) {
                    return;
                }
                String nomFichier = request.split(":")[1].trim();

                if(request.contains("GET")) {
                    /* récupérer le fichier envoyé */

                    FileOutputStream f = new FileOutputStream(CHEMIN_CLIENT + nomFichier, false);
                    f.write(dis.readAllBytes());
                    f.close();
                    return;
                }
                if(request.contains("PUT")) {
                    /* trouver le fichier, l'envoyer */

                    FileInputStream f = new FileInputStream(CHEMIN_CLIENT + nomFichier);
                    dos.write(f.readAllBytes());
                    dos.flush();
                    f.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        Client c = new Client("127.0.0.1", 2121);
        c.initierConnexion();
        System.out.println("done");

    }
}
