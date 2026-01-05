package com.example;

import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class App {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) return;

        Client client = Client.builder().apiKey(apiKey).build();
        boolean exit = false;

        do {
            System.out.println("------------------------------");
            System.out.println(" Generador de Sets de Dades");
            System.out.println("------------------------------");
            System.out.println("1. Generar un nou set de dades");
            System.out.println("2. Visualitzar sets de dades");
            System.out.println("3. Esborrar sets de dades");
            System.out.println("4. Sortir");
            System.out.print("Opció: ");

            String entrada = sc.nextLine();

            switch (entrada) {
                case "1":
                    System.out.print("Nombre d'elements: ");
                    int n = Integer.parseInt(sc.nextLine());
                    System.out.print("Descripció del set: ");
                    String desc = sc.nextLine();

                    String prompt = "Genera una llista de 20 noms de animals mamífers que siguin nocturns."
                            + n
                            + " elements segons aquesta descripció: "
                            + desc
                            + ". Retorna només la llista.";

                    try {
                        GenerateContentResponse response =
                                client.models.generateContent("gemini-2.5-flash", prompt, null);

                        List<String> list = Arrays.asList(
                                response.text()
                                        .replace("[", "")
                                        .replace("]", "")
                                        .replace("\"", "")
                                        .split(",")
                        );

                        System.out.println("=== Set generat ===");
                        System.out.println(list);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "2":
                case "3":
                    System.out.println("Funcionalitat pendent...");
                    break;

                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Opció incorrecta");
            }

        } while (!exit);
    }
}
