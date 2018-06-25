package springRest;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springRest.springDao.GEDao;
import springRest.springDto.MarketMapTree;
import springRest.springDto.TechSubSegment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @Autowired
    GEDao geDao;

    @RequestMapping("/h")
    String hello() {
        return "Hello World!";
    }

    @Data
    static class Result {
        private final int left;
        private final int right;
        private final long answer;
    }

    // SQL sample
    @RequestMapping(value = "/fetchMarketMap", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    List<MarketMapTree> fetchMarketMap() {
        HashMap<Integer, String> horizontalMap = geDao.fetchAllHorizontals();
        HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> techMap = geDao.fetchAllTechSubSegment();
        List<MarketMapTree> marketMapTreeList = new ArrayList<>();
        populateHorizontalMap(horizontalMap, marketMapTreeList);
        populateTechMap(techMap, marketMapTreeList);
        return marketMapTreeList;
    }
    @RequestMapping(value = "/addToMarketMap", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    List<MarketMapTree> addToMarketMap(@RequestParam Integer horizontalId,@RequestParam Integer technologyId,@RequestParam String name) throws Exception {
        geDao.addToTechSubSegmentMap(horizontalId,technologyId,name);
        return fetchMarketMap();

    }
    @RequestMapping(value = "/deleteFromMarketMap", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    List<MarketMapTree> deleteFromMarketMap(@RequestParam Integer id) throws Exception {
        geDao.deleteFromTechSubSegmentMap(id);
        return fetchMarketMap();

    }
    private void populateTechMap(HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> techMap, List<MarketMapTree> marketMapTreeList) {
        for (MarketMapTree marketMapTree : marketMapTreeList) {
            HashMap<Integer, List<TechSubSegment>> techminiMap = techMap.get(marketMapTree.getId().get(0));
            Integer parentPoint = 0;
            recursivePopulateMarketMapTree(marketMapTree, techminiMap, parentPoint);
        }
    }

    private void recursivePopulateMarketMapTree(MarketMapTree marketMapTree, HashMap<Integer, List<TechSubSegment>> techminiMap, Integer parentPoint) {
       if(techminiMap!=null && techminiMap.get(parentPoint)!=null) {
           for (TechSubSegment techSubSegment : techminiMap.get(parentPoint)) {
               MarketMapTree subMarketMapTree = new MarketMapTree();
               List<Integer> deepId = new ArrayList<>();
               deepId.addAll(marketMapTree.getId());
               if (parentPoint == 0) {
                   deepId.add(0);
               }
               deepId.add(techSubSegment.getId());
               subMarketMapTree.setId(deepId);
               subMarketMapTree.setItem(techSubSegment.getName());
               marketMapTree.getChildren().add(subMarketMapTree);

               recursivePopulateMarketMapTree(subMarketMapTree, techminiMap, techSubSegment.getId());
           }
       }
    }

    private void populateHorizontalMap(HashMap<Integer, String> horizontalMap, List<MarketMapTree> marketMapTreeList) {
        for (Map.Entry<Integer, String> horizontalEntry : horizontalMap.entrySet()) {
            MarketMapTree marketMapTree = new MarketMapTree();
            List<Integer> idList = new ArrayList<>();
            idList.add(horizontalEntry.getKey());
            marketMapTree.setId(idList);
            marketMapTree.setItem(horizontalEntry.getValue());
            marketMapTreeList.add(marketMapTree);
        }
    }
}
