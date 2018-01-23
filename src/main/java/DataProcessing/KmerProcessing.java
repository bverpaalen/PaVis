package DataProcessing;

import DataRequester.GeneDatabaseRequester;
import Objects.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KmerProcessing {
    private static final GeneDatabaseRequester GP = new GeneDatabaseRequester();

    /**
     *
     * @param gene gene name to be requested
     * @param genomes genomes where gene is present
     * @return String in json format ready to be used by Tubemaps
     */
    public String RequestKmers(String gene, List<String> genomes){
        HashMap<String,List<Node>> geneKmers = new HashMap();
        List<Node> nodes = new ArrayList<Node>();

        for(int i=0;i<genomes.size();i++){
            String genome = genomes.get(i);
            nodes = GP.GeneToKmers(gene,genome);
        }
        geneKmers.put(gene, nodes);

        System.out.println(geneKmers);

        return geneKmers.toString();
    }
}
