package com.kun.blog.util;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveProcessor;

/**
 * 自定义敏感词*号替代处理器
 *
 * @author gzc
 * @since 2023/2/13 12:38
 */
public class SensitiveDefaultProcessor implements SensitiveProcessor {

    @Override
    public String process(FoundWord foundWord) {
        int length = foundWord.getFoundWord().length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("*");
        }
        return sb.toString();
    }
}
