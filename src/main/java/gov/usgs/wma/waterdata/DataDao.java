package gov.usgs.wma.waterdata;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class DataDao {
	private static final Logger LOG = LoggerFactory.getLogger(DataDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("classpath:sql/dataTypeRouting.sql")
	private Resource dataTypeRouting;

	public static final String DATA_TYPE = "data_type";

	public Map<String, Object> getDataTypeRouting(String uniqueId) {
		Map<String, Object> rtn = null;
		try {
			String sql = new String(FileCopyUtils.copyToByteArray(dataTypeRouting.getInputStream()));
			rtn =  jdbcTemplate.queryForMap(sql,
					uniqueId
				);
		} catch (EmptyResultDataAccessException e) {
			LOG.info("Couldn't find {} - {}", uniqueId, e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error("Unable to get SQL statement", e);
			throw new RuntimeException(e);
		}
		return rtn;
	}
}
