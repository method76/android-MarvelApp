package com.method76.common.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sungjoon Kim on 2016-02-05.
 */
public class PasswdUtil {

    /**
     * 비밀번호 생성 규칙 검증
     * @param hex
     * @return
     */
    private boolean isValidPattern(String hex, String pwPatternStr) {
        Pattern pattern1 = Pattern.compile(pwPatternStr);
        Matcher matcher1 = pattern1.matcher(hex);
        return matcher1.matches();
    }

    /**
     *
     * @param hex
     * @param pwPatternStr1
     * @param pwPatternStr2
     * @return
     */
    private boolean isValidPattern(String hex, String pwPatternStr1, String pwPatternStr2) {
        Pattern pattern1 = Pattern.compile(pwPatternStr1);
        Matcher matcher1 = pattern1.matcher(hex);
        Pattern pattern2 = Pattern.compile(pwPatternStr2);
        Matcher matcher2 = pattern2.matcher(hex);
        boolean match1   = matcher1.matches();
        boolean match2   = matcher2.matches();
        return (match1||match2);
    }

}
