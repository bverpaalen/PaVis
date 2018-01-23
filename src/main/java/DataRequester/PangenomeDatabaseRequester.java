package DataRequester;

import org.neo4j.driver.v1.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PangenomeDatabaseRequester {
    int i = 0;
    Properties properties = new Properties();

    public PangenomeDatabaseRequester(){
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
     * get k-mers genome
     * @param genomeName Name of gene
     * @return
     */
    public List getSpecificGenome(String genomeName){
        List genome = new ArrayList();
        StatementResult result;

        Driver driver = GraphDatabase.driver(properties.getProperty("url"), AuthTokens.basic( properties.getProperty("username"), properties.getProperty("password")));
        Session session = driver.session();

        result = session.run("MATCH (n)-[r]->() where exists (r."+genomeName+") and (n:node) RETURN id(n) as name ,n.sequence as seq order by r."+genomeName);

        while(result.hasNext()){
            Record record = result.next();
            genome.add(record.asMap());
        }

        session.close();
        driver.close();

        return genome;
    }

}
