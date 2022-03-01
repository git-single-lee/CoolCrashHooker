package com.cool.crashcapture_plugin.asm.method

import com.cool.crashcapture_core.PluginConstant
import org.objectweb.asm.Label
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

    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of {@link
     *     Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name the method's name.
     * @param descriptor the method's descriptor (see {@link Type Type}).
     */
    ThreadMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor)
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
        mv.visitLabel(label1);
        mv.visitJumpInsn(GOTO, label3);
        mv.visitLabel(label2);
        mv.visitVarInsn(ASTORE, 1);
        mv.visitMethodInsn(INVOKESTATIC, PluginConstant.CRASH_CAPUTURE_DOOR, "getInstance", "()L" + PluginConstant.CRASH_CAPUTURE_DOOR + ";", false);
        mv.visitLdcInsn("");
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, PluginConstant.CRASH_CAPUTURE_DOOR, "uploadThreadError", "(Ljava/lang/String;Ljava/lang/Exception;)V", false);
        mv.visitLabel(label3); // catch 结束
        mv.visitInsn(RETURN);
        println("ThreadClassVisitor===> visitMaxs before end")
        super.visitMaxs(maxStack, maxLocals);
    }
}