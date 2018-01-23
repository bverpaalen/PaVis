package DataProcessing;

import DataRequester.PangenomeDatabaseRequester;
import Objects.Node;
import Objects.Path;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PangenomeProcessing {
    PangenomeDatabaseRequester PDR = new PangenomeDatabaseRequester();
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Old code, return k-mers from genomes
     * @param genomeNames names of requested genome
     * @return String in json format with k-mers from genomes
     */
    public String testGenome(List<String> genomeNames) throws IOException {
        List nodes = new ArrayList();
        List nodesNames = new ArrayList();
        List paths = new ArrayList();


        for(int i = 0;i<genomeNames.size();i++){
            List<String> sequence = new ArrayList<String>();
            String genomeName = genomeNames.get(i);
            String allNodesAsJson = new Gson().toJson(PDR.getSpecificGenome(genomeName));
            List<Node> allNodes = mapper.readValue(allNodesAsJson, new TypeReference<List<Node>>(){});

            for(int j = 0; j< allNodes.size(); j++){
                Node node = allNodes.get(j);
                int nodeIndex = nodesNames.indexOf(node.getName());

                nodes.add(node);
                nodesNames.add(node.getName());
                sequence.add(node.getName());
            }

            Path path = new Path(i+1,"1",genomeName,sequence);
            paths.add(path);
        }

        List toReturn = new ArrayList();
        toReturn.add(paths);
        toReturn.add(nodes);
        return mapper.writeValueAsString(toReturn);

    }
}
