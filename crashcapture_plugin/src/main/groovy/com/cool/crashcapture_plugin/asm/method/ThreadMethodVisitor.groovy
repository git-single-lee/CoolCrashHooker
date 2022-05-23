package com.cool.crashcapture_plugin.asm.method

import com.cool.crashcapture_core.PluginConstant
import javafx.util.Pair
import org.objectweb.asm.Label
import com.cool.crashcapture_plugin.utils.ClassVisiterUtil;
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
class ThreadMethodVisitor extends AdviceAdapter {
    private Label label0 = new Label();
    private Label label1 = new Label();
    private Label label2 = new Label();
    private Label label3 = new Label();

    private int classLabel = 0;

    // 方法返回值类型描述符
    private String methodDesc;

    ThreadMethodVisitor(int classLabel, int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor)
        this.classLabel = classLabel;
        this.methodDesc = descriptor;
    }

    @Override
    void visitCode() {
        super.visitCode()
        println("ThreadClassVisitor===> visitCode start")
        mv.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
        mv.visitLabel(label0);
        println("ThreadClassVisitor===> visitCode ends")
    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        println("ThreadClassVisitor===> visitMaxs start")
//        if (classLabel == 2) { // AsyncTask
            mv.visitLabel(label1);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label2);
            mv.visitVarInsn(ASTORE, 2);
            mv.visitMethodInsn(INVOKESTATIC, "com/cool/crashcapture_core/core/CrashCaptureDoor", "getInstance", "()Lcom/cool/crashcapture_core/core/CrashCaptureDoor;", false);
            mv.visitLdcInsn("");
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/cool/crashcapture_core/core/CrashCaptureDoor", "uploadThreadError", "(Ljava/lang/String;Ljava/lang/Exception;)V", false);

            Pair<Integer, Integer> defaultVo = ClassVisiterUtil.getDefaultByDesc(methodDesc);
            int value = defaultVo.getKey();
            int opcode = defaultVo.getValue();
            if (value >= 0) {
                mv.visitInsn(value);
            }
            mv.visitInsn(opcode);
//        } else {
//            mv.visitLabel(label1);
//            mv.visitJumpInsn(GOTO, label3);
//            mv.visitLabel(label2);
//            mv.visitVarInsn(ASTORE, 1);
//            mv.visitMethodInsn(INVOKESTATIC, PluginConstant.CRASH_CAPUTURE_DOOR, "getInstance", "()L" + PluginConstant.CRASH_CAPUTURE_DOOR + ";", false);
//            mv.visitLdcInsn("");
//            mv.visitVarInsn(ALOAD, 1);
//            mv.visitMethodInsn(INVOKEVIRTUAL, PluginConstant.CRASH_CAPUTURE_DOOR, "uploadThreadError", "(Ljava/lang/String;Ljava/lang/Exception;)V", false);
//            mv.visitLabel(label3); // catch 结束
//            mv.visitInsn(RETURN);
//        }

        println("ThreadClassVisitor===> visitMaxs before end")
        super.visitMaxs(maxStack, maxLocals);
    }


}