package com.cool.crashcapture_plugin.extension;

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
public class ThreadStateExtension {
    // relay to parent
    public Map<String, List<String>> parentToMethods = new HashMap<>();
    // relay to subClass
    public Map<String, List<String>> classToMethods = new HashMap<>();

    public void defaultValues() {
//        classToMethods.put("")
    }
}
