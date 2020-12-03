package MyPackage;

import java.util.*;
import java.io.*;

public class Main {
    public static Map<String, Set<String>> setStatesRegions(String filePath) throws IOException {
        if (filePath == null) { //how to better detect different exceptions (how do i know if an invalid file path was entered)
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<String>> statesToRegions = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!statesToRegions.containsKey(wordsOnCurrentLine[0])) {
                Set<String> regions = new HashSet<>();
                regions.add(wordsOnCurrentLine[1]);
                statesToRegions.put(wordsOnCurrentLine[0], regions);
            } else {
                statesToRegions.get(wordsOnCurrentLine[0]).add(wordsOnCurrentLine[1]);
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return statesToRegions;
    }

    public static Map<String, Set<String>> setRegionsFlowers(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<String>> regionsToFlowers = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!regionsToFlowers.containsKey(wordsOnCurrentLine[0])) {
                Set<String> flowers = new HashSet<>();
                flowers.add(wordsOnCurrentLine[1]);
                regionsToFlowers.put(wordsOnCurrentLine[0], flowers);
            } else {
                regionsToFlowers.get(wordsOnCurrentLine[0]).add(wordsOnCurrentLine[1]);
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return regionsToFlowers;
    }

    public static Map<String, Map<Integer, Integer>> setStatesZipCodes(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Map<Integer, Integer>> statesToZipCodes = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!statesToZipCodes.containsKey(wordsOnCurrentLine[0])) {
                Map<Integer, Integer> states = new HashMap<>();
                states.put(Integer.parseInt(wordsOnCurrentLine[1]), Integer.parseInt(wordsOnCurrentLine[2]));
                statesToZipCodes.put(wordsOnCurrentLine[0], states);
            } else {
                statesToZipCodes.get(wordsOnCurrentLine[0]).put(Integer.parseInt(wordsOnCurrentLine[1]), Integer.parseInt(wordsOnCurrentLine[2]));
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return statesToZipCodes;
    }

    public static Map<String, Set<Integer>> setRegionsZipCodes(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<Integer>> regionsToZipCodes = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!regionsToZipCodes.containsKey(wordsOnCurrentLine[0])) {
                Set<Integer> zipCodes = new HashSet<>();
                zipCodes.add(Integer.parseInt(wordsOnCurrentLine[1]));
                regionsToZipCodes.put(wordsOnCurrentLine[0], zipCodes);
            } else {
                regionsToZipCodes.get(wordsOnCurrentLine[0]).add(Integer.parseInt(wordsOnCurrentLine[1]));
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return regionsToZipCodes;
    }

    public static Set<String> returnFlowers(String zipCode, Map<String, Set<String>> statesRegions, Map<String, Set<String>> regionsFlowers, Map<String, Map<Integer, Integer>> statesZipCodes, Map<String, Set<Integer>> regionsZipCodes) {
        int zipCodeInt = Integer.parseInt(zipCode);
        String myState = null;
        for (String state : statesZipCodes.keySet()) {
            for (int zipCode1 : statesZipCodes.get(state).keySet()) {
                if (zipCodeInt >= zipCode1 && zipCodeInt <= statesZipCodes.get(state).get(zipCode1)) {
                    myState = state;
                }
            }
        }
        if (myState == null) {
            System.out.println("Could not find a state for the given zip code.");
            return null;
        }
        ArrayList<String> regions = new ArrayList<>();
        for (String region : statesRegions.get(myState)) {
            regions.add(region);
        }
        ArrayList<String> trueRegion = new ArrayList();
        String targetRegion = "";
        if (regions.size() > 1) {
            for (String region : regions) {
                if (regionsZipCodes.containsKey(region)) {
                    if (regionsZipCodes.get(region).contains(zipCodeInt)) {
                        if (trueRegion.isEmpty()) {
                            trueRegion.add(region);
                        } else {
                            trueRegion.remove(0);
                            trueRegion.add(region);
                        }
                        break;
                    }
                } else {
                    trueRegion.add(region);
                }
            }
            targetRegion = trueRegion.get(0);
        } else {
            targetRegion = regions.get(0);
        }
        return regionsFlowers.get(targetRegion);
    }

    public static void main (String[] args) throws IOException {
        Map<String, Set<String>> statesRegions = setStatesRegions("states-regions.tsv");
        Map<String, Set<String>> regionsFlowers = setStatesRegions("regions-flowers.tsv");
        Map<String, Map<Integer, Integer>> statesZipCodes = setStatesZipCodes("states-zip codes.tsv");
        Map<String, Set<Integer>> regionsZipCodes = setRegionsZipCodes("regions-zip codes.tsv");
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Zip Code (5 digits only): ");
        String zipCode = in.nextLine();
        while (zipCode.length() != 5) {
            System.out.println("Zip Code must be 5 digits");
            System.out.println("Enter Zip Code (5 digits only): ");
            zipCode = in.nextLine();
        }
        System.out.println(returnFlowers(zipCode, statesRegions, regionsFlowers, statesZipCodes, regionsZipCodes));
    }
}