package com.example;

import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class App {

    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "datasets.json";
    static final String MODEL = "gemini-2.5-flash";

    public static void main(String[] args) {

        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) return;

        Client client = Client.builder().apiKey(apiKey).build();
        Map<String, List<String>> datasets = loadDatasets();
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
                    System.out.print("Nom del set: ");
                    String name = sc.nextLine();
                    System.out.print("Nombre d'elements: ");
                    int n = Integer.parseInt(sc.nextLine());
                    System.out.print("Descripció del set: ");
                    String desc = sc.nextLine();

                    String prompt = "Genera EXACTAMENT una llista JSON amb "
                            + n
                            + " elements segons aquesta descripció: "
                            + desc
                            + ". Retorna només la llista.";

                    try {
                        GenerateContentResponse response =
                                client.models.generateContent(MODEL, prompt, null);

                        List<String> list = Arrays.asList(
                                response.text().replace("[","").replace("]","").replace("\"","").split(",")
                        );

                        datasets.put(name, list);
                        saveDatasets(datasets);
                        System.out.println("Set guardat correctament");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "2":
                    if (datasets.isEmpty()) {
                        System.out.println("No hi ha sets guardats");
                        break;
                    }
                    for (Map.Entry<String, List<String>> e : datasets.entrySet()) {
                        System.out.println(e.getKey() + " -> " + e.getValue());
                    }
                    break;

                case "3":
                    if (datasets.isEmpty()) {
                        System.out.println("No hi ha sets guardats");
                        break;
                    }
                    System.out.print("Nom del set a esborrar: ");
                    String delKey = sc.nextLine();
                    datasets.remove(delKey);
                    saveDatasets(datasets);
                    System.out.println("Set esborrat");
                    break;

                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Opció incorrecta");
            }

        } while (!exit);
    }

    private static Map<String, List<String>> loadDatasets() {
        Map<String,List<String>> map = new LinkedHashMap<>();
        Path path = Paths.get(FILE_NAME);
        if(!Files.exists(path)) return map;
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            content = content.trim();
            if(content.length()<2) return map;
            content = content.substring(1,content.length()-1);
            String[] entries = content.split("],");
            for(String e:entries){
                if(!e.endsWith("]")) e+="]";
                String[] parts = e.split(":",2);
                String key = parts[0].trim().replace("\"","");
                List<String> list = Arrays.asList(parts[1].replace("[","").replace("]","").replace("\"","").split(","));
                map.put(key,list);
            }
        } catch (IOException ignored){}
        return map;
    }

    private static void saveDatasets(Map<String,List<String>> datasets){
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        int i=0;
        for(Map.Entry<String,List<String>> e: datasets.entrySet()){
            sb.append("  \"").append(e.getKey()).append("\": ").append(e.getValue());
            if(++i<datasets.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("}");
        try {
            Files.writeString(Paths.get(FILE_NAME), sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException ignored){}
    }
}
