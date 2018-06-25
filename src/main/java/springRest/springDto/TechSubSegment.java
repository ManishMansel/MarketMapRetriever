package springRest.springDto;

public class TechSubSegment {
    private Integer id;
    private String name;
    private Integer parentTechId ;
    private Integer parentHorizontalId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentTechId() {
        return parentTechId;
    }

    public void setParentTechId(Integer parentTechId) {
        this.parentTechId = parentTechId;
    }

    public Integer getParentHorizontalId() {
        return parentHorizontalId;
    }

    public void setParentHorizontalId(Integer parentHorizontalId) {
        this.parentHorizontalId = parentHorizontalId;
    }
}
