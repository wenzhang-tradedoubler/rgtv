package com.tradedoubler.cdt;

/**
 * @author qinwa
 */
public class ExternalKey {
    public String extKey; // The unique key in org.
    public long source; // 0 org id, 1 email
    public String extId; //GUID or device Id
    public int idType; // 0 GUID, 1 DeviceId

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ExternalKey)) {
            return false;
        }

        ExternalKey other = (ExternalKey) o;
        return this.extKey.equals(other.extKey) && this.source == other.source && this.extId.equals(other.extId) && this.idType == other.idType;
    }

    public int hashCode() {
        return 31 + extKey.hashCode() + Long.valueOf(source).hashCode() + extId.hashCode() + idType;
    }

    public String toString() {
        return source + "-" + extKey + "-" + extId + "-" + idType;
    }
}
