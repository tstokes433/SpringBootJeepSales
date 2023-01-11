package com.promineotech.jeep.controller.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

public class FetchJeepTestSupport extends BaseTest {   //Week 13
  
	protected List<Jeep> buildExpected() {
		List<Jeep> list = new LinkedList<>();
		
		//@formatter:off
		list.add(Jeep.builder()
				.modelId(JeepModel.WRANGLER)
				.trimLevel("Sport")
				.numDoors(4)
				.wheelSize(17)
				.basePrice(new BigDecimal("31975.00"))
				.build());
		list.add(Jeep.builder()
				.modelId(JeepModel.WRANGLER)
				.trimLevel("Sport")
				.numDoors(2)
				.wheelSize(17)
				.basePrice(new BigDecimal("28475.00"))
				.build());
		//@formatter:on
		
		Collections.sort(list);
		return list;
}

	//INSERT INTO models (model_id, trim_level, num_doors, wheel_size, base_price) VALUES('WRANGLER', 'Sport', 4, 17, 31975.00);
	/**
	 * 
	 * @param error
	 * @param status
	 */

	protected void assertErrorMessageValid(Map<String, Object> error, 
			HttpStatus status) {
		//@formatter:off
		assertThat(error)
			.containsKey("message")
			.containsEntry("status code", status.value())
			.containsEntry("uri", "/jeeps")
			.containsKey("timestamp")
			.containsEntry("reason", status.getReasonPhrase());
		//@formatter:on
	}
	}
