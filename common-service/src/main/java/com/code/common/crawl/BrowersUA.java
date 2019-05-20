package com.code.common.crawl;

import com.code.common.utils.RandomUtils;

public enum BrowersUA {
    //"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
//    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24",
//    "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24"
    FIREFOX(
            "firefox",
            new String[]{
                    "(X11; Linux i686; rv:",
                    "(X11; Linux i586; rv:",
                    "(Windows NT 6.1; WOW64; rv:",
                    "(Windows NT 6.2; WOW64; rv:",
                    "(Macintosh; U; Intel Mac OS X 10.10; rv:",
                    "(Macintosh; Intel Mac OS X 10.13; rv:",
            },
            new String[]{
                    "64.0", "63.0", "62.0", "61.0"
            }) {
        @Override
        public String randomUa() {
            String version = RandomUtils.nextObj(getVersion());
            return "Mozilla/5.0 " + RandomUtils.nextObj(getBrowersInfo()) + version + ") Gecko/20100101 Firefox/" + version;
        }
    },
    CHROME(
            "chrome",
            new String[]{
                    "(Windows NT 6.1; WOW64) AppleWebKit/537.1",
                    "(Windows NT 6.1; WOW64) AppleWebKit/536.6",
                    "(Windows NT 6.2) AppleWebKit/536.6",
                    "(Windows NT 6.2; WOW64) AppleWebKit/537.1",
                    "(X11; CrOS i686 2268.111.0) AppleWebKit/536.11",
                    "(X11; Linux x86_64) AppleWebKit/536.5",
                    "(Windows NT 6.0) AppleWebKit/536.5",
                    "(Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3",
            },
            new String[]{
                    "22.0.1207.1", "20.0.1132.57", "20.0.1092.0", "20.0.1090.0"
            }) {
        @Override
        public String randomUa() {
            return "Mozilla/5.0 " + RandomUtils.nextObj(getBrowersInfo()) + " (KHTML, like Gecko) Chrome/" + RandomUtils.nextObj(getVersion()) + " Safari/536.6";
        }
    },
    OPERA(
            "opera",
            new String[]{
                    "(Windows NT 6.1; U; en-GB)",
                    "(Windows NT 6.1; U; zh-cn)",
                    "(X11; Linux x86_64; U; pl)",
            },
            new String[]{
                    "11.00", "11.11"
            }) {
        @Override
        public String randomUa() {
            return "Opera/9.80 " + RandomUtils.nextObj(getBrowersInfo()) + " Presto/2.7.62 Version/" + RandomUtils.nextObj(getVersion());
        }
    };

    private String type;
    private String[] browersInfo;
    private String[] version;

    public abstract String randomUa();

    BrowersUA(String type, String[] browersInfo, String[] version) {
        this.type = type;
        this.browersInfo = browersInfo;
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getBrowersInfo() {
        return browersInfo;
    }

    public void setBrowersInfo(String[] browersInfo) {
        this.browersInfo = browersInfo;
    }

    public String[] getVersion() {
        return version;
    }

    public void setVersion(String[] version) {
        this.version = version;
    }
}
