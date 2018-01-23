package Objects;

import java.util.List;

public class Path {

    public Path(int id, String genomeNumber, String name, List<String> sequence){
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.genomeNumber = genomeNumber;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSequence() {
        return sequence;
    }

    public void setSequence(List<String> sequence) {
        this.sequence = sequence;
    }

    public String getGenomeNumber() {
        return genomeNumber;
    }

    public void setGenomeNumber(String genomeNumber) {
        this.genomeNumber = genomeNumber;
    }

    int id;
    String genomeNumber;
    String name;
    List<String> sequence;
}
