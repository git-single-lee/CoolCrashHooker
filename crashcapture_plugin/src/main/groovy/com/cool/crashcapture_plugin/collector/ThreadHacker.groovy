package com.cool.crashcapture_plugin.collector

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.cool.crashcapture_plugin.asm.ThreadClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes


/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
public class ThreadHacker {
    public static void collectJavaFileWithDir(DirectoryInput directoryInput, File file, File inDir, File destDir) {
    }

    public static void collectJavaFileWithJar(JarInput jarInput, File file, File jarDest) {
    }

    public static void tryInjectWithClass(DirectoryInput directoryInput, IThreadHacker hackerCallback) {
        println("injectWithClass===> show directoryInput: name: " + directoryInput.name + " contentTypes: " + directoryInput.contentTypes + " scopes: " + directoryInput.scopes)
        // for each in file;
        directoryInput.file.eachFileRecurse {file ->
            println("eachDirRecurse===> all file: " + file.absolutePath)
            if (file.directory || !file.absolutePath.endsWith(".class")) {
                println("eachDirRecurse===> no fit return file: " + file.absolutePath)
            } else {
                println("eachDirRecurse===> fit file: " + file.absolutePath)
                // we deal it
                asmToClass(file)
            }
        }

        hackerCallback.hackerCallback(directoryInput.file)
    }

    private static void asmToClass(File source) {
        try {
            // in
            FileInputStream fileInputStream = new FileInputStream(source.getAbsolutePath())
            ClassReader classReader = new ClassReader(fileInputStream)
            ClassWriter classWriter = new ClassWriter(classReader, 0)
            ClassVisitor visitor = new ThreadClassVisitor(Opcodes.ASM7, classWriter)
            classReader.accept(visitor, ClassReader.EXPAND_FRAMES)
            byte[] code = classWriter.toByteArray();

            // out
            FileOutputStream fos = new FileOutputStream(source.parentFile.absolutePath + File.separator + source.name)
            fos.write(code)
            fos.close()
        } catch (Exception e) {
            println("asmToClass===> error: " + e.toString())
        }
    }

    static interface IThreadHacker {
        abstract void hackerCallback(File hackedFile)
    }
}
