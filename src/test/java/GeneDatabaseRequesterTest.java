import DataRequester.GeneDatabaseRequester;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GeneDatabaseRequesterTest {
    GeneDatabaseRequester GDR = new GeneDatabaseRequester();

    @Test
    public void GenesToGeneIdsTest(){
        List<String> genes = new ArrayList();

        genes.add("\"rpoC2\"");
        genes.add("\"psbK\"");

        GDR.GenesToGeneIds(genes,"1");
    }
}
