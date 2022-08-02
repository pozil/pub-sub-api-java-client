package org.salesforce.demo.events;

import java.util.List;

/**
 * Wrapper class for Change Event Header fields
 * @see https://developer.salesforce.com/docs/atlas.en-us.change_data_capture.meta/change_data_capture/cdc_event_fields_header.htm
 */
public class ChangeEventHeader {
	private String entityName;
	private List<String> recordIds;
	private ChangeType changeType;
	private String changeOrigin;
	private String transactionKey;
	private int sequenceNumber;
	private long commitTimestamp;
	private long commitNumber;
	private String commitUser;
	private List<String> nulledFields;
	private List<String> diffFields;
	private List<String> changedFields;
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<String> getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(List<String> recordIds) {
		this.recordIds = recordIds;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public String getChangeOrigin() {
		return changeOrigin;
	}

	public void setChangeOrigin(String changeOrigin) {
		this.changeOrigin = changeOrigin;
	}

	public String getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(String transactionKey) {
		this.transactionKey = transactionKey;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public long getCommitTimestamp() {
		return commitTimestamp;
	}

	public void setCommitTimestamp(long commitTimestamp) {
		this.commitTimestamp = commitTimestamp;
	}

	public long getCommitNumber() {
		return commitNumber;
	}

	public void setCommitNumber(long commitNumber) {
		this.commitNumber = commitNumber;
	}

	public String getCommitUser() {
		return commitUser;
	}

	public void setCommitUser(String commitUser) {
		this.commitUser = commitUser;
	}

	public List<String> getNulledFields() {
		return nulledFields;
	}

	public void setNulledFields(List<String> nulledFields) {
		this.nulledFields = nulledFields;
	}

	public List<String> getDiffFields() {
		return diffFields;
	}

	public void setDiffFields(List<String> diffFields) {
		this.diffFields = diffFields;
	}

	public List<String> getChangedFields() {
		return changedFields;
	}

	public void setChangedFields(List<String> changedFields) {
		this.changedFields = changedFields;
	}

	public enum ChangeType {
	    CREATE,
	    UPDATE,
	    DELETE,
	    UNDELETE,
	    GAP_CREATE,
	    GAP_UPDATE,
	    GAP_DELETE,
	    GAP_UNDELETE,
	    GAP_OVERFLOW
	}
}
