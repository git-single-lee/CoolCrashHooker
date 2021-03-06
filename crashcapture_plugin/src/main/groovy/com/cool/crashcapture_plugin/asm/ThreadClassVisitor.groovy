package com.cool.crashcapture_plugin.asm;

import com.cool.crashcapture_plugin.asm.method.ThreadMethodVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
public class ThreadClassVisitor extends ClassVisitor implements Opcodes {
    private int maybeThread = 0;
    private String name = "";
    public Map<String, List<String>> classToMethods = new HashMap<>();

    public ThreadClassVisitor(int api) {
        super(api);
    }

    public ThreadClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.maybeThread = tryInjectClass(version, access, name, signature, superName, interfaces);
        this.name = name
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (maybeThread != 0) {
            boolean maybeMethod = tryInjectMethod(name, descriptor);
            if (maybeMethod) {
                println("ThreadClassVisitor===> inject")
                return new ThreadMethodVisitor(maybeThread, Opcodes.ASM7, mv, access, name, descriptor);
            }
        }
        return mv;
    }

    public int tryInjectClass(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (interfaces != null) {
            for (String item : interfaces) {
                if (item.equals("java/lang/Runnable")) {
                    return 1;
                }
            }
        }
        if ("android/os/AsyncTask".equals(superName)) {
            return 2;
        }
        if ("android/os/Handler".equals(superName)) {
            return 3;
        }
        if ("java/lang/Thread".equals(superName)) {
            return 4;
        }
        return 0;
    }

    public boolean tryInjectMethod(String methodName, String methodDesc) {
        println("ThreadClassVisitor===> try inject: className: ${name} methodName: ${methodName} methodDesc:${methodDesc}" )
        if (methodName.equals("run"))
            return true;
        if (methodName.equals("doInBackground"))
            return true;
        if (methodName.equals("handleMessage")) {
            return true;
        }
        return false;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
