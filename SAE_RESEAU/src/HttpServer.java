import java.io.*;
import java.net.*;
import java.nio.Buffer;

/**
 * Classe principale du serveur HTTP.
 */
public class HttpServer {
    public static void main(String[] args) {

        int port = 80;

        try {
            // On lit le fichier config.xml pour récupérer les informations
            BufferedReader br = new BufferedReader(new FileReader("src/config.xml"));
            //On récupère la ligne qui contient le port
            String line1 = br.readLine();
            line1 = br.readLine();
            // On divise la ligne dans un tableau de sous-chaines, on divise sur le caractère '>'
            String[] parts = line1.split(">");
            parts = parts[1].split("<");
            //Si le port est renseigné (sinon il prend la valeur 80 par défaut)
            if (parts[0].length() > 0)
                port = Integer.parseInt(parts[0]);
            //On affiche le port
            System.out.println("Port : " + port);
            //On ferme l'objet BufferedReader br
            br.close();

            //On se connecte au serveur
            ServerSocket serverSocket = new ServerSocket(port);
            //On affiche le port sur lequel on souhaite se connecter
            System.out.println("Serveur en attente de connexions sur le port " + port + "...");

            while (true) {
                //On attend une connexion
                Socket clientSocket = serverSocket.accept();
                //On affiche que l'on a reçu une connexion
                System.out.println("Nouvelle connexion acceptée.");

                //On crée un objet BufferedReader pour lire les requêtes
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;

                //Première ligne de la requête dans la variable qui va récupérer le nom de html
                String nomHtml = reader.readLine();
                //On affiche la première ligne de la requête
                System.out.println(nomHtml);

                //On lit les lignes suivantes de la requête et on les affiche
                line = reader.readLine();
                System.out.println(line);
                while (!line.equals("")) {

                    line = reader.readLine();
                    System.out.println(line);
                }
                //On affiche que la requête est traitée
                System.out.println("Requête traitée.");
                //On ferme l'objet BufferedReader reader
                reader.close();

                //On récupère le nom du fichier html dans la première ligne de la requête
                nomHtml = nomHtml.split(" ")[1].substring(1);

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // On récupère le chemin d'accès absolu pour le dossier root
                BufferedReader bfr = new BufferedReader(new FileReader("src/config.xml"));
                //On récupère la ligne qui contient le chemin en question
                String line2 = br.readLine();
                line2 = br.readLine();
                line2 = br.readLine();

                FileInputStream fis = new FileInputStream("src/html/" + nomHtml);

                int i = fis.read();


                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type:  text/html" + "\n");
                out.writeBytes("\n");

                try {
                    while (i != -1) {
                        out.write(i);
                        i = fis.read();
                    }
                    System.out.println("Fichier envoyé.");
                } catch (SocketException e) {
                    System.out.println("La connexion a été fermée par le client.");
                } finally {
                    fis.close();
                    out.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
