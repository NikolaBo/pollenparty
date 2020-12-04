package MyPackage;

import java.util.*;
import java.io.*;

public class GeoCoder {
    private String statesRegionsPath, statesZipCodesPath, regionsZipCodesPath, regionsFlowersPath;
    private Map<String, Set<String>> statesRegions, regionsFlowers;
    private Map<String, Map<Integer, Integer>> statesZipCodes;
    private Map<String, Set<Integer>> regionsZipCodes;
    public GeoCoder(String statesRegionsPath, String statesZipCodesPath, String regionsZipCodesPath, String regionsFlowersPath) throws IOException {
        this.statesRegionsPath = statesRegionsPath;
        this.statesZipCodesPath = statesZipCodesPath;
        this.regionsZipCodesPath = regionsZipCodesPath;
        this.regionsFlowersPath = regionsFlowersPath;
        statesRegions = setStatesRegions(this.statesRegionsPath);
        regionsFlowers = setRegionsFlowers(this.regionsFlowersPath);
        statesZipCodes = setStatesZipCodes(this.statesZipCodesPath);
        regionsZipCodes = setRegionsZipCodes(this.regionsZipCodesPath);
    }

    public Map<String, Set<String>> setStatesRegions(String filePath) throws IOException {
        if (filePath == null) { //how to better detect different exceptions (how do i know if an invalid file path was entered)
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<String>> statesToRegions = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!statesToRegions.containsKey(wordsOnCurrentLine[0].trim())) {
                Set<String> regions = new HashSet<>();
                regions.add(wordsOnCurrentLine[1].trim());
                statesToRegions.put(wordsOnCurrentLine[0].trim(), regions);
            } else {
                statesToRegions.get(wordsOnCurrentLine[0].trim()).add(wordsOnCurrentLine[1].trim());
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return statesToRegions;
    }

    public Map<String, Set<String>> setRegionsFlowers(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<String>> regionsToFlowers = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!regionsToFlowers.containsKey(wordsOnCurrentLine[0].trim())) {
                Set<String> flowers = new HashSet<>();
                flowers.add(wordsOnCurrentLine[1].trim());
                regionsToFlowers.put(wordsOnCurrentLine[0].trim(), flowers);
            } else {
                regionsToFlowers.get(wordsOnCurrentLine[0].trim()).add(wordsOnCurrentLine[1].trim());
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return regionsToFlowers;
    }

    public Map<String, Map<Integer, Integer>> setStatesZipCodes(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Map<Integer, Integer>> statesToZipCodes = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!statesToZipCodes.containsKey(wordsOnCurrentLine[0].trim())) {
                Map<Integer, Integer> states = new HashMap<>();
                states.put(Integer.parseInt(wordsOnCurrentLine[1].trim()), Integer.parseInt(wordsOnCurrentLine[2].trim()));
                statesToZipCodes.put(wordsOnCurrentLine[0].trim(), states);
            } else {
                statesToZipCodes.get(wordsOnCurrentLine[0].trim()).put(Integer.parseInt(wordsOnCurrentLine[1].trim()), Integer.parseInt(wordsOnCurrentLine[2].trim()));
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return statesToZipCodes;
    }

    public Map<String, Set<Integer>> setRegionsZipCodes(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<Integer>> regionsToZipCodes = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            if(!regionsToZipCodes.containsKey(wordsOnCurrentLine[0].trim())) {
                Set<Integer> zipCodes = new HashSet<>();
                zipCodes.add(Integer.parseInt(wordsOnCurrentLine[1].trim()));
                regionsToZipCodes.put(wordsOnCurrentLine[0].trim(), zipCodes);
            } else {
                regionsToZipCodes.get(wordsOnCurrentLine[0].trim()).add(Integer.parseInt(wordsOnCurrentLine[1].trim()));
            }
            currentLine = reader.readLine();
        }
        reader.close();
        return regionsToZipCodes;
    }

    public Set<String> returnFlowers(String zipCode) {
        int zipCodeInt = Integer.parseInt(zipCode);
        String myState = null;
        for (String state : this.statesZipCodes.keySet()) {
            for (int zipCode1 : this.statesZipCodes.get(state).keySet()) {
                if (zipCodeInt >= zipCode1 && zipCodeInt <= this.statesZipCodes.get(state).get(zipCode1)) {
                    myState = state;
                }
            }
        }
        if (myState == null) {
            System.out.println("Could not find a state for the given zip code.");
            return null;
        }
        ArrayList<String> regions = new ArrayList<>();
        for (String region : this.statesRegions.get(myState)) {
            regions.add(region);
        }
        ArrayList<String> trueRegion = new ArrayList();
        String targetRegion = "";
        if (regions.size() > 1) {
            for (String region : regions) {
                if (this.regionsZipCodes.containsKey(region)) {
                    if (this.regionsZipCodes.get(region).contains(zipCodeInt)) {
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
            targetRegion = trueRegion.get(0); //USE Stack or queue.. instead
        } else {
            targetRegion = regions.get(0);
        }
        return this.regionsFlowers.get(targetRegion);
    }
    public static void main (String[] args) throws IOException {
        GeoCoder files = new GeoCoder("states-regions.tsv", "states-zip codes.tsv", "regions-zip codes.tsv", "regions-flowers.tsv");
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Zip Code (5 digits only): ");
        String zipCode = in.nextLine();
        while (zipCode.length() != 5) {
            System.out.println("Zip Code must be 5 digits");
            System.out.println("Enter Zip Code (5 digits only): ");
            zipCode = in.nextLine();
        }
        System.out.println(files.returnFlowers(zipCode));
    }
}
