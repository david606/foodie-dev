package org.n3r.idworker;

import org.n3r.idworker.strategy.DefaultWorkerIdStrategy;
import org.n3r.idworker.utils.Utils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Sid {
    private static WorkerIdStrategy workerIdStrategy;
    private static IdWorker idWorker;

    static {
        configure(DefaultWorkerIdStrategy.instance);
    }


    /**
     * 使用自定义的 WorkerIdStrategy 配置 IdWorker
     * 如果当前已有 workerIdStrategy 实例，则首先调用其 release 方法进行资源释放
     * 然后使用新的 custom WorkerIdStrategy 创建 IdWorker 实例
     * 该方法使用了同步关键字，确保线程安全，避免并发问题
     *
     * @param custom 自定义的 WorkerIdStrategy，用于生成 workerId
     */
    public static synchronized void configure(WorkerIdStrategy custom) {
        // 如果当前已有 workerIdStrategy 实例，则调用其 release 方法进行资源释放
        if (workerIdStrategy != null) workerIdStrategy.release();
        // 使用新的 custom WorkerIdStrategy 更新 workerIdStrategy 实例
        workerIdStrategy = custom;
        // 使用 workerIdStrategy 的 availableWorkerId 方法创建新的 IdWorker 实例
        idWorker = new IdWorker(workerIdStrategy.availableWorkerId()) {
            /**
             * 重写 getEpoch 方法，返回每天午夜的毫秒数
             * 这样可以确保 IdWorker 使用每天午夜作为时间起始点
             *
             * @return 每天午夜的毫秒数
             */
            @Override
            public long getEpoch() {
                return Utils.midnightMillis();
            }
        };
    }


    /**
     * 一天最大毫秒86400000，最大占用27比特
     * 27+10+11=48位 最大值281474976710655(15字)，YK0XXHZ827(10字)
     * 6位(YYMMDD)+15位，共21位
     *
     * @return 固定21位数字字符串
     */

    public static String next() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + String.format("%014d", id);
    }


    /**
     * 返回固定16位的字母数字混编的字符串。
     */
    public String nextShort() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + Utils.padLeft(Utils.encode(id), 10, '0');
    }
    
    public static void main(String[] args) {
		String aa = new Sid().nextShort();
		String bb =Sid.next();

		System.out.println(aa);
		System.out.println(bb);
	}
}
