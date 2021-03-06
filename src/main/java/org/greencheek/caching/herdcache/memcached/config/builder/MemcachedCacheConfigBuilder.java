package org.greencheek.caching.herdcache.memcached.config.builder;

import net.spy.memcached.*;
import net.spy.memcached.transcoders.Transcoder;
import org.greencheek.caching.herdcache.memcached.config.MemcachedCacheConfig;
import org.greencheek.caching.herdcache.memcached.config.hostparsing.CommaSeparatedHostAndPortStringParser;
import org.greencheek.caching.herdcache.memcached.config.hostparsing.HostStringParser;
import org.greencheek.caching.herdcache.memcached.dns.lookup.AddressByNameHostResolver;
import org.greencheek.caching.herdcache.memcached.dns.lookup.HostResolver;
import org.greencheek.caching.herdcache.memcached.keyhashing.KeyHashingType;
import org.greencheek.caching.herdcache.memcached.metrics.MetricRecorder;
import org.greencheek.caching.herdcache.memcached.metrics.NoOpMetricRecorder;
import org.greencheek.caching.herdcache.memcached.spy.extensions.transcoders.FastSerializingTranscoder;
import org.greencheek.caching.herdcache.memcached.spy.extensions.transcoders.compression.Compression;
import org.greencheek.caching.herdcache.memcached.spy.extensions.transcoders.compression.CompressionAlgorithm;
import org.greencheek.caching.herdcache.memcached.spy.extensions.transcoders.compression.LZ4NativeCompression;
import org.greencheek.caching.herdcache.memcached.spy.extensions.transcoders.compression.SnappyCompression;

import java.time.Duration;
import java.util.Optional;

/**
 * Created by dominictootell on 23/08/2014.
 */
public abstract class MemcachedCacheConfigBuilder<T extends MemcachedCacheConfigBuilder<T>> implements CacheConfigBuilder<T> {

    private Duration timeToLive =  Duration.ofSeconds(60);
    private int maxCapacity = 1000;
    private String memcachedHosts = "localhost:11211";
    private ConnectionFactoryBuilder.Locator hashingType  = ConnectionFactoryBuilder.Locator.CONSISTENT;
    private FailureMode failureMode = FailureMode.Redistribute;
    private HashAlgorithm hashAlgorithm = DefaultHashAlgorithm.KETAMA_HASH;
    private Transcoder<Object> serializingTranscoder = new FastSerializingTranscoder();
    private ConnectionFactoryBuilder.Protocol protocol = ConnectionFactoryBuilder.Protocol.BINARY;
    private int readBufferSize = DefaultConnectionFactory.DEFAULT_READ_BUFFER_SIZE;
    private Duration memcachedGetTimeout  = Duration.ofMillis(2500);
    private Duration dnsConnectionTimeout = Duration.ofSeconds(3);
    private boolean waitForMemcachedSet  = false;
    private Duration setWaitDuration = Duration.ofSeconds(2);
    private KeyHashingType keyHashType = KeyHashingType.NATIVE_XXHASH;
    private Optional<String> keyPrefix = Optional.empty();
    private boolean asciiOnlyKeys = false;
    private HostStringParser hostStringParser = new CommaSeparatedHostAndPortStringParser();
    private HostResolver hostResolver = new AddressByNameHostResolver();
    private boolean useStaleCache = false;
    private Duration staleCacheAdditionalTimeToLive = Duration.ZERO;
    private String staleCachePrefix = "stale";
    private int staleMaxCapacity = -1;
    private Duration staleCacheMemachedGetTimeout = Duration.ZERO;
    private boolean removeFutureFromInternalCacheBeforeSettingValue = false;
    private boolean hashKeyPrefix = true;
    private Duration waitForRemove = Duration.ZERO;
    private MetricRecorder metricRecorder = new NoOpMetricRecorder();

    public MemcachedCacheConfig buildMemcachedConfig()
    {
       return new MemcachedCacheConfig(
               timeToLive,
               maxCapacity,memcachedHosts,
               hashingType,
               failureMode,
               hashAlgorithm,serializingTranscoder,
               protocol,readBufferSize,memcachedGetTimeout,
               dnsConnectionTimeout,waitForMemcachedSet,
               setWaitDuration,
               keyHashType,keyPrefix,asciiOnlyKeys,
               hostStringParser,hostResolver,
               useStaleCache,staleCacheAdditionalTimeToLive,staleCachePrefix,
               staleMaxCapacity,staleCacheMemachedGetTimeout,
               removeFutureFromInternalCacheBeforeSettingValue,
               hashKeyPrefix,
               waitForRemove,
               metricRecorder);
    }

    public T setCompressionAlgorithm(CompressionAlgorithm algorithm) {
        switch (algorithm) {
            case NONE:
                serializingTranscoder = new FastSerializingTranscoder(Compression.NONE);
                break;
            case LZ4_NATIVE:
                serializingTranscoder = new FastSerializingTranscoder(new LZ4NativeCompression());
                break;
            case SNAPPY:
                serializingTranscoder = new FastSerializingTranscoder(new SnappyCompression());
                break;
            default:
                serializingTranscoder = new FastSerializingTranscoder();
                break;
        }
        return self();
    }

    public T setWaitForRemove(Duration durationToWaitFor) {
        this.waitForRemove = durationToWaitFor;
        return self();
    }

    public T setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
        return self();
    }

    public T setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return self();
    }

    public T setMemcachedHosts(String memcachedHosts) {
        this.memcachedHosts = memcachedHosts;
        return self();
    }

    public T setHashingType(ConnectionFactoryBuilder.Locator hashingType) {
        this.hashingType = hashingType;
        return self();
    }

    public T setFailureMode(FailureMode failureMode) {
        this.failureMode = failureMode;
        return self();
    }

    public T setHashAlgorithm(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
        return self();
    }

    public T setSerializingTranscoder(Transcoder<Object> serializingTranscoder) {
        this.serializingTranscoder = serializingTranscoder;
        return self();
    }

    public T setProtocol(ConnectionFactoryBuilder.Protocol protocol) {
        this.protocol = protocol;
        return self();
    }

    public T setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
        return self();
    }

    public T setMemcachedGetTimeout(Duration memcachedGetTimeout) {
        this.memcachedGetTimeout = memcachedGetTimeout;
        return self();
    }

    public T setDnsConnectionTimeout(Duration dnsConnectionTimeout) {
        this.dnsConnectionTimeout = dnsConnectionTimeout;
        return self();
    }

    public T setWaitForMemcachedSet(boolean waitForMemcachedSet) {
        this.waitForMemcachedSet = waitForMemcachedSet;
        return self();
    }

    public T setSetWaitDuration(Duration setWaitDuration) {
        this.setWaitDuration = setWaitDuration;
        return self();
    }

    public T setKeyHashType(KeyHashingType keyHashType) {
        this.keyHashType = keyHashType;
        return self();
    }

    public T setKeyPrefix(Optional<String> keyPrefix) {
        this.keyPrefix = keyPrefix;
        return self();
    }

    public T setAsciiOnlyKeys(boolean asciiOnlyKeys) {
        this.asciiOnlyKeys = asciiOnlyKeys;
        return self();
    }

    public T setHostStringParser(HostStringParser hostStringParser) {
        this.hostStringParser = hostStringParser;
        return self();
    }

    public T setHostResolver(HostResolver hostResolver) {
        this.hostResolver = hostResolver;
        return self();
    }

    public T setUseStaleCache(boolean useStaleCache) {
        this.useStaleCache = useStaleCache;
        return self();
    }

    public T setStaleCacheAdditionalTimeToLive(Duration staleCacheAdditionalTimeToLive) {
        this.staleCacheAdditionalTimeToLive = staleCacheAdditionalTimeToLive;
        return self();
    }

    public T setStaleCachePrefix(String staleCachePrefix) {
        this.staleCachePrefix = staleCachePrefix;
        return self();
    }

    public T setStaleMaxCapacity(int staleMaxCapacity) {
        this.staleMaxCapacity = staleMaxCapacity;
        return self();
    }

    public T setStaleCacheMemachedGetTimeout(Duration staleCacheMemachedGetTimeout) {
        this.staleCacheMemachedGetTimeout = staleCacheMemachedGetTimeout;
        return self();
    }

    public T setRemoveFutureFromInternalCacheBeforeSettingValue(boolean removeFutureFromInternalCacheBeforeSettingValue) {
        this.removeFutureFromInternalCacheBeforeSettingValue = removeFutureFromInternalCacheBeforeSettingValue;
        return self();
    }

    public T setHashKeyPrefix(boolean hashKeyPrefix) {
        this.hashKeyPrefix = hashKeyPrefix;
        return self();
    }

    public T setMetricsRecorder(MetricRecorder metricsRecorder) {
        this.metricRecorder = metricsRecorder;
        return self();

    }
}
