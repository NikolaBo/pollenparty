import java.util.*;
import java.io.*;

public class GeoCoder {
    private String statesRegionsPath, statesZipCodesPath, regionsZipCodesPath, regionFlowersPath, regionDescriptionsPath, flowerDescriptionsPath;
    private Map<String, Set<String>> statesRegions, regionFlowers;
    private Map<String, Map<Integer, Integer>> statesZipCodes;
    private Map<String, Set<Integer>> regionsZipCodes;
    private Map<String, String> regionDescriptions, flowerDescriptions;
    public GeoCoder(String statesRegionsPath, String statesZipCodesPath, String regionsZipCodesPath, String regionFlowersPath, String regionDescriptionsPath, String flowerDescriptionsPath) throws IOException {
        this.statesRegionsPath = statesRegionsPath;
        this.statesZipCodesPath = statesZipCodesPath;
        this.regionsZipCodesPath = regionsZipCodesPath;
        this.regionFlowersPath = regionFlowersPath;
        this.regionDescriptionsPath = regionDescriptionsPath;
        this.flowerDescriptionsPath = flowerDescriptionsPath;
        statesRegions = setStatesRegions(this.statesRegionsPath);
        regionFlowers = setregionFlowers(this.regionFlowersPath);
        statesZipCodes = setStatesZipCodes(this.statesZipCodesPath);
        regionsZipCodes = setRegionsZipCodes(this.regionsZipCodesPath);
        regionDescriptions = setregionDescriptions(this.regionDescriptionsPath);
        flowerDescriptions = setflowerDescriptions(this.flowerDescriptionsPath);
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

    public Map<String, Set<String>> setregionFlowers(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, Set<String>> regionsToFlowers = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            Set<String> flowers = new HashSet<>();
            for (int i = 1; i < wordsOnCurrentLine.length; i++) {
                flowers.add(wordsOnCurrentLine[i].trim());
            }
            regionsToFlowers.put(wordsOnCurrentLine[0].trim(), flowers);
            currentLine = reader.readLine();
        }
        reader.close();
        return regionsToFlowers;
    }

    public Map<String, String> setregionDescriptions(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, String> regionsToDescriptions = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            regionsToDescriptions.put(wordsOnCurrentLine[0].trim(), wordsOnCurrentLine[1].trim());
            currentLine = reader.readLine();
        }
        reader.close();
        return regionsToDescriptions;
    }

    public Map<String, String> setflowerDescriptions(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("Invalid File Path");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<String, String> flowersToDescriptions = new HashMap<>();
        String currentLine = reader.readLine();
        String[] wordsOnCurrentLine;
        while (currentLine != null) {
            wordsOnCurrentLine = currentLine.split("\t");
            flowersToDescriptions.put(wordsOnCurrentLine[0].trim(), wordsOnCurrentLine[1].trim());
            currentLine = reader.readLine();
        }
        reader.close();
        return flowersToDescriptions;
    }
    public class Information {
        private String region, regionDescription;
        private Map<String, String> flowers;
        public Information(String region, String regionDescription, Map<String, String> flowers) {
            this.region = region;
            this.regionDescription = regionDescription;
            this.flowers = flowers;
        }
        public String toString() {
            String toReturn = "";
            toReturn += this.region + ": " + this.regionDescription;
            for (String flower : flowers.keySet()) {
                toReturn += "\n";
                toReturn += flower + ": " + this.flowers.get(flower);
            }
            return toReturn;
        }
        public String getRegion() {
            return this.region;
        }
        public String getRegionDescription() {
            return this.regionDescription;
        }
        public Map<String, String> getFlowers() {
            return this.flowers;
        }
    }

    public GeoCoder.Information returnFlowers(String zipCode) {
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
            throw new IllegalArgumentException();
        }
        ArrayList<String> regions = new ArrayList<>();
        for (String region : this.statesRegions.get(myState)) {
            regions.add(region);
        }
        ArrayList<String> trueRegion = new ArrayList(); //USE Stack or queue.. instead
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
            targetRegion = trueRegion.get(0);
        } else {
            targetRegion = regions.get(0);
        }
        Map<String, String> flowers = new HashMap<>();
        for (String f : this.regionFlowers.get(targetRegion)) {
            flowers.put(f, this.flowerDescriptions.get(f));
            //System.out.println(this.flowerDescriptions.get(f));
        }
        Information correctFlowers = new Information(targetRegion, this.regionDescriptions.get(targetRegion), flowers);
        return correctFlowers;
    }

    public static void main (String[] args) throws IOException {
        GeoCoder files = new GeoCoder("data/states-regions.tsv", "data/states-zipcodes.tsv", "data/regions-zipcodes.tsv", "data/regions-flowers.tsv", "data/regions-descriptions.tsv", "data/flowers-descriptions.tsv");
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
