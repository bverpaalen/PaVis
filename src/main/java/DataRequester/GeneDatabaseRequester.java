package DataRequester;

import Objects.Node;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Path;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class GeneDatabaseRequester {
    Properties properties = new Properties();

    /**
     * Loads property file
     */
    public GeneDatabaseRequester(){
        try {
            InputStream inputStream;
            inputStream = getClass().getClassLoader().getResourceAsStream("neo4j.properties");

            if(inputStream != null){
                this.properties.load(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param geneName gene name to be requested
     * @param genomeId id to specify genome
     * @return gene object with information gene
     */
    public List getGene(String geneName,String genomeId){
        List gene = new ArrayList();
        StatementResult result;

        Driver driver = GraphDatabase.driver(properties.getProperty("url"), AuthTokens.basic(properties.getProperty("username"),properties.getProperty("password")));
        Session session = driver.session();

        result = session.run("Match(g:gene) where g.name = '"+geneName+"' and g.address[0] = "+genomeId+" return g.name as name,g.address as address,g.annotation_node_id as annotation_node_id");

        while(result.hasNext()){
            Record record = result.next();
            gene.add(record.asMap());
        }

        if (gene.size() > 1){
            System.out.println(gene.size());
        }

        session.close();
        driver.close();

        return gene;
    }

    /**
     * Get genes from specific genome number
     * @param genomeNumber genome number to request
     * @return k-mers
     */
    public List AllGenesFromGenome(String genomeNumber){
        List genes = new ArrayList();
        StatementResult result;

        Driver driver = GraphDatabase.driver(properties.getProperty("url"), AuthTokens.basic(properties.getProperty("username"),properties.getProperty("password")));
        Session session = driver.session();

        result = session.run("match(g:gene) where g.address[0]="+genomeNumber+" return id(g) as id,g.name as name,g.address as address, g.annotation_node_id as annotation_node_id order by g.address[3]");

        while(result.hasNext()){
            Record record = result.next();
            genes.add(record.asMap());
        }

        session.close();
        driver.close();

        return genes;
    }

    /**
     * uses gene name to get k-mers
     * this function is temporarily until a better data request is made
     * @param gene genename
     * @param sequence genome id of gene
     * @return k-mers of gene
     */
    public List<Node> GeneToKmers(String gene, String sequence) {
        List kmers = new ArrayList();
        StatementResult result;

        Driver driver = GraphDatabase.driver(properties.getProperty("url"), AuthTokens.basic(properties.getProperty("username"),properties.getProperty("password")));
        Session session = driver.session();

        result = session.run("MATCH (g:gene)-[:starts]->(startN:node),(g:gene)-[:stops]->(stopN:node),p=shortestPath((startN)-[*]->(stopN)) where g.name=\""+gene+"\" and g.address[0] = "+sequence+" and all(r IN relationships(p) where any(a in r.a"+sequence+"_1 where a >= g.address[2]-20 and a <= g.address[3]+20)) return p as node;",Values.parameters("gene",gene,"sequence",sequence,"relation","r.a"+sequence+"_1"));

        while(result.hasNext()){
            Record record = result.next();
            Path path = record.get(0).asPath();
            Iterable<org.neo4j.driver.v1.types.Node> nodes = path.nodes();
            Iterator<org.neo4j.driver.v1.types.Node> nodesItterator = nodes.iterator();
            while(nodesItterator.hasNext()){
                kmers.add(nodesItterator.next().get("sequence"));
            }
        }

        session.close();
        driver.close();

        return kmers;
    }

    /**
     *
     * @param genes list of gene names
     * @param genome genome of genes
     * @return ids of genes
     */
    public List<String> GenesToGeneIds(List<String> genes,String genome) {
        StatementResult result;
        List<String> geneIds = new ArrayList();

        Driver driver = GraphDatabase.driver(properties.getProperty("url"), AuthTokens.basic(properties.getProperty("username"),properties.getProperty("password")));
        Session session = driver.session();

        result = session.run("MATCH(g:gene) where g.address[0]=$genome and g.name in $genes return id(g)",Values.parameters("genome",genome,"genes",genes));

        while(result.hasNext()){
            Record record = result.next();
            geneIds.add(record.get(0).toString());
        }

        return geneIds;
    }
}
