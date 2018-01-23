import Pantools.pantools.Pantools;
import org.junit.jupiter.api.Test;

public class TestPantools {


    Pantools pantools = new Pantools();

    public void RecieveGeneTest(){
        pantools.RetrieveKmersGene("test","/home/bverpaalen/Desktop/pantoolsTest/data/chloroplast_DB_java/");

    }
}
