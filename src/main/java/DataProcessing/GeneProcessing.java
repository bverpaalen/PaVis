package DataProcessing;

import DataRequester.GeneDatabaseRequester;
import Objects.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;

public class GeneProcessing {
    private static final  GeneDatabaseRequester GP = new GeneDatabaseRequester();
    private static final  ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @param geneName name of gene requested
     * @param genomeId genome number of requested gene
     * @return gene object with basic information
     */
    public String RequestGene(String geneName, String genomeId) {
        try {
            String GenesAsJson = new Gson().toJson(GP.getGene(geneName,genomeId));
            List<Gene> allGenes = new ArrayList<Gene>();
            allGenes = mapper.readValue(GenesAsJson,new TypeReference<List<Gene>>(){});

            if(allGenes.size()>1){
                System.out.println(allGenes.size());
            }
            Gene gene = allGenes.get(0);

            return mapper.writeValueAsString(gene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param genomeNumbers list of requested genomeNumbers
     * @return String in json format ready for Tubemap usage
     */
    public String PangenomeGeneRequest(List<String> genomeNumbers){
        try{
            List toReturn = new ArrayList();
            List<Node> nodes = new ArrayList<Node>();
            List<Path> paths = new ArrayList<Path>();
            List<String> allNodeNames = new ArrayList<String>();

            for(int i = 0; i < genomeNumbers.size();i++){
                List<String> nodeNames = new ArrayList<String>();
                String genesAsJson = new String();
                List<String> sequence = new ArrayList<String>();

                List<Gene> allGenes = new ArrayList<Gene>();

                String genomeNumber = genomeNumbers.get(i);
                genesAsJson += new Gson().toJson(GP.AllGenesFromGenome(genomeNumber));
                allGenes = mapper.readValue(genesAsJson, new TypeReference<List<Gene>>() {});

                // To make the visualization easyer to understand duplications get a different id compared to the orginal gene
                for(int j=0;j < allGenes.size();j++){
                    Gene gene = allGenes.get(j);
                    String name= gene.getName();
                    Node node = new Node();
                    node.setName(name);
                    node.setSeq(name);
                    node.setGenomeNumber(String.valueOf(gene.getAddress().get(0)));
                    if(!nodeNames.contains(name) && !allNodeNames.contains(name)){
                        allNodeNames.add(name);
                        nodeNames.add(name);
                        nodes.add(node);
                    }
                    else if(!nodeNames.contains(name)){
                        nodeNames.add(name);
                    }
                    else{
                        int occurrences = Collections.frequency(nodeNames,name);
                        nodeNames.add(name);
                        node.setSeq(name);
                        name = name + Integer.toString(occurrences);
                        node.setName(name);
                        nodes.add(node);
                    }
                    sequence.add(name);
                }

                int numberOfGenome = i + 1;
                Path path = new Path(i,genomeNumber,"genome "+(numberOfGenome),sequence);
                paths.add(path);
            }
            toReturn.add(paths);
            toReturn.add(nodes);

            return mapper.writeValueAsString(toReturn);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
