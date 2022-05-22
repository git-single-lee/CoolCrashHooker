package com.cool.crashcapture_plugin.utils;

import org.objectweb.asm.Opcodes;

import java.io.File;

import javafx.util.Pair;

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
public final class ASMUtil {

    public static Pair<Integer, Integer> getDefaultByDesc(String methodDesc) {
        Pair<Integer, Integer> pair = null;
        int value = -1;
        int opcode = -1;

        if (methodDesc.endsWith("[Z") ||
                methodDesc.endsWith("[I") ||
                methodDesc.endsWith("[S") ||
                methodDesc.endsWith("[B") ||
                methodDesc.endsWith("[C")) {
            value = Opcodes.ACONST_NULL;
            opcode = Opcodes.ARETURN;

        } else if (methodDesc.endsWith("Z") ||
                methodDesc.endsWith("I") ||
                methodDesc.endsWith("S") ||
                methodDesc.endsWith("B") ||
                methodDesc.endsWith("C")) {
            value = Opcodes.ICONST_0;
            opcode = Opcodes.IRETURN;

        } else if (methodDesc.endsWith("J")) {
            value = Opcodes.LCONST_0;
            opcode = Opcodes.LRETURN;

        } else if (methodDesc.endsWith("F")) {
            value = Opcodes.FCONST_0;
            opcode = Opcodes.FRETURN;

        } else if (methodDesc.endsWith("D")) {
            value = Opcodes.DCONST_0;
            opcode = Opcodes.DRETURN;

        } else if (methodDesc.endsWith("V")) {
            opcode = Opcodes.RETURN;

        } else {
            value = Opcodes.ACONST_NULL;
            opcode = Opcodes.ARETURN;
        }

        pair = new Pair<>(value, opcode);
        return pair;
    }

    /**
     * aaa.bbb.ccc -> aaa/bbb/ccc
     */
    public static String className2FullName(String name, boolean force) {
        return name.replace(".", force ? "/" : File.separator);
    }

    /**
     * aaa/bbb/ccc -> aaa.bbb.ccc
     */
    public static String fullName2ClassName(String name) {
        return name.replace(File.separator, ".");
    }

}
