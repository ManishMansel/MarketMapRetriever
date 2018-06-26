package springRest.springDao;

import springRest.springDto.TechSubSegment;

import java.util.HashMap;
import java.util.List;

public interface GEDao {

    HashMap<Integer, String> fetchAllHorizontals();

    HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> fetchAllTechSubSegment();

    Long addToTechSubSegmentMap(Integer horizontalId, Integer technologyId, String name) throws Exception;

    String deleteFromTechSubSegmentMap(Integer id) throws Exception;
    String updateTechSubSegmentMap(Integer id,String name) throws Exception;
}
