import DataProcessing.KmerProcessing;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class KmerProcessingTest {
    KmerProcessing KP = new KmerProcessing();

    @Test
    public void RequestKmersTest(){
        List<String> genes = new ArrayList();
        List<String> genomes = new ArrayList<>();
        genomes.add("1");
        KP.RequestKmers("matK",genomes);
    }
}
