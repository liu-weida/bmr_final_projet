package org.rhw.user.toolkit;


import org.rhw.user.common.convention.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalUserGroupIdGenerator {

    private final RedisUserGroupIdGenerator redisShortLinkIdGenerator;

    // 当前号段的起始值
    private long currentSegmentStart;

    // 当前局部计数
    private long localCounter;

    // 局部最大值
    private static final long SEGMENT_SIZE = 10000L;

    @Autowired
    public LocalUserGroupIdGenerator(RedisUserGroupIdGenerator redisShortLinkIdGenerator) {
        this.redisShortLinkIdGenerator = redisShortLinkIdGenerator;
        initializeSegment();
    }

    /**
     * 初始化号段
     */
    private synchronized void initializeSegment() {
        currentSegmentStart = redisShortLinkIdGenerator.getAndIncrement();
        localCounter = 0;
    }

    /**
     * 获取下一个ID
     *
     * @return 返回全局唯一的短链ID
     */
    public synchronized long nextID() {
        if (localCounter >= SEGMENT_SIZE) {
            initializeSegment();
        }
        return currentSegmentStart + localCounter++;
    }

    /**
     * 获取下一个 Base62 编码的短链 ID
     *
     * @return Base62 编码的字符串
     * */
    public synchronized String nextUserGroupID() {
        long nextId = nextID(); // 获取下一个唯一 ID

        if (nextId >= 56800235583L){
            throw new ServiceException("生成id耗尽");
        }

        return Base62Util.convertDecToBase62(nextId); // 转换为 Base62
    }

}



