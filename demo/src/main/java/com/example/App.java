package com.example;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class App {
    public static void main(String[] args) {
        // Opció 1: deixar que el client llegeixi la clau de GEMINI_API_KEY / GOOGLE_API_KEY
        // Client client = new Client();

        // Opció 2: passar explícitament l'API key (també la llegeixo de l'entorn)
        String apiKey = System.getenv("GEMINI_API_KEY");// Reemplaça-ho amb la teva clau d'API real
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: falta la variable d'entorn GEMINI_API_KEY");
            System.err.println("Si us plau, exporta GEMINI_API_KEY amb la teva API key de Gemini.");
            return;
        }

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