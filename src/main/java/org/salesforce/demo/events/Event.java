package org.salesforce.demo.events;

import org.apache.avro.generic.GenericRecord;

public class Event {
	private ChangeEventHeader header;
	private long replayId;
	private GenericRecord payload;
	
	public Event(ChangeEventHeader header, long replayId, GenericRecord payload) {
		super();
		this.header = header;
		this.replayId = replayId;
		this.payload = payload;
	}
	
	public ChangeEventHeader getHeader() {
		return header;
	}
	
	public long getReplayId() {
		return replayId;
	}

	public GenericRecord getPayload() {
		return payload;
	}
	
	public String toString() {
		return this.payload.toString();
	}
}
