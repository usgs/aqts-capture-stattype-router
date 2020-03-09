package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment=WebEnvironment.NONE)
public class DetermineRouteTest {

	@MockBean
	private DataDao dataDao;
	private DetermineRoute determineRoute;
	private RequestObject request;
	private Map<String, Object> data;

	@BeforeEach
	public void beforeEach() {
		determineRoute = new DetermineRoute(dataDao);
		data = new HashMap<>();
		request = new RequestObject();
		request.setId(1l);
		request.setUniqueId("dEf");
	}

	@Test
	public void notFoundAtAllTest() throws IOException {
		when(dataDao.getDataTypeRouting(anyString())).thenReturn(null);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(DetermineRoute.OTHER, result.getType());
	}

	@Test
	public void foundUnknownTest() throws IOException {
		data.put(DataDao.DATA_TYPE, "value");
		when(dataDao.getDataTypeRouting(anyString())).thenReturn(data);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(DetermineRoute.OTHER, result.getType());
	}

	@Test
	public void foundTSDescriptionTest() throws IOException {
		data.put(DataDao.DATA_TYPE, DetermineRoute.TS_DV_STAT);
		when(dataDao.getDataTypeRouting(anyString())).thenReturn(data);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(DetermineRoute.TS_DV_STAT, result.getType());
	}
}
