package com.cool.crashcapture_plugin.collector

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformInvocation
import com.android.utils.FileUtils
import com.cool.crashcapture_plugin.ThreadStateTransform
import com.cool.crashcapture_plugin.asm.ThreadClassVisitor
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.nio.file.Files
import java.nio.file.attribute.FileTime
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
class ThreadHacker {
    private static final FileTime ZERO = FileTime.fromMillis(0L);

    static void tryInjectWithClass(DirectoryInput directoryInput, File to, ThreadStateTransform transform) {
        println("tryInjectWithClass===> show directoryInput: name: " + directoryInput.name + " contentTypes: " + directoryInput.contentTypes + " scopes: " + directoryInput.scopes)
        // for each in file;
        directoryInput.file.eachFileRecurse {file ->
            println("tryInjectWithClass===> all file: " + file.absolutePath)
            if (file.directory || !isTargetClass(file.absolutePath)) {
                println("tryInjectWithClass===> no fit file: " + file.absolutePath)
            } else {
                println("tryInjectWithClass===> fit file: " + file.absolutePath)
                // we deal it
                asmToClass(file)
            }
        }
        FileUtils.copyDirectory(directoryInput.file, to)
    }

    static void tryInjectWithJar(JarInput jarInput, File to) {
        // output jar；
        ZipOutputStream outputZip = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(to.toPath())));
        ZipFile inputZip = new ZipFile(jarInput.file);
        Enumeration enumeration = inputZip.entries()
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement()
            ZipEntry outEntry = new ZipEntry(zipEntry.getName());

            // file in the jar；
            InputStream originFile = new BufferedInputStream(inputZip.getInputStream(zipEntry));

            byte[] newEntryContent = null;
            if (isTargetClass(outEntry.getName().replace("/", "."))) {
                newEntryContent = changesSingleClassToByteArray(originFile); // just change;
            } else {
                newEntryContent = IOUtils.toByteArray(originFile);
            }

            // write to new zip
            CRC32 crc32 = new CRC32();
            crc32.update(newEntryContent);
            outEntry.setCrc(crc32.getValue());
            outEntry.setMethod(0);
            outEntry.setSize((long)newEntryContent.length);
            outEntry.setCompressedSize((long)newEntryContent.length);
            outEntry.setLastAccessTime(ZERO);
            outEntry.setLastModifiedTime(ZERO);
            outEntry.setCreationTime(ZERO);
            outputZip.putNextEntry(outEntry);
            outputZip.write(newEntryContent);
            outputZip.closeEntry();
        }

        outputZip.flush();
        outputZip.close();
    }

    static byte[] changesSingleClassToByteArray(InputStream inputStream) throws IOException {
        ClassReader classReader = new ClassReader(inputStream)
        ClassWriter classWriter = new ClassWriter(classReader, 0)
        ClassVisitor visitor = new ThreadClassVisitor(Opcodes.ASM7, classWriter)
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray();
    }

    static boolean isTargetClass(String qualifiedName) {
        return qualifiedName.endsWith('.class') && !qualifiedName.contains('R$') && !qualifiedName.contains('R.class') && !qualifiedName.contains('BuildConfig.class');
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
        void hackerCallback(File hackedFile, File to)
    }
}
