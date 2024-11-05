package uk.tw.energy.exceptions;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
public class RecordNotFoundException extends RuntimeException {

    private final String recordId;

    public RecordNotFoundException(String recordId) {
        super();
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;
    }
}
