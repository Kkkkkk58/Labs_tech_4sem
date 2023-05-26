package ru.kslacker.cats.microservices.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@JsonComponent
public class PageableDeserializer extends JsonDeserializer<Pageable> {

	@Override
	public Pageable deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

		Sort sort = Sort.unsorted();
		JsonNode node = p.getCodec().readTree(p);
		int pageNumber = node.get("pageNumber").asInt();
		int pageSize = node.get("pageSize").asInt();

		if (isSortValid(node)) {
			JsonNode sortNode = node.get("sort");
			Iterator<JsonNode> iterator = sortNode.iterator();
			List<Order> sortingOrders = new ArrayList<>();
			while (iterator.hasNext()) {
				JsonNode next = iterator.next();
				String property = next.findValue("property").asText();
				String direction = next.findValue("direction").asText();
				sortingOrders.add(new Order(Direction.valueOf(direction), property));
			}
			sort = Sort.by(sortingOrders);
		}

		return PageRequest.of(pageNumber, pageSize, sort);
	}

	private boolean isSortValid(JsonNode node) {

		return node.get("sort") != null && !node.get("sort").isNull() && node.get("sort").isArray();
	}
}