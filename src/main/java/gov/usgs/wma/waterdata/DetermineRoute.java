package gov.usgs.wma.waterdata;

import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetermineRoute implements Function<RequestObject, ResultObject> {
	private static final Logger LOG = LoggerFactory.getLogger(DetermineRoute.class);

	private DataDao dataDao;
	// Router Types
	public static final String ERROR = "error";
	public static final String OTHER = "other";
	public static final String TS_DV_STAT = "tsDailyValueStatisticalTransform";

	@Autowired
	public DetermineRoute(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public ResultObject apply(RequestObject request) {
		return processRequest(request);
	}

	protected ResultObject processRequest(RequestObject request) {
		ResultObject result = new ResultObject();
		result.setDataType(determineType(request.getUniqueId()));
		return result;
	}

	protected String determineType(String uniqueId) {
		String type = ERROR;
		Map<String, Object> data = dataDao.getDataTypeRouting(uniqueId);
		LOG.debug("data: {}", data);
		if (null != data) {
			switch (data.get(DataDao.DATA_TYPE).toString()) {
			case TS_DV_STAT:
				type = TS_DV_STAT;
				break;
			default:
				type = OTHER;
				break;
			}
		} else {
			type = OTHER;
		}
		return type;
	}
}
