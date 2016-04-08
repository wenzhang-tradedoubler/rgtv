package com.tradedoubler.cdt;

import com.datastax.driver.core.*;
import com.google.common.collect.Sets;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Note: You need to have a tunnel to Cassandra!
 * UAT: tdaccapp05:9042
 * DevTest: tdtestapp01,tdtestapp02:9042
 *
 * @author qinwa
 */
public class UserJourneyReader extends Thread {
    private final Logger LOGGER = getLogger(UserJourneyReader.class);
    private static final String SELECT = "Select * from ExtKeyToUserId where extkey = ?";

    private final Session idMapperSession;

//    private final Set<ExternalKey> externalKeys = Sets.newHashSet();
    private final PreparedStatement idMapperSelect;
    private final IUserJourneyListener listener;
    private final String extkey;

    private volatile boolean running;
    public UserJourneyReader(IUserJourneyListener listener, String cassandraHosts, int cassandraPort, String extkey) {
        Cluster cluster = Cluster.builder().withoutMetrics().addContactPoints(cassandraHosts).withPort(cassandraPort).build();
        this.idMapperSession = cluster.connect("idmapperstore");
        this.running = true;
        this.idMapperSelect = this.idMapperSession.prepare(SELECT);
        this.listener = listener;
        this.extkey = extkey;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Iterator<Row> iterator = idMapperSession.execute(idMapperSelect.bind(extkey)).iterator();
                int ctr = 0;
                while (iterator.hasNext()) {
                    Row row = iterator.next();
//                    ExternalKey externalKey = convert(row);
//                    externalKeys.add(externalKey);
                    ctr++;
                }
                LOGGER.info("Found " + ctr + " linked devices");
                if (listener != null) {
                    listener.onNumDevices(ctr);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private ExternalKey convert(Row row) {
        ExternalKey externalKey = new ExternalKey();
        externalKey.extKey = row.getString("extkey");
        externalKey.source = row.getLong("source");
        externalKey.extId = row.getString("id");
        externalKey.idType = row.getInt("idType");
        return externalKey;
    }

    public void shutdown() {
        running = false;
        idMapperSession.shutdown();
    }

//    public static void main(String[] args) throws InterruptedException {
//        UserJourneyReader reader = new UserJourneyReader(null, "localhost", 9042, "CDT_RegressionTest_ExtId_1");
//        reader.start();
//        reader.join();
//    }

}
