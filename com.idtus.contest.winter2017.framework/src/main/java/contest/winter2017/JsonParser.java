package contest.winter2017;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
	private Logger log = LoggerFactory.getLogger(getClass());
	private String jsonFileName;

	public JsonParser(String jsonFilePath) {
		this.jsonFileName = jsonFilePath;
	}

	public Test[] parse() {
		try {
			ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
			// search json file from the classpath with the specified name
			Resource[] mappingLocations = patternResolver.getResources("classpath*:**/" + jsonFileName);
			if (mappingLocations != null && mappingLocations.length > 0) {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(mappingLocations[0].getInputStream(), Test[].class);
			}
		} catch (Exception e) {
			log.error("Failed to parse json file:", e);
		}
		return null;
	}
}
