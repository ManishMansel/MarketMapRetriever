package springRest.springDto;

import java.util.ArrayList;
import java.util.List;

public class MarketMapTree {
    private String item;
    private List<Integer> id;
    private List<MarketMapTree> children = new ArrayList<>();

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public List<MarketMapTree> getChildren() {
        return children;
    }

    public void setChildren(List<MarketMapTree> children) {
        this.children = children;
    }
}
