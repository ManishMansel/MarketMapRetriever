package springRest.springDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import springRest.springDto.TechSubSegment;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GeDaoImpl implements GEDao {
    private static final String GET_ALL_HORIZONTAL = "Select id,name from mtb_horizontal";
    private static final String GET_ALL_TSS = "Select * from mtb_technology_sub_segment";
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public HashMap<Integer, String> fetchAllHorizontals() {
        HashMap<Integer, String> horizontalMap = jdbcTemplate.query(GET_ALL_HORIZONTAL, new ResultSetExtractor<HashMap<Integer, String>>() {
            @Override
            public HashMap<Integer, String> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                HashMap<Integer, String> horizontalMapResult = new HashMap<>();
                while (resultSet.next()) {
                    horizontalMapResult.put(resultSet.getInt("id"), resultSet.getString("name"));
                }
                return horizontalMapResult;
            }
        });
        return horizontalMap;
    }

    @Override
    public HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> fetchAllTechSubSegment() {
        HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> technologyMap = jdbcTemplate.query(GET_ALL_TSS, new ResultSetExtractor<HashMap<Integer, HashMap<Integer, List<TechSubSegment>>>>() {
            @Override
            public HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                HashMap<Integer, HashMap<Integer, List<TechSubSegment>>> techMap = new HashMap<>();
                while (resultSet.next()) {
                    TechSubSegment techSubSegment = new TechSubSegment();
                    techSubSegment.setId(resultSet.getInt("id"));
                    techSubSegment.setName(resultSet.getString("segment_name"));
                    techSubSegment.setParentTechId(resultSet.getInt("parent_id"));
                    techSubSegment.setParentHorizontalId(resultSet.getInt("technology_id"));
                    techMap.putIfAbsent(resultSet.getInt("technology_id"), new HashMap<>());
                    techMap.get(resultSet.getInt("technology_id")).putIfAbsent(resultSet.getInt("parent_id"), new ArrayList<>());
                    techMap.get(resultSet.getInt("technology_id")).get(resultSet.getInt("parent_id")).add(techSubSegment);
                }
                return techMap;
            }
        });
        return technologyMap;
    }

    @Override
    public Long addToTechSubSegmentMap(Integer horizontalId, Integer technologyId, String name) throws Exception {

        try {
            String query = "Insert into mtb_technology_sub_segment(segment_name,parent_id,technology_id) values(:name,:tId,:hId)";
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("name", name)
                    .addValue("tId", technologyId)
                    .addValue("hId", horizontalId);
            KeyHolder holder = new GeneratedKeyHolder();

            jdbcTemplate.update(query, parameters, holder, new String[]{"ID"});
            return holder.getKey().longValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception occured for insert");
        }

    }

    @Override
    public String deleteFromTechSubSegmentMap(Integer id) throws Exception {
        try{

            Map namedParameters = new HashMap();
            namedParameters.put("id", id);
            String deleteQuery = "delete from mtb_technology_sub_segment where id = :id";
            jdbcTemplate.update(deleteQuery, namedParameters);
            return "Success";
        }
        catch (Exception e){
            throw new Exception("Exception occurred for delete");
        }

    }
    @Override
    public String updateTechSubSegmentMap(Integer id,String name) throws Exception {
        try{
            String query = "select count(*) from mtb_technology_sub_segment where id = :id";
            Map namedParameters = new HashMap();
            namedParameters.put("id", id);
            Integer count = jdbcTemplate.queryForObject(query, namedParameters, Integer.class);
            if(count==0){
                return "No such record Found";
            }
            else{
                namedParameters.put("name", name);
                String deleteQuery = "update mtb_technology_sub_segment set segment_name = :name where id = :id";
                jdbcTemplate.update(deleteQuery, namedParameters);
                return "Success";
            }
        }
        catch (Exception e){
            throw new Exception("Exception occurred for Update");
        }

    }

}
