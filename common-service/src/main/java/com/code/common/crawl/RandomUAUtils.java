package com.code.common.crawl;

import com.code.common.utils.RandomUtils;

public class RandomUAUtils {
    private static BrowersUA[] browersUAs = null;

    public static String getRandomUA(BrowersUA browersUA) {
        if (browersUA == null) {
            browersUA = BrowersUA.FIREFOX;
        }
        return browersUA.randomUa();
    }

    public static String getRandomUA() {
        if (browersUAs == null) {
            browersUAs = BrowersUA.values();
        }
        BrowersUA browersUA = RandomUtils.nextObj(browersUAs);

        return browersUA.randomUa();
    }

}
