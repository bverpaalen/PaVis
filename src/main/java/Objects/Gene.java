package Objects;

import java.util.List;

public class Gene {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getAddress() {
        return address;
    }

    public void setAddress(List<Integer> address) {
        this.address = address;
    }

    public int getAnnotation_node_id() {
        return annotation_node_id;
    }

    public void setAnnotation_node_id(int annotation_node_id) {
        this.annotation_node_id = annotation_node_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String name;
    List<Integer> address;
    int annotation_node_id;

}
