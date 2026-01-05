package com.example;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class App {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        // Opció 1: deixar que el client llegeixi la clau de GEMINI_API_KEY / GOOGLE_API_KEY
        // Client client = new Client();
        boolean exit = false;
        // Opció 2: passar explícitament l'API key (també la llegeixo de l'entorn)
        String apiKey = System.getenv("GEMINI_API_KEY");// Reemplaça-ho amb la teva clau d'API real
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: falta la variable d'entorn GEMINI_API_KEY");
            System.err.println("Si us plau, exporta GEMINI_API_KEY amb la teva API key de Gemini.");
            return;
        }
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
                    // Generar sets
                    break;

                case "2":
                    // Visualitzar sets
                    break;

                case "3":
                    // Esborrar sets
                    break;

                case "4":
                    exit = true;
                    break;

                default:
                    System.out.println("Opció incorrecta");
            }
        } while (!exit);

        Client client = Client.builder()
                .apiKey(apiKey)  // usem l'API key del Gemini Developer API
                .build();

        // Prompt a enviar al model
        String prompt = "Donem un conjunt de dades que es puguin importar en format llista i que continguin mamifers.";

        try {
            // "gemini-2.5-flash" és un dels models recomanats per text :contentReference[oaicite:4]{index=4}
            GenerateContentResponse response =
                    client.models.generateContent("gemini-2.5-flash", prompt, null);

            System.out.println("=== Resposta de Gemini ===");
            System.out.println(response.text());
        } catch (Exception e) {
            System.err.println("S'ha produït un error cridant a Gemini:");
            e.printStackTrace();
        }
    }
}